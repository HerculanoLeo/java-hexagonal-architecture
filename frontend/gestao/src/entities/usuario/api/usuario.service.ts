import {createServerFn} from '@tanstack/react-start';
import {z} from 'zod';

import type UsuarioDto from '../model/usuario.dto';
import type UsuarioGrupoDto from '../model/usuario-grupo.dto';
import {usuarioDtoSchema, usuarioGrupoDtoSchema,} from '../model/usuario-dto.schema';
import type {UsuarioRegisterRequest} from '../model/usuario-register.schema';
import {usuarioRegisterSchema} from '../model/usuario-register.schema';
import type {UsuarioSearchRequest} from '../model/usuario-search.schema';
import {usuarioSearchRequestSchema} from '../model/usuario-search.schema';
import type {UsuarioUpdateRequest} from '../model/usuario-update.schema';
import {usuarioUpdateSchema} from '../model/usuario-update.schema';
import {byIdSchema} from '@shared/model/by-id.schema';

export const USUARIOS_QUERY_KEY = 'usuarios';

const endpoint = '/sistema/usuario';

const updateSchema = z.object({
	...byIdSchema.shape,
	data: usuarioUpdateSchema,
});

const findUsuarios = createServerFn({ method: 'GET' })
	.validator(usuarioSearchRequestSchema)
	.handler(async ({ data }): Promise<UsuarioDto[]> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');

		const response = await fetchCadastros<UsuarioDto[]>(endpoint, {
			params: data,
		});

		return z.array(usuarioDtoSchema).parse(response);
	});

const findUsuarioById = createServerFn({ method: 'GET' })
	.validator(byIdSchema)
	.handler(async ({ data }): Promise<UsuarioDto> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');

		const response = await fetchCadastros<UsuarioDto>(`${endpoint}/${data.id}`);

		return usuarioDtoSchema.parse(response);
	});

const findUsuarioGrupoById = createServerFn({ method: 'GET' })
	.validator(byIdSchema)
	.handler(async ({ data }): Promise<UsuarioGrupoDto | null> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');
		const { ApiHttpError } = await import('@shared/api/api-error.type.ts');

		try {
			const response = await fetchCadastros<UsuarioGrupoDto>(
				`${endpoint}/${data.id}/grupo`,
			);

			return usuarioGrupoDtoSchema.parse(response);
		} catch (error) {
			if (error instanceof ApiHttpError && error.status === 404) {
				return null;
			}

			throw error;
		}
	});

const registerUsuario = createServerFn({ method: 'POST' })
	.validator(usuarioRegisterSchema)
	.handler(async ({ data }): Promise<UsuarioDto> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');

		const response = await fetchCadastros<UsuarioDto>(endpoint, {
			body: data,
			method: 'POST',
		});

		return usuarioDtoSchema.parse(response);
	});

const updateUsuario = createServerFn({ method: 'POST' })
	.validator(updateSchema)
	.handler(async ({ data }): Promise<void> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');

		await fetchCadastros<void>(`${endpoint}/${data.id}`, {
			body: data.data,
			method: 'PUT',
		});
	});

const ativarUsuario = createServerFn({ method: 'POST' })
	.validator(byIdSchema)
	.handler(async ({ data }): Promise<void> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');

		await fetchCadastros<void>(`${endpoint}/${data.id}/ativar`, {
			method: 'PUT',
		});
	});

const inativarUsuario = createServerFn({ method: 'POST' })
	.validator(byIdSchema)
	.handler(async ({ data }): Promise<void> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');

		await fetchCadastros<void>(`${endpoint}/${data.id}/inativar`, {
			method: 'DELETE',
		});
	});

const resetPasswordUsuario = createServerFn({ method: 'POST' })
	.validator(byIdSchema)
	.handler(async ({ data }): Promise<void> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');

		await fetchCadastros<void>(`${endpoint}/${data.id}/reset-password`, {
			method: 'PUT',
		});
	});

class UsuariosService {
	async findAll(filters: UsuarioSearchRequest = {}): Promise<UsuarioDto[]> {
		return findUsuarios({
			data: filters,
		});
	}

	async findById(id: string): Promise<UsuarioDto> {
		return findUsuarioById({
			data: { id },
		});
	}

	async findGrupoById(id: string): Promise<UsuarioGrupoDto | null> {
		return findUsuarioGrupoById({
			data: { id },
		});
	}

	async register(data: UsuarioRegisterRequest): Promise<UsuarioDto> {
		return registerUsuario({
			data,
		});
	}

	async update(id: string, data: UsuarioUpdateRequest): Promise<void> {
		await updateUsuario({
			data: {
				data,
				id,
			},
		});
	}

	async ativar(id: string): Promise<void> {
		await ativarUsuario({
			data: { id },
		});
	}

	async inativar(id: string): Promise<void> {
		await inativarUsuario({
			data: { id },
		});
	}

	async resetPassword(id: string): Promise<void> {
		await resetPasswordUsuario({
			data: { id },
		});
	}
}

export default new UsuariosService();
