import {z} from 'zod';

const trocarSenhaBaseSchema = z.object({
	confirmacaoSenha: z.string().trim().min(1, 'Confirme a nova senha.'),
	novaSenha: z.string().trim().min(1, 'Informe a nova senha.'),
	senhaAtual: z.string().trim().min(1, 'Informe a senha atual.'),
});

export const trocarSenhaFormSchema = trocarSenhaBaseSchema.refine(
	(data) => data.novaSenha === data.confirmacaoSenha,
	{
		message: 'Senha e confirmação não são iguais.',
		path: ['confirmacaoSenha'],
	},
);

export type TrocarSenhaFormValues = z.infer<typeof trocarSenhaBaseSchema>;

export const trocarSenhaRequestSchema = trocarSenhaBaseSchema.refine(
	(data) => data.novaSenha === data.confirmacaoSenha,
	{
		message: 'Senha e confirmação não são iguais.',
		path: ['confirmacaoSenha'],
	},
);

export type TrocarSenhaRequest = z.infer<typeof trocarSenhaBaseSchema>;

export function parseTrocarSenhaForm(
	values: TrocarSenhaFormValues,
): TrocarSenhaRequest {
	return {
		confirmacaoSenha: values.confirmacaoSenha.trim(),
		novaSenha: values.novaSenha.trim(),
		senhaAtual: values.senhaAtual.trim(),
	};
}

export const trocarSenhaDefaultValues: TrocarSenhaFormValues = {
	confirmacaoSenha: '',
	novaSenha: '',
	senhaAtual: '',
};
