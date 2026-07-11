import type {ContentBreadcrumbItem} from '@components/widgets/content-layout';

const administracaoSeguranca: ContentBreadcrumbItem[] = [
	{ label: 'Administração' },
	{ label: 'Segurança' },
];

export const usuariosOnlineBreadcrumbs = {
	list: [...administracaoSeguranca, { label: 'Usuários Online' }],
};
