import {formatDate, formatDateTime} from './date.formatters';

describe('formatDate', () => {
	it('formats dates with the default pattern', () => {
		expect(formatDate(new Date('2026-03-15T10:30:00'))).toBe('15/03/2026');
	});
});

describe('formatDateTime', () => {
	it('formats Date values with the default pattern', () => {
		expect(formatDateTime(new Date('2026-03-15T10:30:00'))).toBe(
			'15/03/2026 10:30',
		);
	});

	it('formats ISO strings', () => {
		expect(formatDateTime('2026-03-15T10:30:00')).toBe('15/03/2026 10:30');
	});
});
