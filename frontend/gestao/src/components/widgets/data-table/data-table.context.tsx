import type {ReactElement, ReactNode} from 'react';
import {Children, createContext, isValidElement, useContext, useMemo,} from 'react';

import type {DataTableColumnDefinition, DataTableSortOrder,} from './data-table.types';
import {dataTableColumnDisplayName, dataTableToolbarDisplayName,} from './data-table.types';

type DataTableContextValue<T> = {
	columns: DataTableColumnDefinition<T>[];
	first: number;
	loading: boolean;
	onPageChange: (page: number) => void;
	onSort: (field: string) => void;
	page: number;
	pageCount: number;
	rows: T[];
	rowsPerPage: number;
	sortField?: string;
	sortOrder: DataTableSortOrder;
	toolbar?: ReactNode;
	totalRecords: number;
};

const DataTableContext = createContext<DataTableContextValue<unknown> | null>(
	null,
);

export function useDataTableContext<T>() {
	const context = useContext(DataTableContext);

	if (!context) {
		throw new Error('useDataTableContext must be used inside <DataTable>.');
	}

	return context as DataTableContextValue<T>;
}

type DataTableProviderProps<T> = {
	children: ReactNode;
	columns: DataTableColumnDefinition<T>[];
	first: number;
	loading: boolean;
	onPageChange: (page: number) => void;
	onSort: (field: string) => void;
	page: number;
	pageCount: number;
	rows: T[];
	rowsPerPage: number;
	sortField?: string;
	sortOrder: DataTableSortOrder;
	toolbar?: ReactNode;
	totalRecords: number;
};

export function DataTableProvider<T>({
	children,
	columns,
	first,
	loading,
	onPageChange,
	onSort,
	page,
	pageCount,
	rows,
	rowsPerPage,
	sortField,
	sortOrder,
	toolbar,
	totalRecords,
}: DataTableProviderProps<T>) {
	const value = useMemo(
		() => ({
			columns,
			first,
			loading,
			onPageChange,
			onSort,
			page,
			pageCount,
			rows,
			rowsPerPage,
			sortField,
			sortOrder,
			toolbar,
			totalRecords,
		}),
		[
			columns,
			first,
			loading,
			onPageChange,
			onSort,
			page,
			pageCount,
			rows,
			rowsPerPage,
			sortField,
			sortOrder,
			toolbar,
			totalRecords,
		],
	);

	return (
		<DataTableContext.Provider value={value as DataTableContextValue<unknown>}>
			{children}
		</DataTableContext.Provider>
	);
}

type ParsedDataTableChildren<T> = {
	columns: DataTableColumnDefinition<T>[];
	toolbar?: ReactNode;
};

export function parseDataTableChildren<T>(children: ReactNode) {
	const parsed: ParsedDataTableChildren<T> = {
		columns: [],
	};

	Children.forEach(children, (child) => {
		if (!isValidElement(child)) {
			return;
		}

		const element = child as ReactElement<{
			align?: DataTableColumnDefinition<T>['align'];
			body?: DataTableColumnDefinition<T>['body'];
			className?: string;
			field?: keyof T & string;
			header?: ReactNode;
			headerClassName?: string;
			mobileRole?: DataTableColumnDefinition<T>['mobileRole'];
			sortable?: boolean;
			sortValue?: DataTableColumnDefinition<T>['sortValue'];
		}>;

		if (getDisplayName(element) === dataTableToolbarDisplayName) {
			parsed.toolbar = (element.props as { children?: ReactNode }).children;
			return;
		}

		if (getDisplayName(element) === dataTableColumnDisplayName) {
			parsed.columns.push({
				align: element.props.align,
				body: element.props.body,
				className: element.props.className,
				field: element.props.field,
				header: element.props.header,
				headerClassName: element.props.headerClassName,
				mobileRole: element.props.mobileRole,
				sortable: element.props.sortable,
				sortValue: element.props.sortValue,
			});
		}
	});

	return parsed;
}

function getDisplayName(element: ReactElement) {
	const elementType = element.type as { displayName?: string };

	return elementType.displayName;
}
