import {z} from 'zod';

export const historicoLoginSearchRequestSchema = z.object({
	dataEventoDe: z.string().optional(),
	dataEventoAte: z.string().optional(),
	email: z.string().trim().optional(),
	tipo: z.string().optional(),
	relacionadoId: z.string().trim().optional(),
	usuarioId: z.string().uuid().optional(),
});

export type HistoricoLoginSearchRequest = z.infer<
	typeof historicoLoginSearchRequestSchema
>;

export const historicoLoginSearchFormSchema = z.object({
	dataEventoDe: z.union([z.date(), z.undefined()]).optional(),
	dataEventoAte: z.union([z.date(), z.undefined()]).optional(),
	email: z.string(),
	tipo: z.string(),
	relacionadoId: z.string(),
});

export type HistoricoLoginSearchFormValues = z.infer<
	typeof historicoLoginSearchFormSchema
>;

function toStartOfDayIso(date: Date) {
	const value = new Date(date);
	value.setHours(0, 0, 0, 0);
	return value.toISOString();
}

function toEndOfDayIso(date: Date) {
	const value = new Date(date);
	value.setHours(23, 59, 59, 999);
	return value.toISOString();
}

export function parseHistoricoLoginSearchForm(
	values: HistoricoLoginSearchFormValues,
): HistoricoLoginSearchRequest {
	const filters: HistoricoLoginSearchRequest = {};
	const email = values.email.trim();
	const relacionadoId = values.relacionadoId.trim();
	const tipo = values.tipo.trim();

	if (values.dataEventoDe instanceof Date) {
		filters.dataEventoDe = toStartOfDayIso(values.dataEventoDe);
	}

	if (values.dataEventoAte instanceof Date) {
		filters.dataEventoAte = toEndOfDayIso(values.dataEventoAte);
	}

	if (email) {
		filters.email = email;
	}

	if (tipo) {
		filters.tipo = tipo;
	}

	if (relacionadoId) {
		filters.relacionadoId = relacionadoId;
	}

	return filters;
}
