import {createServerFn} from '@tanstack/react-start';

import type UsuarioAutenticadoDto from '../model/usuario-autenticado.dto';
import type GrupoDto from '@entities/grupo/model/grupo.dto';
import {grupoDtoSchema} from '@entities/grupo/model/grupo-dto.schema';
import type {MeusDadosUpdateRequest} from '../model/meus-dados-update.schema';
import {meusDadosUpdateSchema} from '../model/meus-dados-update.schema';
import type {TrocarSenhaRequest} from '../model/meus-dados-troca-senha.schema';
import {trocarSenhaRequestSchema} from '../model/meus-dados-troca-senha.schema';

export const AUTH_QUERY_KEY = 'auth';

const endpoint = '/auth';

const findAuthenticatedUser = createServerFn({ method: 'GET' }).handler(
	async (): Promise<UsuarioAutenticadoDto> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');

		return fetchCadastros<UsuarioAutenticadoDto>(`${endpoint}/me`);
	},
);

const findAuthenticatedGroup = createServerFn({ method: 'GET' }).handler(
	async (): Promise<GrupoDto | null> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');
		const { ApiHttpError } = await import('@shared/api/api-error.type.ts');

		try {
			const response = await fetchCadastros<GrupoDto>(`${endpoint}/grupo`);

			if (!response) {
				return null;
			}

			return grupoDtoSchema.parse(response);
		} catch (error) {
			if (
				error instanceof ApiHttpError &&
				(error.status === 404 || error.status === 204)
			) {
				return null;
			}

			throw error;
		}
	},
);

const updateAuthenticatedUser = createServerFn({ method: 'POST' })
	.validator(meusDadosUpdateSchema)
	.handler(async ({ data }): Promise<UsuarioAutenticadoDto> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');

		return fetchCadastros<UsuarioAutenticadoDto>(`${endpoint}/me`, {
			body: data,
			method: 'PUT',
		});
	});

const changeAuthenticatedUserPassword = createServerFn({ method: 'POST' })
	.validator(trocarSenhaRequestSchema)
	.handler(async ({ data }): Promise<void> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');

		await fetchCadastros<void>(`${endpoint}/me/senha`, {
			body: data,
			method: 'PUT',
		});
	});

class AuthService {
	async me(): Promise<UsuarioAutenticadoDto> {
		return findAuthenticatedUser();
	}

	async findGrupo(): Promise<GrupoDto | null> {
		return findAuthenticatedGroup();
	}

	async updateMe(data: MeusDadosUpdateRequest): Promise<UsuarioAutenticadoDto> {
		return updateAuthenticatedUser({
			data,
		});
	}

	async changePassword(data: TrocarSenhaRequest): Promise<void> {
		await changeAuthenticatedUserPassword({
			data,
		});
	}
}

export default new AuthService();
