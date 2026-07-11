import {format} from 'date-fns';

export function formatDate(value: Date, pattern = 'dd/MM/yyyy') {
	return format(value, pattern);
}

export function formatDateTime(
	value: Date | string,
	pattern = 'dd/MM/yyyy HH:mm',
) {
	const date = typeof value === 'string' ? new Date(value) : value;
	return format(date, pattern);
}
