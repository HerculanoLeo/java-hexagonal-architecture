import {act, renderHook} from '@testing-library/react';

import {useDataTableState} from './use-data-table-state';

describe('useDataTableState', () => {
	it('initializes pagination and sort state', () => {
		const { result } = renderHook(() =>
			useDataTableState({ rows: 10, defaultSortField: 'nome' }),
		);

		expect(result.current.page).toBe(1);
		expect(result.current.first).toBe(0);
		expect(result.current.sortField).toBe('nome');
		expect(result.current.sortOrder).toBe('asc');
	});

	it('changes page through goToPage', () => {
		const { result } = renderHook(() => useDataTableState({ rows: 5 }));

		act(() => {
			result.current.goToPage(3);
		});

		expect(result.current.page).toBe(3);
		expect(result.current.first).toBe(10);
	});

	it('sets a new sort field in ascending order', () => {
		const { result } = renderHook(() => useDataTableState({ rows: 10 }));

		act(() => {
			result.current.toggleSort('email');
		});

		expect(result.current.sortField).toBe('email');
		expect(result.current.sortOrder).toBe('asc');
	});

	it('toggles sort order when sorting the same field again', () => {
		const { result } = renderHook(() =>
			useDataTableState({ rows: 10, defaultSortField: 'nome' }),
		);

		act(() => {
			result.current.toggleSort('nome');
		});

		expect(result.current.sortOrder).toBe('desc');

		act(() => {
			result.current.toggleSort('nome');
		});

		expect(result.current.sortOrder).toBe('asc');
	});

	it('resets page to the first page', () => {
		const { result } = renderHook(() => useDataTableState({ rows: 10 }));

		act(() => {
			result.current.goToPage(4);
			result.current.resetPage();
		});

		expect(result.current.page).toBe(1);
		expect(result.current.first).toBe(0);
	});
});
