import {Status} from '@shared/model/status.enum';

import {defineStatusMap} from './status-badge.utils';

export const ativoInativoStatusDefinition = defineStatusMap({
	[Status.ATIVO]: {
		label: 'Ativo',
		variant: 'success',
	},
	[Status.INATIVO]: {
		label: 'Inativo',
		variant: 'muted',
	},
});
