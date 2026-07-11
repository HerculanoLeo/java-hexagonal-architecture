import {grupoDtoSchema} from './grupo-dto.schema';

describe('grupoDtoSchema', () => {
	it('parses a valid grupo DTO', () => {
		expect(
			grupoDtoSchema.parse({
				id: 'grupo-1',
				nome: 'Administradores',
				roles: ['usuarios-sistemas'],
				tipo: 'SISTEMA',
			}),
		).toEqual({
			id: 'grupo-1',
			nome: 'Administradores',
			roles: ['usuarios-sistemas'],
			tipo: 'SISTEMA',
		});
	});

	it('defaults roles to an empty array', () => {
		expect(
			grupoDtoSchema.parse({
				id: 'grupo-1',
				nome: 'Administradores',
			}).roles,
		).toEqual([]);
	});
});
