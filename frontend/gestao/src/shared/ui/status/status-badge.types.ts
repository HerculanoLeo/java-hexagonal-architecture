export type StatusVariant = 'success' | 'warning' | 'danger' | 'info' | 'muted';

export type StatusDefinitionItem = {
	className?: string;
	label: string;
	variant?: StatusVariant;
};

export type StatusDefinition<TStatus extends string = string> = Record<
	TStatus,
	StatusDefinitionItem
>;
