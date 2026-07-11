import {ArrowDown, ArrowUp} from 'lucide-react';
import {useMemo} from 'react';

import {cn} from '@shared/ui/utils';

import {DataTableProvider, parseDataTableChildren,} from '../data-table.context';
import type {DataTableProps} from '../data-table.types';
import {DataTableMobileList} from '../DataTableMobileList';
import {DataTableMobileSort} from '../DataTableMobileSort';
import {DataTablePaginator} from '../DataTablePaginator';
import {
    getAlignClassName,
    getCurrentPage,
    getPageCount,
    paginateRows,
    renderColumnCell,
    sortRows,
} from '../data-table.utils';
import {useDataTableState} from '../use-data-table-state';

export function DataTable<T extends Record<string, unknown>>({
	children,
	dataKey,
	emptyMessage = 'Nenhum registro encontrado.',
	first: controlledFirst,
	lazy = false,
	loading = false,
	loadingMessage = 'Carregando...',
	onPage,
	onSort,
	paginator = false,
	rows = 10,
	sortField: controlledSortField,
	sortOrder: controlledSortOrder,
	totalRecords: controlledTotalRecords,
	value,
}: DataTableProps<T>) {
	const { columns, toolbar } = useMemo(
		() => parseDataTableChildren<T>(children),
		[children],
	);

	const internalState = useDataTableState({
		defaultSortField: controlledSortField,
		defaultSortOrder: controlledSortOrder,
		rows,
	});

	const sortField = controlledSortField ?? internalState.sortField;
	const sortOrder = controlledSortOrder ?? internalState.sortOrder;
	const first = controlledFirst ?? (paginator ? internalState.first : 0);
	const totalRecords = controlledTotalRecords ?? value.length;
	const pageCount = getPageCount(totalRecords, rows);
	const page = getCurrentPage(first, rows);

	const processedRows = useMemo(() => {
		if (lazy) {
			return value;
		}

		const sortedRows = sortRows(value, sortField, sortOrder, columns);

		if (!paginator) {
			return sortedRows;
		}

		return paginateRows(sortedRows, first, rows);
	}, [columns, first, lazy, paginator, rows, sortField, sortOrder, value]);

	function handleSort(field: string) {
		if (onSort) {
			const nextOrder =
				sortField === field && sortOrder === 'asc' ? 'desc' : 'asc';

			onSort({
				field,
				order: sortField === field ? nextOrder : 'asc',
			});
			return;
		}

		internalState.toggleSort(field);
	}

	function handleMobileSort(field: string, order: 'asc' | 'desc') {
		if (onSort) {
			onSort({ field, order });
			return;
		}

		internalState.setSortField(field);
		internalState.setSortOrder(order);
	}

	function handlePageChange(nextPage: number) {
		const nextFirst = (nextPage - 1) * rows;

		if (onPage) {
			onPage({
				first: nextFirst,
				page: nextPage,
				rows,
			});
			return;
		}

		internalState.goToPage(nextPage);
	}

	return (
		<DataTableProvider
			columns={columns}
			first={first}
			loading={loading}
			onPageChange={handlePageChange}
			onSort={handleSort}
			page={page}
			pageCount={pageCount}
			rows={processedRows}
			rowsPerPage={rows}
			sortField={sortField}
			sortOrder={sortOrder}
			toolbar={toolbar}
			totalRecords={totalRecords}
		>
			<div className="space-y-4">
				{toolbar ? <div>{toolbar}</div> : null}

				<div className="space-y-4 md:hidden">
					<DataTableMobileSort
						columns={columns}
						onSort={handleMobileSort}
						sortField={sortField}
						sortOrder={sortOrder}
					/>
					<DataTableMobileList
						columns={columns}
						dataKey={dataKey}
						emptyMessage={emptyMessage}
						loading={loading}
						loadingMessage={loadingMessage}
						rows={processedRows}
					/>
				</div>

				<div className="hidden overflow-hidden rounded-md border md:block">
					<table className="w-full text-sm">
						<thead className="bg-muted/50">
							<tr className="border-b">
								{columns.map((column, index) => {
									const isActive = Boolean(
										column.field && column.field === sortField,
									);
									const Icon = sortOrder === 'asc' ? ArrowUp : ArrowDown;

									return (
										<th
											key={column.field ?? `column-${index}`}
											className={cn(
												'px-4 py-3 font-medium',
												getAlignClassName(column.align),
												column.headerClassName,
											)}
										>
											{column.sortable && column.field ? (
												<button
													type="button"
													className="inline-flex cursor-pointer items-center gap-2 rounded-sm transition-colors hover:text-foreground"
													onClick={() => handleSort(column.field!)}
												>
													{column.header}
													{isActive ? <Icon className="size-3.5" /> : null}
												</button>
											) : (
												column.header
											)}
										</th>
									);
								})}
							</tr>
						</thead>
						<tbody>
							{loading ? (
								<tr>
									<td
										colSpan={columns.length}
										className="px-4 py-8 text-center text-muted-foreground"
									>
										{loadingMessage}
									</td>
								</tr>
							) : null}
							{!loading && processedRows.length === 0 ? (
								<tr>
									<td
										colSpan={columns.length}
										className="px-4 py-8 text-center text-muted-foreground"
									>
										{emptyMessage}
									</td>
								</tr>
							) : null}
							{!loading
								? processedRows.map((row) => (
										<tr
											key={String(row[dataKey])}
											className="border-b last:border-0"
										>
											{columns.map((column, index) => (
												<td
													key={column.field ?? `column-${index}`}
													className={cn(
														'px-4 py-3',
														getAlignClassName(column.align),
														column.className,
													)}
												>
													{renderColumnCell(row, column)}
												</td>
											))}
										</tr>
									))
								: null}
						</tbody>
					</table>
				</div>

				{paginator ? <DataTablePaginator /> : null}
			</div>
		</DataTableProvider>
	);
}
