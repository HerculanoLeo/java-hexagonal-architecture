import type {StatusVariant} from './status-badge.types';

export const statusVariantClassNames: Record<StatusVariant, string> = {
	danger: 'bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-300',
	info: 'bg-blue-100 text-blue-700 dark:bg-blue-900/30 dark:text-blue-300',
	muted: 'bg-muted text-muted-foreground',
	success:
		'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-300',
	warning:
		'bg-amber-100 text-amber-700 dark:bg-amber-900/30 dark:text-amber-300',
};

export const defaultStatusVariant: StatusVariant = 'muted';
