import type {ExternalToast} from 'sonner';
import {toast} from 'sonner';

type AppToastOptions = Pick<
	ExternalToast,
	'action' | 'description' | 'duration' | 'id'
>;

type AppToastInput =
	| string
	| ({
			title: string;
	  } & AppToastOptions);

export const appToast = {
	error: (input: AppToastInput, options?: AppToastOptions) =>
		toast.error(getToastTitle(input), getToastOptions(input, options)),
	info: (input: AppToastInput, options?: AppToastOptions) =>
		toast.info(getToastTitle(input), getToastOptions(input, options)),
	message: (input: AppToastInput, options?: AppToastOptions) =>
		toast(getToastTitle(input), getToastOptions(input, options)),
	success: (input: AppToastInput, options?: AppToastOptions) =>
		toast.success(getToastTitle(input), getToastOptions(input, options)),
	warning: (input: AppToastInput, options?: AppToastOptions) =>
		toast.warning(getToastTitle(input), getToastOptions(input, options)),

	// Aliases em português para manter chamadas expressivas nas features.
	atencao: (input: AppToastInput, options?: AppToastOptions) =>
		appToast.warning(input, options),
	falha: (input: AppToastInput, options?: AppToastOptions) =>
		appToast.error(input, options),
	informacao: (input: AppToastInput, options?: AppToastOptions) =>
		appToast.info(input, options),
	sucesso: (input: AppToastInput, options?: AppToastOptions) =>
		appToast.success(input, options),
};

function getToastTitle(input: AppToastInput) {
	return typeof input === 'string' ? input : input.title;
}

function getToastOptions(
	input: AppToastInput,
	options?: AppToastOptions,
): AppToastOptions | undefined {
	if (typeof input === 'string') {
		return options;
	}

	return {
		action: input.action,
		description: input.description,
		duration: input.duration,
		id: input.id,
		...options,
	};
}
