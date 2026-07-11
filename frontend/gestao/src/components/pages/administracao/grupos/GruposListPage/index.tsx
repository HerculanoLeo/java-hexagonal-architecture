import {Link} from '@tanstack/react-router';
import {useQuery} from '@tanstack/react-query';
import {Pencil, Plus} from 'lucide-react';
import {useMemo, useState} from 'react';

import {DataTable, DataTableColumn, DataTableToolbar,} from '@components/widgets/data-table';
import {ContentLayout} from '@components/widgets/content-layout';
import {Badge} from '@components/ui/badge';
import {Button} from '@components/ui/button';
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from '@components/ui/card';
import type {GrupoDto, GrupoSearchRequest} from '@entities/grupo';
import {GRUPOS_QUERY_KEY, grupoService} from '@entities/grupo';
import {ROLES_QUERY_KEY, rolesService} from '@entities/role';

import {GrupoSearchForm} from '@components/features/administracao/grupos/GrupoSearchForm';
import {gruposBreadcrumbs} from '@components/pages/administracao/grupos/lib/grupos.breadcrumbs';

const pageSize = 5;

export function GruposListPage() {
	const [filters, setFilters] = useState<GrupoSearchRequest>({});

	const gruposQuery = useQuery({
		queryKey: [GRUPOS_QUERY_KEY, 'findAll', filters],
		queryFn: () => grupoService.findAll(filters),
	});

	const rolesQuery = useQuery({
		queryKey: [ROLES_QUERY_KEY, 'findAll'],
		queryFn: () => rolesService.findAll(),
	});

	const roleDescriptionByNome = useMemo(() => {
		const map = new Map<string, string>();

		for (const role of rolesQuery.data ?? []) {
			map.set(role.nome, role.descricao);
		}

		return map;
	}, [rolesQuery.data]);

	return (
		<ContentLayout breadcrumbs={gruposBreadcrumbs.list}>
			<Card>
				<CardHeader className="gap-4 lg:flex lg:flex-row lg:items-center lg:justify-between">
					<div className="space-y-2">
						<CardTitle>Grupos</CardTitle>
						<CardDescription>
							Consulte, filtre e gerencie os grupos de acesso do painel.
						</CardDescription>
					</div>
					<Button asChild>
						<Link to="/administracao/acesso/grupos/novo">
							<Plus className="size-4" />
							Adicionar
						</Link>
					</Button>
				</CardHeader>
				<CardContent>
					<DataTable<GrupoDto>
						value={gruposQuery.data ?? []}
						dataKey="id"
						loading={gruposQuery.isLoading}
						loadingMessage="Carregando grupos..."
						rows={pageSize}
						paginator
						emptyMessage="Nenhum grupo encontrado."
					>
						<DataTableToolbar>
							<GrupoSearchForm onSearch={setFilters} />
						</DataTableToolbar>

						<DataTableColumn<GrupoDto>
							field="nome"
							header="Nome"
							sortable
							className="font-medium"
						/>
						<DataTableColumn<GrupoDto>
							header="Permissões"
							body={(grupo) => (
								<div className="flex flex-wrap gap-1">
									{grupo.roles.map((roleNome) => (
										<Badge key={roleNome} variant="secondary">
											{roleDescriptionByNome.get(roleNome) ?? roleNome}
										</Badge>
									))}
								</div>
							)}
						/>
						<DataTableColumn<GrupoDto>
							header="Ações"
							align="right"
							headerClassName="w-24"
							mobileRole="actions"
							body={(grupo) => (
								<Button
									variant="ghost"
									size="sm"
									asChild
									className="md:size-9 md:px-0"
								>
									<Link
										to="/administracao/acesso/grupos/$grupoId"
										params={{ grupoId: grupo.id }}
										aria-label={`Editar ${grupo.nome}`}
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
