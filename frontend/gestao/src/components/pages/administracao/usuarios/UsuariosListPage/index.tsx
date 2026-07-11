import {Link} from '@tanstack/react-router';
import {useQuery} from '@tanstack/react-query';
import {Pencil, Plus} from 'lucide-react';
import {useState} from 'react';

import {DataTable, DataTableColumn, DataTableToolbar,} from '@components/widgets/data-table';
import {StatusBadge} from '@components/widgets/status-badge';
import {ContentLayout} from '@components/widgets/content-layout';
import {Button} from '@components/ui/button';
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from '@components/ui/card';
import type {UsuarioDto, UsuarioSearchRequest} from '@entities/usuario';
import {USUARIOS_QUERY_KEY, usuariosService} from '@entities/usuario';
import {ativoInativoStatusDefinition, createStatusFormatter,} from '@shared/ui/status';

import {UsuarioSearchForm} from '@components/features/administracao/usuarios/UsuarioSearchForm';
import {usuariosBreadcrumbs} from '@components/pages/administracao/usuarios/lib/usuarios.breadcrumbs';

const pageSize = 5;
const usuarioStatusFormatter = createStatusFormatter(
	ativoInativoStatusDefinition,
);

export function UsuariosListPage() {
	const [filters, setFilters] = useState<UsuarioSearchRequest>({});

	const usuariosQuery = useQuery({
		queryKey: [USUARIOS_QUERY_KEY, 'findAll', filters],
		queryFn: () => usuariosService.findAll(filters),
	});

	return (
		<ContentLayout breadcrumbs={usuariosBreadcrumbs.list}>
			<Card>
				<CardHeader className="gap-4 lg:flex lg:flex-row lg:items-center lg:justify-between">
					<div className="space-y-2">
						<CardTitle>Usuários</CardTitle>
						<CardDescription>
							Consulte, filtre e gerencie os usuários do painel.
						</CardDescription>
					</div>
					<Button asChild>
						<Link to="/administracao/acesso/usuarios/novo">
							<Plus className="size-4" />
							Adicionar
						</Link>
					</Button>
				</CardHeader>
				<CardContent>
					<DataTable<UsuarioDto>
						value={usuariosQuery.data ?? []}
						dataKey="id"
						loading={usuariosQuery.isLoading}
						loadingMessage="Carregando usuários..."
						rows={pageSize}
						paginator
						emptyMessage="Nenhum usuário encontrado."
					>
						<DataTableToolbar>
							<UsuarioSearchForm onSearch={setFilters} />
						</DataTableToolbar>

						<DataTableColumn<UsuarioDto>
							field="nome"
							header="Nome"
							sortable
							className="font-medium"
							body={(usuario) => (
								<span className="inline-flex flex-wrap items-center gap-2">
									<span>{usuario.nome}</span>
									{usuario.main ? (
										<span className="rounded-md bg-muted px-1.5 py-0.5 text-xs font-medium text-muted-foreground">
											Principal
										</span>
									) : null}
								</span>
							)}
						/>
						<DataTableColumn<UsuarioDto>
							field="email"
							header="E-mail"
							className="text-muted-foreground"
						/>
						<DataTableColumn<UsuarioDto>
							field="status"
							header="Status"
							sortable
							sortValue={(usuario) =>
								usuarioStatusFormatter.getSortValue(usuario.status)
							}
							body={(usuario) => (
								<StatusBadge
									status={usuario.status}
									definition={ativoInativoStatusDefinition}
								/>
							)}
						/>
						<DataTableColumn<UsuarioDto>
							header="Ações"
							align="right"
							headerClassName="w-24"
							mobileRole="actions"
							body={(usuario) => (
								<Button
									variant="ghost"
									size="sm"
									asChild
									className="md:size-9 md:px-0"
								>
									<Link
										to="/administracao/acesso/usuarios/$usuarioId"
										params={{ usuarioId: usuario.id }}
										aria-label={`Editar ${usuario.nome}`}
										className="md:justify-center"
									>
										<Pencil className="size-4" />
										<span className="md:hidden">Editar</span>
									</Link>
								</Button>
							)}
						/>
					</DataTable>
				</CardContent>
			</Card>
		</ContentLayout>
	);
}
