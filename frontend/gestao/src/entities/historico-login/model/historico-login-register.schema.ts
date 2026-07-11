import {z} from 'zod';

export const historicoLoginRegisterSchema = z.object({
	ip: z.string().nullable().optional(),
	userAgent: z.string().nullable().optional(),
	sessaoBffId: z.string().nullable().optional(),
	email: z.string().nullable().optional(),
	nome: z.string().nullable().optional(),
});

export type HistoricoLoginRegisterRequest = z.infer<
	typeof historicoLoginRegisterSchema
>;
