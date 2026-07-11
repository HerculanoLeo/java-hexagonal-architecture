import {z} from 'zod';

const grupoRolesSchema = z.array(z.string().trim().min(1));

export const grupoUpdateSchema = z.object({
	nome: z.string().trim().min(1, 'Informe o nome.'),
	roles: grupoRolesSchema,
});

export type GrupoUpdateRequest = z.infer<typeof grupoUpdateSchema>;

export const grupoEditFormSchema = grupoUpdateSchema;

export type GrupoEditFormValues = z.infer<typeof grupoEditFormSchema>;

export function parseGrupoUpdateForm(
	values: GrupoEditFormValues,
): GrupoUpdateRequest {
	return {
		nome: values.nome.trim(),
		roles: values.roles,
	};
}
