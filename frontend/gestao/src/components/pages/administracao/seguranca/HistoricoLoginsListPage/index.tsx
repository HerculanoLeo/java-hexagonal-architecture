import {useQuery} from '@tanstack/react-query';
import {useState} from 'react';

import {DataTable, DataTableColumn, DataTableToolbar,} from '@components/widgets/data-table';
import {ContentLayout} from '@components/widgets/content-layout';
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from '@components/ui/card';
import type {HistoricoLoginDto, HistoricoLoginSearchRequest,} from '@entities/historico-login';
import {HISTORICO_LOGIN_QUERY_KEY, historicoLoginService,} from '@entities/historico-login';
import {formatDateTime} from '@shared/ui/formatters/date.formatters';

import {HistoricoLoginSearchForm} from '@components/features/administracao/seguranca/HistoricoLoginSearchForm';
import {historicoLoginsBreadcrumbs} from '@components/pages/administracao/seguranca/lib/historico-logins.breadcrumbs';

const pageSize = 10;

function tipoLabel(tipo: string) {
	switch (tipo) {
		case 'ST':
		case 'USUARIO_SISTEMA':
			return 'Sistema (ST)';
		case 'CL':
		case 'CLIENTE_SISTEMA':
			return 'Cliente (CL)';
		default:
			return tipo;
	}
}

export function HistoricoLoginsListPage() {
	const [filters, setFilters] = useState<HistoricoLoginSearchRequest>({});

	const historicoQuery = useQuery({
		queryKey: [HISTORICO_LOGIN_QUERY_KEY, 'findAll', filters],
		queryFn: () => historicoLoginService.findAll(filters),
	});

	return (
		<ContentLayout breadcrumbs={historicoLoginsBreadcrumbs.list}>
			<Card>
				<CardHeader>
					<div className="space-y-2">
						<CardTitle>Histórico de logins</CardTitle>
						<CardDescription>
							Consulte logins bem-sucedidos por período, e-mail, tipo de acesso
							e tenant.
						</CardDescription>
					</div>
				</CardHeader>
				<CardContent>
					<DataTable<HistoricoLoginDto>
						value={historicoQuery.data ?? []}
						dataKey="id"
						loading={historicoQuery.isLoading}
						loadingMessage="Carregando histórico..."
						rows={pageSize}
						paginator
						emptyMessage="Nenhum login encontrado."
					>
						<DataTableToolbar>
							<HistoricoLoginSearchForm onSearch={setFilters} />
						</DataTableToolbar>

						<DataTableColumn<HistoricoLoginDto>
							field="dataEvento"
							header="Data"
							sortable
							body={(row) => formatDateTime(row.dataEvento)}
						/>
						<DataTableColumn<HistoricoLoginDto>
							field="nome"
							header="Usuário"
							sortable
							className="font-medium"
							body={(row) => row.nome || '—'}
						/>
						<DataTableColumn<HistoricoLoginDto>
							field="email"
							header="E-mail"
							className="text-muted-foreground"
							body={(row) => row.email || '—'}
						/>
						<DataTableColumn<HistoricoLoginDto>
							field="tipo"
							header="Tipo"
							body={(row) => tipoLabel(row.tipo)}
						/>
						<DataTableColumn<HistoricoLoginDto>
							field="relacionadoId"
							header="Tenant"
							className="font-mono text-xs text-muted-foreground"
							body={(row) => row.relacionadoId || '—'}
						/>
						<DataTableColumn<HistoricoLoginDto>
							field="ip"
							header="IP"
							body={(row) => row.ip || '—'}
						/>
						<DataTableColumn<HistoricoLoginDto>
							field="userAgent"
							header="User-agent"
							className="max-w-[240px] truncate text-muted-foreground"
							body={(row) => row.userAgent || '—'}
						/>
					</DataTable>
				</CardContent>
			</Card>
		</ContentLayout>
	);
}
