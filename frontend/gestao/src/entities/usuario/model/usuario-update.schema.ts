import {z} from 'zod';

import {normalizeUsuarioGrupoId} from '../lib/usuario-grupo.utils';

const grupoIdWhenNotMain = (main: boolean, grupoId: string) =>
	main || normalizeUsuarioGrupoId(grupoId).length > 0;

export const usuarioUpdateSchema = z
	.object({
		main: z.boolean().default(false),
		nome: z.string().trim().min(1, 'Informe o nome.'),
		grupoId: z.string().optional(),
	})
	.superRefine((data, ctx) => {
		if (!grupoIdWhenNotMain(data.main, data.grupoId ?? '')) {
			ctx.addIssue({
				code: 'custom',
				message: 'Selecione um grupo.',
				path: ['grupoId'],
			});
		}
	});

export type UsuarioUpdateRequest = z.infer<typeof usuarioUpdateSchema>;

export const usuarioEditFormSchema = z
	.object({
		email: z.string(),
		grupoId: z.string(),
		main: z.boolean().default(false),
		nome: z.string().trim().min(1, 'Informe o nome.'),
		status: z.string(),
	})
	.superRefine((data, ctx) => {
		if (!grupoIdWhenNotMain(data.main, data.grupoId)) {
			ctx.addIssue({
				code: 'custom',
				message: 'Selecione um grupo.',
				path: ['grupoId'],
			});
		}
	});

export type UsuarioEditFormValues = z.infer<typeof usuarioEditFormSchema>;

export function parseUsuarioUpdateForm(
	values: UsuarioEditFormValues,
): UsuarioUpdateRequest {
	const grupoId = normalizeUsuarioGrupoId(values.grupoId);
	const request: UsuarioUpdateRequest = {
		main: values.main,
		nome: values.nome.trim(),
	};

	if (grupoId) {
		request.grupoId = grupoId;
	} else if (values.main) {
		// Blank string: backend remove grupos do Keycloak.
		request.grupoId = '';
	}

	return request;
}
