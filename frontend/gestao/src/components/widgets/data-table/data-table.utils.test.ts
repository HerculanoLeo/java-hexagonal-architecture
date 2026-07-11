import {
    getCurrentPage,
    getFieldValue,
    getPageCount,
    isActionsColumn,
    paginateRows,
    sortRows,
} from './data-table.utils';

type SampleRow = {
	id: string;
	nome: string;
	idade: number;
	criadoEm: Date;
};

describe('getFieldValue', () => {
	it('returns an empty string for null and undefined values', () => {
		const row = { id: '1', nome: 'Ana', idade: 30, criadoEm: new Date() };

		expect(getFieldValue({ ...row, nome: null as unknown as string }, 'nome')).toBe(
			'',
		);
	});

	it('serializes dates as ISO strings', () => {
		const date = new Date('2026-01-15T12:00:00.000Z');
		const row: SampleRow = { id: '1', nome: 'Ana', idade: 30, criadoEm: date };

		expect(getFieldValue(row, 'criadoEm')).toBe(date.toISOString());
	});
});

describe('sortRows', () => {
	const rows: SampleRow[] = [
		{ id: '1', nome: 'Zebra', idade: 20, criadoEm: new Date() },
		{ id: '2', nome: 'Alpha', idade: 10, criadoEm: new Date() },
	];

	it('sorts string fields in ascending order using pt-BR locale', () => {
		const sorted = sortRows(rows, 'nome', 'asc', [{ field: 'nome', header: 'Nome' }]);

		expect(sorted.map((row) => row.nome)).toEqual(['Alpha', 'Zebra']);
	});

	it('sorts numeric fields in descending order', () => {
		const sorted = sortRows(rows, 'idade', 'desc', [{ field: 'idade', header: 'Idade' }]);

		expect(sorted.map((row) => row.idade)).toEqual([20, 10]);
	});

	it('returns the original order when sort field is undefined', () => {
		expect(sortRows(rows, undefined, 'asc', [])).toEqual(rows);
	});
});

describe('paginateRows', () => {
	const rows = [{ id: '1' }, { id: '2' }, { id: '3' }];

	it('returns the requested page slice', () => {
		expect(paginateRows(rows, 1, 1)).toEqual([{ id: '2' }]);
	});
});

describe('getPageCount', () => {
	it('returns at least one page even when there are no records', () => {
		expect(getPageCount(0, 10)).toBe(1);
	});

	it('calculates the number of pages for partial results', () => {
		expect(getPageCount(11, 5)).toBe(3);
	});
});

describe('getCurrentPage', () => {
	it('converts the first index into a one-based page number', () => {
		expect(getCurrentPage(10, 5)).toBe(3);
	});
});

describe('isActionsColumn', () => {
	it('detects actions columns by mobile role or header label', () => {
		expect(isActionsColumn({ header: 'Nome', mobileRole: 'data' })).toBe(false);
		expect(isActionsColumn({ header: 'Ações' })).toBe(true);
		expect(isActionsColumn({ header: 'ações' })).toBe(true);
		expect(isActionsColumn({ header: 'Menu', mobileRole: 'actions' })).toBe(true);
	});
});
