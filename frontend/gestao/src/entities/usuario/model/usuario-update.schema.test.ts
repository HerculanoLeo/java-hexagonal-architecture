import {parseUsuarioUpdateForm, usuarioEditFormSchema, usuarioUpdateSchema,} from './usuario-update.schema';

const validForm = {
	email: 'ana@example.com',
	grupoId: 'grupo-1',
	main: true,
	nome: 'Ana Silva',
	status: 'ATIVO',
};

describe('usuarioEditFormSchema', () => {
	it('accepts valid edit form values', () => {
		expect(usuarioEditFormSchema.safeParse(validForm).success).toBe(true);
	});

	it('rejects empty name and grupo', () => {
		expect(
			usuarioEditFormSchema.safeParse({
				...validForm,
				main: false,
				nome: '   ',
				grupoId: '',
			}).success,
		).toBe(false);
	});

	it('allows empty grupo when main is true', () => {
		expect(
			usuarioEditFormSchema.safeParse({
				...validForm,
				main: true,
				grupoId: '',
			}).success,
		).toBe(true);
	});
});

describe('usuarioUpdateSchema', () => {
	it('accepts the editable request fields', () => {
		expect(
			usuarioUpdateSchema.safeParse({
				main: false,
				nome: 'Ana Silva',
				grupoId: 'grupo-1',
			}).success,
		).toBe(true);
	});
});

describe('parseUsuarioUpdateForm', () => {
	it('maps only editable fields with trimmed values', () => {
		expect(
			parseUsuarioUpdateForm({
				...validForm,
				nome: '  Ana Silva  ',
				grupoId: '  grupo-1  ',
			}),
		).toEqual({
			main: true,
			nome: 'Ana Silva',
			grupoId: 'grupo-1',
		});
	});

	it('sends blank grupoId when main has no group', () => {
		expect(
			parseUsuarioUpdateForm({
				...validForm,
				main: true,
				grupoId: '',
			}),
		).toEqual({
			main: true,
			nome: 'Ana Silva',
			grupoId: '',
		});
	});

	it('sends blank grupoId when main uses sem-grupo sentinel', () => {
		expect(
			parseUsuarioUpdateForm({
				...validForm,
				main: true,
				grupoId: '__none__',
			}),
		).toEqual({
			main: true,
			nome: 'Ana Silva',
			grupoId: '',
		});
	});
});
