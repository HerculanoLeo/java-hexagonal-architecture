import type {ContentBreadcrumbItem} from '@components/widgets/content-layout';

const administracaoAcesso: ContentBreadcrumbItem[] = [
	{ label: 'Administração' },
	{ label: 'Acesso' },
];

export const gruposBreadcrumbs = {
	list: [...administracaoAcesso, { label: 'Grupos' }],
	create: [
		...administracaoAcesso,
		{ label: 'Grupos', to: '/administracao/acesso/grupos' },
		{ label: 'Novo' },
	],
	edit: (nome: string): ContentBreadcrumbItem[] => [
		...administracaoAcesso,
		{ label: 'Grupos', to: '/administracao/acesso/grupos' },
		{ label: nome },
	],
};
