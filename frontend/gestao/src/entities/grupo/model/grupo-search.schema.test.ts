import {grupoSearchFormSchema, parseGrupoSearchForm,} from './grupo-search.schema';

describe('grupoSearchFormSchema', () => {
	it('accepts an empty search form', () => {
		expect(grupoSearchFormSchema.safeParse({ nome: '' }).success).toBe(true);
	});
});

describe('parseGrupoSearchForm', () => {
	it('returns an empty object when nome is blank', () => {
		expect(parseGrupoSearchForm({ nome: '   ' })).toEqual({});
	});

	it('trims and includes nome when provided', () => {
		expect(parseGrupoSearchForm({ nome: '  Admin  ' })).toEqual({
			nome: 'Admin',
		});
	});
});
