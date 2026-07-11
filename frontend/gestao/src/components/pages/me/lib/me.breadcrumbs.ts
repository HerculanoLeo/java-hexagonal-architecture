import type {ContentBreadcrumbItem} from '@components/widgets/content-layout';

const inicio: ContentBreadcrumbItem = { label: 'Início', to: '/' };

const meuPerfil: ContentBreadcrumbItem = {
	label: 'Meu Perfil',
	to: '/me',
};

export const meuPerfilBreadcrumbs: ContentBreadcrumbItem[] = [
	inicio,
	{ label: 'Meu Perfil' },
];

export const meusDadosBreadcrumbs: ContentBreadcrumbItem[] = [
	inicio,
	meuPerfil,
	{ label: 'Meus Dados' },
];

export const trocarSenhaBreadcrumbs: ContentBreadcrumbItem[] = [
	inicio,
	meuPerfil,
	{ label: 'Trocar Senha' },
];
