import {createServerFn} from '@tanstack/react-start';
import {z} from 'zod';

import type GrupoDto from '../model/grupo.dto';
import {grupoDtoSchema} from '../model/grupo-dto.schema';
import type {GrupoRegisterRequest} from '../model/grupo-register.schema';
import {grupoRegisterSchema} from '../model/grupo-register.schema';
import type {GrupoSearchRequest} from '../model/grupo-search.schema';
import {grupoSearchRequestSchema} from '../model/grupo-search.schema';
import type {GrupoUpdateRequest} from '../model/grupo-update.schema';
import {grupoUpdateSchema} from '../model/grupo-update.schema';
import {byIdSchema} from '@shared/model/by-id.schema';

export const GRUPOS_QUERY_KEY = 'grupos';

const endpoint = '/sistema/grupo';

const updateSchema = z.object({
	...byIdSchema.shape,
	data: grupoUpdateSchema,
});

const findGrupos = createServerFn({ method: 'GET' })
	.validator(grupoSearchRequestSchema)
	.handler(async ({ data }): Promise<GrupoDto[]> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');

		const response = await fetchCadastros<GrupoDto[]>(endpoint, {
			params: data,
		});

		return z.array(grupoDtoSchema).parse(response);
	});

const findGrupoById = createServerFn({ method: 'GET' })
	.validator(byIdSchema)
	.handler(async ({ data }): Promise<GrupoDto> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');

		const response = await fetchCadastros<GrupoDto>(`${endpoint}/${data.id}`);

		return grupoDtoSchema.parse(response);
	});

const registerGrupo = createServerFn({ method: 'POST' })
	.validator(grupoRegisterSchema)
	.handler(async ({ data }): Promise<GrupoDto> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');

		const response = await fetchCadastros<GrupoDto>(endpoint, {
			body: data,
			method: 'POST',
		});

		return grupoDtoSchema.parse(response);
	});

const updateGrupo = createServerFn({ method: 'POST' })
	.validator(updateSchema)
	.handler(async ({ data }): Promise<void> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');

		await fetchCadastros<void>(`${endpoint}/${data.id}`, {
			body: data.data,
			method: 'PUT',
		});
	});

const deleteGrupo = createServerFn({ method: 'POST' })
	.validator(byIdSchema)
	.handler(async ({ data }): Promise<void> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');

		await fetchCadastros<void>(`${endpoint}/${data.id}`, {
			method: 'DELETE',
		});
	});

class GrupoService {
	async findAll(filters: GrupoSearchRequest = {}): Promise<GrupoDto[]> {
		return findGrupos({
			data: filters,
		});
	}

	async findGrupos(): Promise<GrupoDto[]> {
		return this.findAll();
	}

	async findById(id: string): Promise<GrupoDto> {
		return findGrupoById({
			data: { id },
		});
	}

	async register(data: GrupoRegisterRequest): Promise<GrupoDto> {
		return registerGrupo({
			data,
		});
	}

	async update(id: string, data: GrupoUpdateRequest): Promise<void> {
		await updateGrupo({
			data: {
				data,
				id,
			},
		});
	}

	async delete(id: string): Promise<void> {
		await deleteGrupo({
			data: { id },
		});
	}
}

export default new GrupoService();
