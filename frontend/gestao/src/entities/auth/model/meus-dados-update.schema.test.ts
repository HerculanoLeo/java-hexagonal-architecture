import {meusDadosFormSchema, meusDadosUpdateSchema, parseMeusDadosForm,} from './meus-dados-update.schema';

describe('meusDadosFormSchema', () => {
	it('requires a non-empty trimmed name', () => {
		expect(
			meusDadosFormSchema.safeParse({
				nome: '  Ana Silva  ',
				email: 'ana@example.com',
				grupoNome: 'Administradores',
				status: 'ATIVO',
			}).success,
		).toBe(true);

		expect(
			meusDadosFormSchema.safeParse({
				nome: '   ',
				email: 'ana@example.com',
				grupoNome: 'Administradores',
				status: 'ATIVO',
			}).success,
		).toBe(false);
	});
});

describe('meusDadosUpdateSchema', () => {
	it('accepts only the editable name field', () => {
		expect(meusDadosUpdateSchema.safeParse({ nome: 'Ana Silva' }).success).toBe(
			true,
		);
	});
});

describe('parseMeusDadosForm', () => {
	it('maps the form to the update request with a trimmed name', () => {
		expect(
			parseMeusDadosForm({
				nome: '  Ana Silva  ',
				email: 'ana@example.com',
				grupoNome: 'Administradores',
				status: 'ATIVO',
			}),
		).toEqual({ nome: 'Ana Silva' });
	});
});
