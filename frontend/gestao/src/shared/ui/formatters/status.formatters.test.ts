import {Status} from '@shared/model/status.enum';

import {getStatusLabel} from './status.formatters';

describe('getStatusLabel', () => {
	it('returns labels from the shared status definition', () => {
		expect(getStatusLabel(Status.ATIVO)).toBe('Ativo');
		expect(getStatusLabel(Status.INATIVO)).toBe('Inativo');
	});
});
