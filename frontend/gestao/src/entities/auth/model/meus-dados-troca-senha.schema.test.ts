import {parseTrocarSenhaForm, trocarSenhaFormSchema, trocarSenhaRequestSchema,} from './meus-dados-troca-senha.schema';

const validPasswordForm = {
	senhaAtual: 'atual-123',
	novaSenha: 'nova-456',
	confirmacaoSenha: 'nova-456',
};

describe('trocarSenhaFormSchema', () => {
	it('accepts matching password and confirmation', () => {
		expect(trocarSenhaFormSchema.safeParse(validPasswordForm).success).toBe(true);
	});

	it('rejects empty required fields', () => {
		const result = trocarSenhaFormSchema.safeParse({
			senhaAtual: '',
			novaSenha: '',
			confirmacaoSenha: '',
		});

		expect(result.success).toBe(false);
	});

	it('rejects mismatched password confirmation on confirmacaoSenha path', () => {
		const result = trocarSenhaFormSchema.safeParse({
			...validPasswordForm,
			confirmacaoSenha: 'outra-senha',
		});

		expect(result.success).toBe(false);

		if (!result.success) {
			expect(
				result.error.issues.some((issue) => issue.path[0] === 'confirmacaoSenha'),
			).toBe(true);
		}
	});
});

describe('trocarSenhaRequestSchema', () => {
	it('applies the same confirmation rule as the form schema', () => {
		const result = trocarSenhaRequestSchema.safeParse({
			...validPasswordForm,
			confirmacaoSenha: 'diferente',
		});

		expect(result.success).toBe(false);
	});
});

describe('parseTrocarSenhaForm', () => {
	it('trims all password fields', () => {
		expect(
			parseTrocarSenhaForm({
				senhaAtual: '  atual  ',
				novaSenha: '  nova  ',
				confirmacaoSenha: '  nova  ',
			}),
		).toEqual({
			senhaAtual: 'atual',
			novaSenha: 'nova',
			confirmacaoSenha: 'nova',
		});
	});
});
