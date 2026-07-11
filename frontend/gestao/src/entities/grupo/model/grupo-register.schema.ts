import {z} from 'zod';

const grupoRolesSchema = z.array(z.string().trim().min(1));

export const grupoRegisterFormSchema = z.object({
	nome: z.string().trim().min(1, 'Informe o nome.'),
	roles: grupoRolesSchema,
});

export type GrupoRegisterFormValues = z.infer<typeof grupoRegisterFormSchema>;

export const grupoRegisterSchema = grupoRegisterFormSchema;

export type GrupoRegisterRequest = z.infer<typeof grupoRegisterSchema>;

export function parseGrupoRegisterForm(
	values: GrupoRegisterFormValues,
): GrupoRegisterRequest {
	return {
		nome: values.nome.trim(),
		roles: values.roles,
	};
}
