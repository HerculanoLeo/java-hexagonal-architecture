import {z} from 'zod';

export const byIdSchema = z.object({ id: z.string().min(1) });
