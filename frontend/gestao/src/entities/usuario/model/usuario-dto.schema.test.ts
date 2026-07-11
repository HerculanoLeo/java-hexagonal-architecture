import {Status} from '@shared/model/status.enum';

import {usuarioDtoSchema} from './usuario-dto.schema';

describe('usuarioDtoSchema', () => {
	it('parses a valid usuario DTO with grupo', () => {
		expect(
			usuarioDtoSchema.parse({
				id: 'user-1',
				email: 'ana@example.com',
				nome: 'Ana Silva',
				status: Status.ATIVO,
				grupo: { id: 'grupo-1', nome: 'Administradores' },
			}),
		).toMatchObject({
			id: 'user-1',
			email: 'ana@example.com',
			nome: 'Ana Silva',
			status: Status.ATIVO,
			main: false,
		});
	});

	it('defaults main to false when omitted', () => {
		expect(
			usuarioDtoSchema.parse({
				id: 'user-1',
				email: 'ana@example.com',
				nome: 'Ana Silva',
				status: Status.INATIVO,
			}).main,
		).toBe(false);
	});
});
