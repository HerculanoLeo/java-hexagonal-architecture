import {z} from 'zod';

export const revokeAllSessionsSchema = z.object({
	userId: z.string().min(1),
});

export type RevokeAllSessionsRequest = z.infer<typeof revokeAllSessionsSchema>;
