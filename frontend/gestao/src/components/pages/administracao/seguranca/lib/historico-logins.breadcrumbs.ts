import type {ContentBreadcrumbItem} from '@components/widgets/content-layout';

const administracaoSeguranca: ContentBreadcrumbItem[] = [
	{ label: 'Administração' },
	{ label: 'Segurança' },
];

export const historicoLoginsBreadcrumbs = {
	list: [...administracaoSeguranca, { label: 'Histórico de logins' }],
};
