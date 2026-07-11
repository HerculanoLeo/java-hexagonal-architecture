import {Status} from '@shared/model/status.enum';

import {ativoInativoStatusDefinition} from './status.definitions';
import {
    createStatusFormatter,
    defineStatusMap,
    resolveStatusClassName,
    resolveStatusDefinitionItem,
    resolveStatusLabel,
} from './status-badge.utils';

describe('defineStatusMap', () => {
	it('returns the same status definition object', () => {
		const definition = defineStatusMap({
			[Status.ATIVO]: { label: 'Ativo', variant: 'success' },
		});

		expect(definition[Status.ATIVO].label).toBe('Ativo');
	});
});

describe('resolveStatusDefinitionItem', () => {
	it('returns the item for a known status', () => {
		expect(
			resolveStatusDefinitionItem(Status.ATIVO, ativoInativoStatusDefinition).label,
		).toBe('Ativo');
	});
});

describe('resolveStatusLabel', () => {
	it('returns the configured label', () => {
		expect(resolveStatusLabel(Status.INATIVO, ativoInativoStatusDefinition)).toBe(
			'Inativo',
		);
	});
});

describe('resolveStatusClassName', () => {
	it('uses the variant class names and custom className', () => {
		const className = resolveStatusClassName({
			label: 'Ativo',
			variant: 'success',
			className: 'font-semibold',
		});

		expect(className).toContain('bg-green-100');
		expect(className).toContain('font-semibold');
	});

	it('falls back to the muted variant', () => {
		const className = resolveStatusClassName({ label: 'Desconhecido' });

		expect(className).toContain('bg-muted');
	});
});

describe('createStatusFormatter', () => {
	it('exposes label helpers for sorting and display', () => {
		const formatter = createStatusFormatter(ativoInativoStatusDefinition);

		expect(formatter.getLabel(Status.ATIVO)).toBe('Ativo');
		expect(formatter.getSortValue(Status.ATIVO)).toBe('Ativo');
	});
});
