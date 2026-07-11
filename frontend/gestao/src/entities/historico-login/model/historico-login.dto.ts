import {z} from 'zod';

export const historicoLoginDtoSchema = z.object({
	id: z.string().uuid(),
	identityId: z.string(),
	usuarioId: z.string().uuid().nullable().optional(),
	tipo: z.string(),
	relacionadoId: z.string().nullable().optional(),
	email: z.string().nullable().optional(),
	nome: z.string().nullable().optional(),
	ip: z.string().nullable().optional(),
	userAgent: z.string().nullable().optional(),
	sucesso: z.boolean(),
	dataEvento: z.string(),
	sessaoBffId: z.string().nullable().optional(),
});

export type HistoricoLoginDto = z.infer<typeof historicoLoginDtoSchema>;
