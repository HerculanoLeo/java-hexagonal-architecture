import {useMemo, useState} from 'react';

import type {DataTableSortOrder} from './data-table.types';
import {getFirstFromPage} from './data-table.utils';

type UseDataTableStateOptions = {
	defaultSortField?: string;
	defaultSortOrder?: DataTableSortOrder;
	rows: number;
};

export function useDataTableState({
	defaultSortField,
	defaultSortOrder = 'asc',
	rows,
}: UseDataTableStateOptions) {
	const [page, setPage] = useState(1);
	const [sortField, setSortField] = useState<string | undefined>(
		defaultSortField,
	);
	const [sortOrder, setSortOrder] =
		useState<DataTableSortOrder>(defaultSortOrder);

	const first = useMemo(() => getFirstFromPage(page, rows), [page, rows]);

	function resetPage() {
		setPage(1);
	}

	function goToPage(nextPage: number) {
		setPage(Math.max(1, nextPage));
	}

	function toggleSort(field: string) {
		if (field === sortField) {
			setSortOrder((currentOrder) => (currentOrder === 'asc' ? 'desc' : 'asc'));
			return;
		}

		setSortField(field);
		setSortOrder('asc');
	}

	return {
		first,
		goToPage,
		page,
		resetPage,
		setPage: goToPage,
		setSortField,
		setSortOrder,
		sortField,
		sortOrder,
		toggleSort,
	};
}
