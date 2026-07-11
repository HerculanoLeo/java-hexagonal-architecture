import {z} from 'zod';

import {Status} from '@shared/model/status.enum';

export const usuarioSearchRequestSchema = z.object({
	nome: z.string().trim().optional(),
	email: z.string().trim().optional(),
	status: z.enum(Status).optional(),
});

export type UsuarioSearchRequest = z.infer<typeof usuarioSearchRequestSchema>;

export const usuarioSearchFormSchema = z.object({
	nome: z.string(),
	email: z.string(),
	status: z.union([z.literal(''), z.enum(Status)]),
});

export type UsuarioSearchFormValues = z.infer<typeof usuarioSearchFormSchema>;

export function parseUsuarioSearchForm(
	values: UsuarioSearchFormValues,
): UsuarioSearchRequest {
	const filters: UsuarioSearchRequest = {};
	const nome = values.nome.trim();
	const email = values.email.trim();

	if (nome) {
		filters.nome = nome;
	}

	if (email) {
		filters.email = email;
	}

	if (values.status !== '') {
		filters.status = values.status;
	}

	return filters;
}
