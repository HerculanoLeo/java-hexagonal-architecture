import {z} from 'zod';

import {normalizeUsuarioGrupoId} from '../lib/usuario-grupo.utils';

const grupoIdWhenNotMain = (main: boolean, grupoId: string) =>
	main || normalizeUsuarioGrupoId(grupoId).length > 0;

export const usuarioRegisterFormSchema = z
	.object({
		main: z.boolean().default(false),
		nome: z.string().trim().min(1, 'Informe o nome.'),
		email: z.email('Informe um email valido.').toLowerCase(),
		grupoId: z.string(),
		senha: z.string(),
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

export type UsuarioRegisterFormValues = z.infer<
	typeof usuarioRegisterFormSchema
>;

export const usuarioRegisterSchema = z
	.object({
		main: z.boolean().default(false),
		nome: z.string().trim().min(1, 'Informe o nome.'),
		email: z.email('Informe um email valido.').toLowerCase(),
		grupoId: z.string().optional(),
		senha: z.string().trim().min(1).optional(),
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

export type UsuarioRegisterRequest = z.infer<typeof usuarioRegisterSchema>;

export function parseUsuarioRegisterForm(
	values: UsuarioRegisterFormValues,
): UsuarioRegisterRequest {
	const senha = values.senha.trim();
	const grupoId = normalizeUsuarioGrupoId(values.grupoId);
	const request: UsuarioRegisterRequest = {
		email: values.email.trim().toLowerCase(),
		main: values.main,
		nome: values.nome.trim(),
	};

	if (grupoId) {
		request.grupoId = grupoId;
	}

	if (senha) {
		request.senha = senha;
	}

	return request;
}
