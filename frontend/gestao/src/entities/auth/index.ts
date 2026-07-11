export type { default as UsuarioAutenticadoDto } from './model/usuario-autenticado.dto';
export type { default as MeusDadosProfileDto } from './model/meus-dados-profile.dto';

export {
	meusDadosFormSchema,
	meusDadosUpdateSchema,
	parseMeusDadosForm,
} from './model/meus-dados-update.schema';
export type {
	MeusDadosFormValues,
	MeusDadosUpdateRequest,
} from './model/meus-dados-update.schema';

export {
	parseTrocarSenhaForm,
	trocarSenhaDefaultValues,
	trocarSenhaFormSchema,
	trocarSenhaRequestSchema,
} from './model/meus-dados-troca-senha.schema';
export type { TrocarSenhaRequest } from './model/meus-dados-troca-senha.schema';

export { default as authService, AUTH_QUERY_KEY } from './api/auth.service';
export {
	fetchMeusDadosProfile,
	getMeusDadosQueryKey,
	invalidateMeusDadosProfile,
	refetchMeusDadosProfile,
} from './api/auth.queries';
