import type {ReactNode} from 'react';

import type {DataTableColumnDefinition} from '../data-table.types';
import {isActionsColumn, renderColumnCell} from '../data-table.utils';

type DataTableMobileListProps<T extends Record<string, unknown>> = {
	columns: DataTableColumnDefinition<T>[];
	dataKey: keyof T & string;
	emptyMessage: string;
	loading: boolean;
	loadingMessage: string;
	rows: T[];
};

export function DataTableMobileList<T extends Record<string, unknown>>({
	columns,
	dataKey,
	emptyMessage,
	loading,
	loadingMessage,
	rows,
}: DataTableMobileListProps<T>) {
	const dataColumns = columns.filter((column) => !isActionsColumn(column));
	const actionColumns = columns.filter((column) => isActionsColumn(column));

	if (loading) {
		return (
			<div className="rounded-md border px-4 py-8 text-center text-sm text-muted-foreground">
				{loadingMessage}
			</div>
		);
	}

	if (rows.length === 0) {
		return (
			<div className="rounded-md border px-4 py-8 text-center text-sm text-muted-foreground">
				{emptyMessage}
			</div>
		);
	}

	return (
		<div className="divide-y rounded-md border">
			{rows.map((row) => (
				<article key={String(row[dataKey])} className="space-y-3 p-4">
					{dataColumns.map((column, index) => (
						<MobileField
							key={column.field ?? `mobile-column-${index}`}
							label={column.header}
							value={renderColumnCell(row, column)}
						/>
					))}

					{actionColumns.length > 0 ? (
						<div className="space-y-2 border-t pt-3 md:[&_a]:w-auto md:[&_button]:w-auto md:[&_button]:justify-center md:[&_button]:px-2">
							{actionColumns.map((column, index) => (
								<div
									key={`mobile-action-${index}`}
									className="[&_a]:inline-flex [&_a]:w-full [&_a]:items-center [&_a]:gap-2 [&_a]:text-sm [&_button]:inline-flex [&_button]:w-full [&_button]:items-center [&_button]:justify-start [&_button]:gap-2 [&_button]:px-0"
								>
									{column.body?.(row)}
								</div>
							))}
						</div>
					) : null}
				</article>
			))}
		</div>
	);
}

function MobileField({
	label,
	value,
}: {
	label?: ReactNode;
	value: ReactNode;
}) {
	if (!label) {
		return <div className="min-w-0 text-sm">{value}</div>;
	}

	return (
		<div className="grid grid-cols-[minmax(0,6.5rem)_1fr] gap-3 text-sm">
			<span className="text-muted-foreground">{label}</span>
			<div className="min-w-0 break-words">{value}</div>
		</div>
	);
}
