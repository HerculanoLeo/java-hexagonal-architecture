import {Status} from '@shared/model/status.enum';

import {parseUsuarioSearchForm, usuarioSearchFormSchema, usuarioSearchRequestSchema,} from './usuario-search.schema';

describe('usuarioSearchFormSchema', () => {
	it('accepts empty filters and explicit empty status', () => {
		expect(
			usuarioSearchFormSchema.safeParse({
				nome: '',
				email: '',
				status: '',
			}).success,
		).toBe(true);
	});

	it('accepts a valid status enum value', () => {
		expect(
			usuarioSearchFormSchema.safeParse({
				nome: 'Ana',
				email: 'ana@example.com',
				status: Status.ATIVO,
			}).success,
		).toBe(true);
	});
});

describe('usuarioSearchRequestSchema', () => {
	it('accepts partial filter objects', () => {
		expect(
			usuarioSearchRequestSchema.safeParse({ nome: 'Ana' }).success,
		).toBe(true);
	});
});

describe('parseUsuarioSearchForm', () => {
	it('omits empty strings and blank status from the request', () => {
		expect(
			parseUsuarioSearchForm({
				nome: '  ',
				email: '  ',
				status: '',
			}),
		).toEqual({});
	});

	it('trims populated filters and keeps status when selected', () => {
		expect(
			parseUsuarioSearchForm({
				nome: '  Ana  ',
				email: '  ana@example.com  ',
				status: Status.INATIVO,
			}),
		).toEqual({
			nome: 'Ana',
			email: 'ana@example.com',
			status: Status.INATIVO,
		});
	});
});
