import {z} from 'zod';

export const grupoDtoSchema = z.object({
	id: z.string(),
	nome: z.string(),
	roles: z.array(z.string()).default([]),
	tipo: z.string().optional(),
});

export type GrupoDtoParsed = z.infer<typeof grupoDtoSchema>;
