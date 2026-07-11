import {cn} from '@shared/ui/utils';

import {defaultStatusVariant, statusVariantClassNames,} from './status-badge.variants';
import type {StatusDefinition, StatusDefinitionItem,} from './status-badge.types';

export function defineStatusMap<TStatus extends string>(
	definition: StatusDefinition<TStatus>,
) {
	return definition;
}

export function resolveStatusDefinitionItem<TStatus extends string>(
	status: TStatus,
	definition: StatusDefinition<TStatus>,
): StatusDefinitionItem {
	return definition[status];
}

export function resolveStatusLabel<TStatus extends string>(
	status: TStatus,
	definition: StatusDefinition<TStatus>,
) {
	return resolveStatusDefinitionItem(status, definition).label;
}

export function resolveStatusClassName(definitionItem: StatusDefinitionItem) {
	const variant = definitionItem.variant ?? defaultStatusVariant;

	return cn(statusVariantClassNames[variant], definitionItem.className);
}

export function createStatusFormatter<TStatus extends string>(
	definition: StatusDefinition<TStatus>,
) {
	return {
		getLabel: (status: TStatus) => resolveStatusLabel(status, definition),
		getSortValue: (status: TStatus) => resolveStatusLabel(status, definition),
	};
}
