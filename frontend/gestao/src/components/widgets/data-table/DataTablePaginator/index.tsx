import {Button} from '@components/ui/button';

import {useDataTableContext} from '../data-table.context';

export function DataTablePaginator() {
	const { onPageChange, page, pageCount, totalRecords } = useDataTableContext();

	return (
		<div className="flex flex-col gap-3 text-sm text-muted-foreground sm:flex-row sm:items-center sm:justify-between">
			<span>
				Página {page} de {pageCount} · {totalRecords} registro(s)
			</span>
			<div className="flex gap-2">
				<Button
					type="button"
					variant="outline"
					disabled={page === 1}
					onClick={() => onPageChange(page - 1)}
				>
					Anterior
				</Button>
				<Button
					type="button"
					variant="outline"
					disabled={page === pageCount}
					onClick={() => onPageChange(page + 1)}
				>
					Próxima
				</Button>
			</div>
		</div>
	);
}
