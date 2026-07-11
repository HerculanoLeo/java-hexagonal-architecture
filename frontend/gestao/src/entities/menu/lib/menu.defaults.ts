import type {MenuDto} from '../model/menu.dto';
import {buildMenuTreeFromNodes} from '../lib/menu-tree.utils';
import {SISTEMAS} from '@shared/auth/auth-permissions.enum';

export type MenuDefaultNode = {
	id: string;
	parentId: string | null;
	label: string;
	description?: string;
	icon?: string;
	sortOrder: number;
	enabled?: boolean;
	roles?: string[];
	route?: string;
	externalUrl?: string;
};

export const DEFAULT_MENU_NODES: MenuDefaultNode[] = [
	{
		id: 'administracao',
		parentId: null,
		label: 'Administração',
		description: 'Gestão administrativa do sistema',
		icon: 'Building2',
		sortOrder: 0,
	},
	{
		id: 'acesso',
		parentId: 'administracao',
		label: 'Acesso',
		description: 'Usuários, grupos e permissões',
		icon: 'ShieldCheck',
		sortOrder: 0,
	},
	{
		id: 'usuarios',
		parentId: 'acesso',
		label: 'Usuários',
		description: 'Gerencie usuários com acesso ao sistema',
		icon: 'Users',
		sortOrder: 0,
		roles: [SISTEMAS.USUARIOS],
		route: '/administracao/acesso/usuarios',
	},
	{
		id: 'grupos',
		parentId: 'acesso',
		label: 'Grupos',
		description: 'Organize perfis, grupos e permissões',
		icon: 'UserRound',
		sortOrder: 1,
		roles: [SISTEMAS.GRUPOS],
		route: '/administracao/acesso/grupos',
	},
	{
		id: 'seguranca',
		parentId: 'administracao',
		label: 'Segurança',
		description: 'Monitoramento e controle de sessões',
		icon: 'Shield',
		sortOrder: 1,
	},
	{
		id: 'usuarios-online',
		parentId: 'seguranca',
		label: 'Usuários Online',
		description: 'Visualize e revogue sessões ativas',
		icon: 'Activity',
		sortOrder: 0,
		roles: [SISTEMAS.SEGURANCA_USUARIOS_ONLINE],
		route: '/administracao/seguranca/usuarios-online',
	},
	{
		id: 'historico-logins',
		parentId: 'seguranca',
		label: 'Histórico de logins',
		description: 'Auditoria de logins bem-sucedidos',
		icon: 'History',
		sortOrder: 1,
		roles: [SISTEMAS.SEGURANCA_HISTORICO_LOGINS],
		route: '/administracao/seguranca/historico-logins',
	},
	{
		id: 'configuracoes',
		parentId: 'administracao',
		label: 'Configurações',
		description: 'Parâmetros gerais da área administrativa',
		icon: 'Settings',
		sortOrder: 2,
		roles: [SISTEMAS.CONFIGURACAO],
		route: '/administracao/configuracoes',
	},
];

export function getDefaultNavigationMenus(): MenuDto[] {
	return buildMenuTreeFromNodes(
		DEFAULT_MENU_NODES.map((node) => ({
			description: node.description ?? null,
			enabled: node.enabled ?? true,
			externalUrl: node.externalUrl ?? null,
			icon: node.icon ?? null,
			id: node.id,
			label: node.label,
			parentId: node.parentId,
			roles: node.roles ?? [],
			route: node.route ?? null,
			sortOrder: node.sortOrder,
		})),
	);
}
