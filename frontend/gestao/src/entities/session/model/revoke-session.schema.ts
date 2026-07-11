import {z} from 'zod';

export const revokeSessionSchema = z.object({
	sessionId: z.string().min(1),
});

export type RevokeSessionRequest = z.infer<typeof revokeSessionSchema>;
