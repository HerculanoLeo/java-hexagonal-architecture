import {ApiHttpError} from './api-error.type';

describe('ApiHttpError', () => {
	it('stores status and payload', () => {
		const error = new ApiHttpError('Request failed', 400, {
			message: 'Dados inválidos',
			fieldErrors: [{ field: 'nome', message: 'Obrigatório' }],
		});

		expect(error.name).toBe('ApiHttpError');
		expect(error.message).toBe('Request failed');
		expect(error.status).toBe(400);
		expect(error.payload?.message).toBe('Dados inválidos');
	});
});
