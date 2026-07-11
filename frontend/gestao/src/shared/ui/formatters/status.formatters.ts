import type {Status} from '@shared/model/status.enum';
import {ativoInativoStatusDefinition, createStatusFormatter,} from '@shared/ui/status';

const ativoInativoStatusFormatter = createStatusFormatter(
	ativoInativoStatusDefinition,
);

export function getStatusLabel(status: Status) {
	return ativoInativoStatusFormatter.getLabel(status);
}
