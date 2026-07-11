import {grupoRegisterFormSchema, parseGrupoRegisterForm,} from './grupo-register.schema';

const validForm = {
	nome: 'Administradores',
	roles: ['usuarios-sistemas'],
};

describe('grupoRegisterFormSchema', () => {
	it('accepts valid registration data', () => {
		expect(grupoRegisterFormSchema.safeParse(validForm).success).toBe(true);
	});

	it('accepts empty roles', () => {
		expect(
			grupoRegisterFormSchema.safeParse({
				nome: 'Sem permissões',
				roles: [],
			}).success,
		).toBe(true);
	});

	it('rejects empty name', () => {
		expect(
			grupoRegisterFormSchema.safeParse({
				nome: '   ',
				roles: [],
			}).success,
		).toBe(false);
	});

	it('rejects roles with blank entries', () => {
		expect(
			grupoRegisterFormSchema.safeParse({
				nome: 'Administradores',
				roles: [' '],
			}).success,
		).toBe(false);
	});
});

describe('parseGrupoRegisterForm', () => {
	it('trims the group name', () => {
		expect(
			parseGrupoRegisterForm({
				nome: '  Administradores  ',
				roles: ['usuarios-sistemas'],
			}),
		).toEqual({
			nome: 'Administradores',
			roles: ['usuarios-sistemas'],
		});
	});
});
