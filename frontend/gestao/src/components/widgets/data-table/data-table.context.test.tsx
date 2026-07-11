import {parseDataTableChildren} from './data-table.context';
import {DataTableColumn} from './DataTableColumn';
import {DataTableToolbar} from './DataTableToolbar';

type Row = { id: string; nome: string };

describe('parseDataTableChildren', () => {
	it('extracts column definitions from DataTableColumn children', () => {
		const parsed = parseDataTableChildren<Row>([
			<DataTableColumn field="nome" header="Nome" sortable />,
			<DataTableColumn field="id" header="ID" />,
		]);

		expect(parsed.columns).toEqual([
			{
				align: undefined,
				body: undefined,
				className: undefined,
				field: 'nome',
				header: 'Nome',
				headerClassName: undefined,
				mobileRole: undefined,
				sortable: true,
				sortValue: undefined,
			},
			{
				align: undefined,
				body: undefined,
				className: undefined,
				field: 'id',
				header: 'ID',
				headerClassName: undefined,
				mobileRole: undefined,
				sortable: undefined,
				sortValue: undefined,
			},
		]);
	});

	it('extracts toolbar children', () => {
		const parsed = parseDataTableChildren<Row>([
			<DataTableToolbar>
				<button type="button">Novo</button>
			</DataTableToolbar>,
			<DataTableColumn field="nome" header="Nome" />,
		]);

		expect(parsed.toolbar).toBeDefined();
		expect(parsed.columns).toHaveLength(1);
	});
});
