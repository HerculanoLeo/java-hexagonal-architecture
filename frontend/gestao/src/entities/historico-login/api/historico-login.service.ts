import {createServerFn} from '@tanstack/react-start';

import type {HistoricoLoginDto} from '../model/historico-login.dto';
import {historicoLoginDtoSchema} from '../model/historico-login.dto';
import type {HistoricoLoginSearchRequest} from '../model/historico-login-search.schema';
import {historicoLoginSearchRequestSchema} from '../model/historico-login-search.schema';
import {z} from 'zod';

export const HISTORICO_LOGIN_QUERY_KEY = 'historico-login';

const endpoint = '/seguranca/historico-logins';

const findHistoricoLogins = createServerFn({ method: 'GET' })
	.validator(historicoLoginSearchRequestSchema)
	.handler(async ({ data }): Promise<HistoricoLoginDto[]> => {
		const { fetchCadastros } =
			await import('@shared/api/cadastros/cadastros.server.ts');

		const response = await fetchCadastros<HistoricoLoginDto[]>(endpoint, {
			params: data,
		});

		return z.array(historicoLoginDtoSchema).parse(response);
	});

class HistoricoLoginService {
	async findAll(
		filters: HistoricoLoginSearchRequest = {},
	): Promise<HistoricoLoginDto[]> {
		return findHistoricoLogins({
			data: filters,
		});
	}
}

export default new HistoricoLoginService();
