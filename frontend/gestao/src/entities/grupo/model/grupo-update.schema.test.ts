import {grupoEditFormSchema, parseGrupoUpdateForm,} from './grupo-update.schema';

const validForm = {
	nome: 'Operadores',
	roles: ['grupos-sistemas'],
};

describe('grupoEditFormSchema', () => {
	it('accepts valid update data', () => {
		expect(grupoEditFormSchema.safeParse(validForm).success).toBe(true);
	});

	it('accepts empty roles', () => {
		expect(
			grupoEditFormSchema.safeParse({
				nome: 'Operadores',
				roles: [],
			}).success,
		).toBe(true);
	});
});

describe('parseGrupoUpdateForm', () => {
	it('trims the group name', () => {
		expect(
			parseGrupoUpdateForm({
				nome: '  Operadores  ',
				roles: ['grupos-sistemas'],
			}),
		).toEqual({
			nome: 'Operadores',
			roles: ['grupos-sistemas'],
		});
	});
});
