import {getRouteApi} from '@tanstack/react-router';
import {useMutation, useQuery, useQueryClient} from '@tanstack/react-query';
import {ChevronDown, LogOut, ShieldOff} from 'lucide-react';
import {useState} from 'react';

import {ContentLayout} from '@components/widgets/content-layout';
import {formActionDeleteButtonClassName} from '@components/forms/FormActions';
import {Button} from '@components/ui/button';
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from '@components/ui/card';
import {
    Dialog,
    DialogClose,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from '@components/ui/dialog';
import type {UsuarioOnlineGroupDto, UsuarioOnlineSessionDto,} from '@entities/session';
import {
    fetchUsuariosOnline,
    getUsuariosOnlineErrorMessage,
    getUsuariosOnlineQueryKey,
    invalidateUsuariosOnline,
    revokeAllSessions,
    revokeSession,
} from '@entities/session';
import {invalidateAuthSession} from '@shared/auth/auth.server-fn';
import {cn} from '@shared/ui/utils';
import {formatDateTime} from '@shared/ui/formatters/date.formatters';
import {appToast} from '@shared/ui/toast';

import {usuariosOnlineBreadcrumbs} from '@components/pages/administracao/seguranca/lib/usuarios-online.breadcrumbs';

const routeApi = getRouteApi(
	'/(authenticated)/administracao/seguranca/usuarios-online/',
);

type PendingRevokeAction =
	| {
			kind: 'session';
			session: UsuarioOnlineSessionDto;
			user: UsuarioOnlineGroupDto;
	  }
	| {
			kind: 'all';
			user: UsuarioOnlineGroupDto;
	  };

export function UsuariosOnlineListPage() {
	const { currentSessionId, currentUserId } = routeApi.useLoaderData();
	const queryClient = useQueryClient();
	const [expandedUserIds, setExpandedUserIds] = useState<Set<string>>(
		new Set(),
	);
	const [pendingAction, setPendingAction] =
		useState<PendingRevokeAction | null>(null);

	const usuariosOnlineQuery = useQuery({
		queryFn: fetchUsuariosOnline,
		queryKey: getUsuariosOnlineQueryKey(),
		refetchInterval: 30_000,
	});

	const revokeMutation = useMutation({
		mutationFn: async (action: PendingRevokeAction) => {
			if (action.kind === 'session') {
				await revokeSession(action.session.id);
				return action;
			}

			await revokeAllSessions(action.user.userId);
			return action;
		},
		onSuccess: async (action) => {
			await invalidateUsuariosOnline(queryClient);
			setPendingAction(null);

			const affectsCurrentSession =
				action.kind === 'session'
					? action.session.id === currentSessionId
					: action.user.userId === currentUserId;

			if (affectsCurrentSession) {
				appToast.informacao({
					title: 'Sessão encerrada',
					description:
						'Sua sessão atual foi revogada. Redirecionando para o login.',
				});
				await invalidateAuthSession();
				window.location.assign('/login');
				return;
			}

			appToast.sucesso({
				title:
					action.kind === 'session' ? 'Sessão revogada' : 'Sessões revogadas',
				description:
					action.kind === 'session'
						? 'A sessão selecionada foi encerrada com sucesso.'
						: `Todas as ${action.user.sessionCount} sessões de ${action.user.userName} foram encerradas.`,
			});
		},
		onError: (error) => {
			appToast.falha({
				title: 'Erro ao revogar sessão',
				description: getUsuariosOnlineErrorMessage(error),
			});
		},
	});

	function toggleExpanded(userId: string) {
		setExpandedUserIds((current) => {
			const next = new Set(current);

			if (next.has(userId)) {
				next.delete(userId);
			} else {
				next.add(userId);
			}

			return next;
		});
	}

	const groups = usuariosOnlineQuery.data ?? [];

	return (
		<ContentLayout breadcrumbs={usuariosOnlineBreadcrumbs.list}>
			<Card>
				<CardHeader>
					<CardTitle>Usuários Online</CardTitle>
					<CardDescription>
						Consulte sessões ativas agrupadas por usuário. A lista é atualizada
						automaticamente a cada 30 segundos.
					</CardDescription>
				</CardHeader>
				<CardContent>
					{usuariosOnlineQuery.isLoading ? (
						<p className="text-sm text-muted-foreground">
							Carregando usuários online...
						</p>
					) : groups.length === 0 ? (
						<p className="text-sm text-muted-foreground">
							Nenhum usuário online no momento.
						</p>
					) : (
						<div className="overflow-x-auto rounded-lg border">
							<table className="w-full min-w-[56rem] text-sm">
								<thead className="border-b bg-muted/40 text-left">
									<tr>
										<th className="w-10 px-3 py-3" aria-hidden />
										<th className="px-3 py-3 font-medium">Nome</th>
										<th className="px-3 py-3 font-medium">E-mail</th>
										<th className="px-3 py-3 font-medium">Sessões</th>
										<th className="px-3 py-3 font-medium">Última atividade</th>
										<th className="px-3 py-3 text-right font-medium">Ações</th>
									</tr>
								</thead>
								<tbody>
									{groups.map((group) => {
										const isExpanded = expandedUserIds.has(group.userId);

										return (
											<UsuarioOnlineGroupRow
												key={group.userId}
												currentSessionId={currentSessionId}
												group={group}
												isExpanded={isExpanded}
												isRevoking={revokeMutation.isPending}
												onRevokeAll={() =>
													setPendingAction({ kind: 'all', user: group })
												}
												onRevokeSession={(session) =>
													setPendingAction({
														kind: 'session',
														session,
														user: group,
													})
												}
												onToggleExpanded={() => toggleExpanded(group.userId)}
											/>
										);
									})}
								</tbody>
							</table>
						</div>
					)}
				</CardContent>
			</Card>

			<RevokeConfirmDialog
				action={pendingAction}
				currentSessionId={currentSessionId}
				isSubmitting={revokeMutation.isPending}
				onConfirm={() => {
					if (pendingAction) {
						revokeMutation.mutate(pendingAction);
					}
				}}
				onOpenChange={(open) => {
					if (!open) {
						setPendingAction(null);
					}
				}}
				open={pendingAction !== null}
			/>
		</ContentLayout>
	);
}

function UsuarioOnlineGroupRow({
	currentSessionId,
	group,
	isExpanded,
	isRevoking,
	onRevokeAll,
	onRevokeSession,
	onToggleExpanded,
}: {
	currentSessionId: string;
	group: UsuarioOnlineGroupDto;
	isExpanded: boolean;
	isRevoking: boolean;
	onRevokeAll: () => void;
	onRevokeSession: (session: UsuarioOnlineSessionDto) => void;
	onToggleExpanded: () => void;
}) {
	return (
		<>
			<tr className="border-b last:border-b-0">
				<td className="px-3 py-3 align-top">
					<Button
						type="button"
						variant="ghost"
						size="icon-sm"
						onClick={onToggleExpanded}
						aria-expanded={isExpanded}
						aria-label={
							isExpanded
								? `Recolher sessões de ${group.userName}`
								: `Expandir sessões de ${group.userName}`
						}
					>
						<ChevronDown
							className={cn(
								'size-4 transition-transform duration-200',
								isExpanded && 'rotate-180',
							)}
						/>
					</Button>
				</td>
				<td className="px-3 py-3 align-top font-medium">{group.userName}</td>
				<td className="px-3 py-3 align-top text-muted-foreground">
					{group.userEmail}
				</td>
				<td className="px-3 py-3 align-top">{group.sessionCount}</td>
				<td className="px-3 py-3 align-top">
					{formatDateTime(group.lastActivityAt)}
				</td>
				<td className="px-3 py-3 align-top text-right">
					<Button
						type="button"
						variant="outline"
						size="sm"
						disabled={isRevoking}
						onClick={onRevokeAll}
					>
						<ShieldOff className="size-4" />
						Revogar todas
					</Button>
				</td>
			</tr>

			{isExpanded && (
				<tr className="border-b bg-muted/20">
					<td colSpan={6} className="px-3 py-3">
						<div className="overflow-x-auto rounded-md border bg-background">
							<table className="w-full min-w-[48rem] text-sm">
								<thead className="border-b text-left">
									<tr>
										<th className="px-3 py-2 font-medium">IP</th>
										<th className="px-3 py-2 font-medium">Dispositivo</th>
										<th className="px-3 py-2 font-medium">Início</th>
										<th className="px-3 py-2 font-medium">Expiração</th>
										<th className="px-3 py-2 text-right font-medium">Ações</th>
									</tr>
								</thead>
								<tbody>
									{group.sessions.map((session) => (
										<tr key={session.id} className="border-b last:border-b-0">
											<td className="px-3 py-2">{session.ipAddress ?? '—'}</td>
											<td
												className="max-w-xs truncate px-3 py-2"
												title={session.userAgent ?? undefined}
											>
												{truncateUserAgent(session.userAgent)}
											</td>
											<td className="px-3 py-2">
												{formatDateTime(session.createdAt)}
											</td>
											<td className="px-3 py-2">
												{formatDateTime(session.expiresAt)}
											</td>
											<td className="px-3 py-2 text-right">
												<Button
													type="button"
													variant="ghost"
													size="sm"
													disabled={isRevoking}
													onClick={() => onRevokeSession(session)}
												>
													<LogOut className="size-4" />
													Revogar
													{session.id === currentSessionId && (
														<span className="text-xs text-muted-foreground">
															(sua sessão)
														</span>
													)}
												</Button>
											</td>
										</tr>
									))}
								</tbody>
							</table>
						</div>
					</td>
				</tr>
			)}
		</>
	);
}

function RevokeConfirmDialog({
	action,
	currentSessionId,
	isSubmitting,
	onConfirm,
	onOpenChange,
	open,
}: {
	action: PendingRevokeAction | null;
	currentSessionId: string;
	isSubmitting: boolean;
	onConfirm: () => void;
	onOpenChange: (open: boolean) => void;
	open: boolean;
}) {
	if (!action) {
		return null;
	}

	const affectsCurrentSession =
		action.kind === 'session'
			? action.session.id === currentSessionId
			: action.user.sessions.some((session) => session.id === currentSessionId);

	const title =
		action.kind === 'session' ? 'Revogar sessão' : 'Revogar todas as sessões';

	const description =
		action.kind === 'session' ? (
			<>
				Tem certeza que deseja revogar a sessão de{' '}
				<strong>{action.user.userName}</strong>
				{action.session.ipAddress ? ` (${action.session.ipAddress})` : ''}?
			</>
		) : (
			<>
				Tem certeza que deseja revogar todas as{' '}
				<strong>{action.user.sessionCount}</strong> sessões de{' '}
				<strong>{action.user.userName}</strong>?
			</>
		);

	return (
		<Dialog open={open} onOpenChange={onOpenChange}>
			<DialogContent>
				<DialogHeader>
					<DialogTitle>{title}</DialogTitle>
					<DialogDescription asChild>
						<div className="space-y-2 text-sm text-muted-foreground">
							<p>{description}</p>
							{affectsCurrentSession && (
								<p className="font-medium text-foreground">
									Atenção: esta ação inclui sua sessão atual e você será
									desconectado.
								</p>
							)}
						</div>
					</DialogDescription>
				</DialogHeader>
				<DialogFooter>
					<DialogClose asChild>
						<Button type="button" variant="outline" disabled={isSubmitting}>
							Cancelar
						</Button>
					</DialogClose>
					<Button
						type="button"
						className={formActionDeleteButtonClassName}
						disabled={isSubmitting}
						onClick={onConfirm}
					>
						Revogar
					</Button>
				</DialogFooter>
			</DialogContent>
		</Dialog>
	);
}

function truncateUserAgent(userAgent: string | null, maxLength = 72) {
	if (!userAgent) {
		return '—';
	}

	if (userAgent.length <= maxLength) {
		return userAgent;
	}

	return `${userAgent.slice(0, maxLength)}…`;
}
