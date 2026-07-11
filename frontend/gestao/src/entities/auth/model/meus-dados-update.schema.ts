import {z} from 'zod';

export const meusDadosFormSchema = z.object({
	email: z.string(),
	grupoNome: z.string(),
	nome: z.string().trim().min(1, 'Informe o nome.'),
	status: z.string(),
});

export type MeusDadosFormValues = z.infer<typeof meusDadosFormSchema>;

export const meusDadosUpdateSchema = z.object({
	nome: z.string().trim().min(1, 'Informe o nome.'),
});

export type MeusDadosUpdateRequest = z.infer<typeof meusDadosUpdateSchema>;

export function parseMeusDadosForm(
	values: MeusDadosFormValues,
): MeusDadosUpdateRequest {
	return {
		nome: values.nome.trim(),
	};
}
