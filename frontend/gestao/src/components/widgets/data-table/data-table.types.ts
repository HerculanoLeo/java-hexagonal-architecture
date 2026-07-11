import type {ReactNode} from 'react';

export type DataTableSortOrder = 'asc' | 'desc';

export type DataTableColumnAlign = 'left' | 'center' | 'right';

export type DataTableColumnDefinition<T> = {
	align?: DataTableColumnAlign;
	body?: (row: T) => ReactNode;
	className?: string;
	field?: keyof T & string;
	header?: ReactNode;
	headerClassName?: string;
	mobileRole?: 'actions' | 'data';
	sortable?: boolean;
	sortValue?: (row: T) => string | number;
};

export type DataTableSortState = {
	field?: string;
	order: DataTableSortOrder;
};

export type DataTablePageState = {
	first: number;
	page: number;
	rows: number;
};

export type DataTableLazyLoadEvent = {
	first: number;
	filters?: Record<string, unknown>;
	rows: number;
	sortField?: string;
	sortOrder?: DataTableSortOrder;
};

export type DataTableProps<T> = {
	children?: ReactNode;
	dataKey: keyof T & string;
	emptyMessage?: string;
	first?: number;
	lazy?: boolean;
	loading?: boolean;
	loadingMessage?: string;
	onLazyLoad?: (event: DataTableLazyLoadEvent) => void;
	onPage?: (event: { first: number; page: number; rows: number }) => void;
	onSort?: (event: DataTableSortState) => void;
	paginator?: boolean;
	rows?: number;
	sortField?: string;
	sortOrder?: DataTableSortOrder;
	totalRecords?: number;
	value: T[];
};

export type DataTableColumnProps<T> = DataTableColumnDefinition<T>;

export const dataTableToolbarDisplayName = 'DataTableToolbar';
export const dataTableColumnDisplayName = 'DataTableColumn';
