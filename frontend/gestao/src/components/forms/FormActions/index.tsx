import type {PropsWithChildren} from 'react';

import {cn} from '@shared/ui/utils';

export const formActionButtonClassName = 'w-full sm:w-auto';

export const formActionSaveButtonClassName = 'btn-action-save';
export const formActionActivateButtonClassName = 'btn-action-activate';
export const formActionBackButtonClassName = 'btn-action-back';
export const formActionDeleteButtonClassName = 'btn-action-delete';

type FormActionsProps = PropsWithChildren<{
	className?: string;
}>;

export function FormActions({ children, className }: FormActionsProps) {
	return (
		<div
			className={cn(
				'flex w-full flex-col gap-3 border-t pt-6 sm:flex-row sm:items-center sm:justify-end',
				className,
			)}
		>
			{children}
		</div>
	);
}

export function FormActionsGroup({ children, className }: FormActionsProps) {
	return (
		<div
			className={cn(
				'flex w-full flex-col gap-3 sm:w-auto sm:flex-row sm:items-center',
				className,
			)}
		>
			{children}
		</div>
	);
}
