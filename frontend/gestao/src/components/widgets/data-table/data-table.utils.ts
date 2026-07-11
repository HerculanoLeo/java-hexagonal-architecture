import type {DataTableColumnDefinition, DataTableSortOrder,} from './data-table.types';

export function getFieldValue<T extends Record<string, unknown>>(
	row: T,
	field: keyof T & string,
) {
	const value = row[field];

	if (value === null || value === undefined) {
		return '';
	}

	if (value instanceof Date) {
		return value.toISOString();
	}

	return String(value);
}

export function getSortValue<T>(
	row: T,
	field: string,
	columns: DataTableColumnDefinition<T>[],
) {
	const column = columns.find((item) => item.field === field);

	if (column?.sortValue) {
		return column.sortValue(row);
	}

	return getFieldValue(row as Record<string, unknown>, field);
}

export function sortRows<T>(
	rows: T[],
	field: string | undefined,
	order: DataTableSortOrder,
	columns: DataTableColumnDefinition<T>[],
) {
	if (!field) {
		return rows;
	}

	return [...rows].sort((rowA, rowB) => {
		const valueA = getSortValue(rowA, field, columns);
		const valueB = getSortValue(rowB, field, columns);

		if (typeof valueA === 'number' && typeof valueB === 'number') {
			return order === 'asc' ? valueA - valueB : valueB - valueA;
		}

		const comparison = String(valueA).localeCompare(String(valueB), 'pt-BR');

		return order === 'asc' ? comparison : comparison * -1;
	});
}

export function paginateRows<T>(rows: T[], first: number, rowsPerPage: number) {
	return rows.slice(first, first + rowsPerPage);
}

export function getPageCount(totalRecords: number, rowsPerPage: number) {
	return Math.max(1, Math.ceil(totalRecords / rowsPerPage));
}

export function getCurrentPage(first: number, rowsPerPage: number) {
	return Math.floor(first / rowsPerPage) + 1;
}

export function getFirstFromPage(page: number, rowsPerPage: number) {
	return (page - 1) * rowsPerPage;
}

export function getAlignClassName(
	align: DataTableColumnDefinition<unknown>['align'],
) {
	switch (align) {
		case 'center':
			return 'text-center';
		case 'right':
			return 'text-right';
		default:
			return 'text-left';
	}
}

export function isActionsColumn<T>(column: DataTableColumnDefinition<T>) {
	if (column.mobileRole === 'actions') {
		return true;
	}

	if (column.mobileRole === 'data') {
		return false;
	}

	return String(column.header ?? '').toLowerCase() === 'ações';
}

export function renderColumnCell<T extends Record<string, unknown>>(
	row: T,
	column: DataTableColumnDefinition<T>,
) {
	if (column.body) {
		return column.body(row);
	}

	if (column.field) {
		return getFieldValue(row, column.field);
	}

	return null;
}
