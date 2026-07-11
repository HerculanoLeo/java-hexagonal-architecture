import type {ContentBreadcrumbItem} from '@components/widgets/content-layout';

const administracaoAcesso: ContentBreadcrumbItem[] = [
	{ label: 'Administração' },
	{ label: 'Acesso' },
];

export const usuariosBreadcrumbs = {
	list: [...administracaoAcesso, { label: 'Usuários' }],
	create: [
		...administracaoAcesso,
		{ label: 'Usuários', to: '/administracao/acesso/usuarios' },
		{ label: 'Novo' },
	],
	edit: (nome: string): ContentBreadcrumbItem[] => [
		...administracaoAcesso,
		{ label: 'Usuários', to: '/administracao/acesso/usuarios' },
		{ label: nome },
	],
};
