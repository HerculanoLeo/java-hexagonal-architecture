import {fireEvent, render, screen, within} from '@testing-library/react';
import type {ComponentProps} from 'react';

import {DataTable, DataTableColumn} from '../index';

type Row = {
	id: string;
	nome: string;
	email: string;
};

const rows: Row[] = [
	{ id: '1', nome: 'Ana Silva', email: 'ana@example.com' },
	{ id: '2', nome: 'Bruno Costa', email: 'bruno@example.com' },
];

function renderDataTable(
	props: Partial<ComponentProps<typeof DataTable<Row>>> = {},
) {
	return render(
		<DataTable<Row>
			value={props.value ?? rows}
			dataKey="id"
			{...props}
		>
			<DataTableColumn field="nome" header="Nome" sortable />
			<DataTableColumn field="email" header="E-mail" />
		</DataTable>,
	);
}

describe('DataTable', () => {
	it('renders table rows on desktop', () => {
		renderDataTable();

		const table = screen.getByRole('table');
		const tableRows = within(table).getAllByRole('row');

		expect(within(tableRows[1]).getByText('Ana Silva')).toBeInTheDocument();
		expect(within(tableRows[2]).getByText('Bruno Costa')).toBeInTheDocument();
	});

	it('shows the empty state message when there are no rows', () => {
		renderDataTable({ value: [] });

		const table = screen.getByRole('table');
		expect(within(table).getByText('Nenhum registro encontrado.')).toBeInTheDocument();
	});

	it('shows the loading state message', () => {
		renderDataTable({ loading: true });

		const table = screen.getByRole('table');
		expect(within(table).getByText('Carregando...')).toBeInTheDocument();
		expect(screen.queryByText('Ana Silva')).not.toBeInTheDocument();
	});

	it('sorts rows when a sortable header is clicked', () => {
		const unsortedRows: Row[] = [
			{ id: '1', nome: 'Zebra', email: 'z@example.com' },
			{ id: '2', nome: 'Alpha', email: 'a@example.com' },
		];

		renderDataTable({ value: unsortedRows });

		fireEvent.click(screen.getByRole('button', { name: 'Nome' }));

		const table = screen.getByRole('table');
		const firstDataRow = within(table).getAllByRole('row')[1];

		expect(within(firstDataRow).getByText('Alpha')).toBeInTheDocument();
	});

	it('paginates rows when paginator is enabled', () => {
		const paginatedRows: Row[] = [
			{ id: '1', nome: 'Row 1', email: '1@example.com' },
			{ id: '2', nome: 'Row 2', email: '2@example.com' },
			{ id: '3', nome: 'Row 3', email: '3@example.com' },
		];

		renderDataTable({
			value: paginatedRows,
			paginator: true,
			rows: 2,
		});

		const table = screen.getByRole('table');
		expect(within(table).getByText('Row 1')).toBeInTheDocument();
		expect(within(table).queryByText('Row 3')).not.toBeInTheDocument();

		fireEvent.click(screen.getByRole('button', { name: 'Próxima' }));

		expect(within(table).getByText('Row 3')).toBeInTheDocument();
		expect(within(table).queryByText('Row 1')).not.toBeInTheDocument();
	});
});
