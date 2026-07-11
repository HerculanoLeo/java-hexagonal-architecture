import type {ReactNode} from 'react';

import {dataTableColumnDisplayName} from '../data-table.types';

export function DataTableColumn<T>(_props: {
	align?: 'left' | 'center' | 'right';
	body?: (row: T) => ReactNode;
	className?: string;
	field?: keyof T & string;
	header?: ReactNode;
	headerClassName?: string;
	mobileRole?: 'actions' | 'data';
	sortable?: boolean;
	sortValue?: (row: T) => string | number;
}) {
	return null;
}

DataTableColumn.displayName = dataTableColumnDisplayName;
