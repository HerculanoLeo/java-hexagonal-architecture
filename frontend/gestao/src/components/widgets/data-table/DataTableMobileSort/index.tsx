import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue,} from '@components/ui/select';
import {Label} from '@components/ui/label';

import type {DataTableColumnDefinition, DataTableSortOrder,} from '../data-table.types';

type DataTableMobileSortProps<T> = {
	columns: DataTableColumnDefinition<T>[];
	onSort: (field: string, order: DataTableSortOrder) => void;
	sortField?: string;
	sortOrder: DataTableSortOrder;
};

export function DataTableMobileSort<T>({
	columns,
	onSort,
	sortField,
	sortOrder,
}: DataTableMobileSortProps<T>) {
	const sortableColumns = columns.filter(
		(column) => column.sortable && column.field,
	);

	if (sortableColumns.length === 0) {
		return null;
	}

	const currentValue = sortField ? `${sortField}:${sortOrder}` : undefined;

	return (
		<div className="space-y-2">
			<Label htmlFor="data-table-mobile-sort">Ordenar</Label>
			<Select
				value={currentValue}
				onValueChange={(value) => {
					const [field, order] = value.split(':') as [
						string,
						DataTableSortOrder,
					];

					onSort(field, order);
				}}
			>
				<SelectTrigger id="data-table-mobile-sort" className="w-full">
					<SelectValue placeholder="Selecione a ordenação" />
				</SelectTrigger>
				<SelectContent>
					{sortableColumns.flatMap((column) => {
						const field = column.field!;

						return [
							<SelectItem key={`${field}:asc`} value={`${field}:asc`}>
								{column.header} ascendente
							</SelectItem>,
							<SelectItem key={`${field}:desc`} value={`${field}:desc`}>
								{column.header} descendente
							</SelectItem>,
						];
					})}
				</SelectContent>
			</Select>
		</div>
	);
}
