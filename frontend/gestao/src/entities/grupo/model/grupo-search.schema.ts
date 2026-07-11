import {z} from 'zod';

export const grupoSearchRequestSchema = z.object({
	nome: z.string().trim().optional(),
});

export type GrupoSearchRequest = z.infer<typeof grupoSearchRequestSchema>;

export const grupoSearchFormSchema = z.object({
	nome: z.string(),
});

export type GrupoSearchFormValues = z.infer<typeof grupoSearchFormSchema>;

export function parseGrupoSearchForm(
	values: GrupoSearchFormValues,
): GrupoSearchRequest {
	const nome = values.nome.trim();

	if (nome) {
		return { nome };
	}

	return {};
}
