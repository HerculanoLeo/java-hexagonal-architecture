import {z} from 'zod';

import {Status} from '@shared/model/status.enum';

export const usuarioGrupoDtoSchema = z.object({
	id: z.string(),
	nome: z.string(),
});

export const usuarioDtoSchema = z.object({
	id: z.string(),
	email: z.string(),
	/** Resolvido a partir de {@code tb_sistema_usuario.main} (sincronizado com admin-sistemas no Keycloak). */
	main: z.boolean().default(false),
	nome: z.string(),
	status: z.nativeEnum(Status),
	grupo: usuarioGrupoDtoSchema.optional(),
});

export type UsuarioDtoParsed = z.infer<typeof usuarioDtoSchema>;
