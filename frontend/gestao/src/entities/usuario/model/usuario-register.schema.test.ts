import {parseUsuarioRegisterForm, usuarioRegisterFormSchema, usuarioRegisterSchema,} from './usuario-register.schema';

const validRegisterForm = {
	main: false,
	nome: 'Ana Silva',
	email: 'ana@example.com',
	grupoId: 'grupo-1',
	senha: 'senha-123',
};

describe('usuarioRegisterFormSchema', () => {
	it('accepts valid registration data', () => {
		expect(usuarioRegisterFormSchema.safeParse(validRegisterForm).success).toBe(
			true,
		);
	});

	it('rejects invalid email addresses', () => {
		expect(
			usuarioRegisterFormSchema.safeParse({
				...validRegisterForm,
				email: 'email-invalido',
			}).success,
		).toBe(false);
	});

	it('rejects empty required fields', () => {
		expect(
			usuarioRegisterFormSchema.safeParse({
				...validRegisterForm,
				nome: '   ',
				grupoId: '',
			}).success,
		).toBe(false);
	});

	it('allows empty grupo when main is true', () => {
		expect(
			usuarioRegisterFormSchema.safeParse({
				...validRegisterForm,
				main: true,
				grupoId: '',
			}).success,
		).toBe(true);
	});
});

describe('usuarioRegisterSchema', () => {
	it('allows optional password on the request schema', () => {
		expect(
			usuarioRegisterSchema.safeParse({
				main: true,
				nome: 'Ana Silva',
				email: 'ana@example.com',
				grupoId: 'grupo-1',
			}).success,
		).toBe(true);
	});

	it('allows main user without grupoId', () => {
		expect(
			usuarioRegisterSchema.safeParse({
				main: true,
				nome: 'Ana Silva',
				email: 'ana@example.com',
			}).success,
		).toBe(true);
	});
});

describe('parseUsuarioRegisterForm', () => {
	it('trims fields and lowercases email', () => {
		expect(
			parseUsuarioRegisterForm({
				main: true,
				nome: '  Ana Silva  ',
				email: '  ANA@EXAMPLE.COM  ',
				grupoId: '  grupo-1  ',
				senha: '  senha-123  ',
			}),
		).toEqual({
			main: true,
			nome: 'Ana Silva',
			email: 'ana@example.com',
			grupoId: 'grupo-1',
			senha: 'senha-123',
		});
	});

	it('omits senha when the form field is blank', () => {
		expect(
			parseUsuarioRegisterForm({
				...validRegisterForm,
				senha: '   ',
			}),
		).toEqual({
			main: false,
			nome: 'Ana Silva',
			email: 'ana@example.com',
			grupoId: 'grupo-1',
		});
	});

	it('omits grupoId when main and grupo is blank', () => {
		expect(
			parseUsuarioRegisterForm({
				...validRegisterForm,
				main: true,
				grupoId: '   ',
				senha: '',
			}),
		).toEqual({
			main: true,
			nome: 'Ana Silva',
			email: 'ana@example.com',
		});
	});

	it('omits grupoId when main uses sem-grupo sentinel', () => {
		expect(
			parseUsuarioRegisterForm({
				...validRegisterForm,
				main: true,
				grupoId: '__none__',
				senha: '',
			}),
		).toEqual({
			main: true,
			nome: 'Ana Silva',
			email: 'ana@example.com',
		});
	});
});
