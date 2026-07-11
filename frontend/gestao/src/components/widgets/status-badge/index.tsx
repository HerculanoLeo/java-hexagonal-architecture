import {cn} from '@shared/ui/utils';
import type {StatusDefinition} from '@shared/ui/status';
import {resolveStatusClassName, resolveStatusDefinitionItem,} from '@shared/ui/status';

type StatusBadgeProps<TStatus extends string> = {
	className?: string;
	definition: StatusDefinition<TStatus>;
	status: TStatus;
};

export function StatusBadge<TStatus extends string>({
	className,
	definition,
	status,
}: StatusBadgeProps<TStatus>) {
	const definitionItem = resolveStatusDefinitionItem(status, definition);

	return (
		<span
			className={cn(
				'inline-flex rounded-full px-2.5 py-1 text-xs font-medium',
				resolveStatusClassName(definitionItem),
				className,
			)}
		>
			{definitionItem.label}
		</span>
	);
}
