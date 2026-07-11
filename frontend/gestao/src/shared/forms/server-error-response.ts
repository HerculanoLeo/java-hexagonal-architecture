import {ApiHttpError} from '@shared/api/api-error.type.ts';
import {appToast} from '@shared/ui/toast';

import type {AnyFieldLikeMetaBase} from '@tanstack/react-form';

type FieldErrorMap = Record<string, string>;

type FormApiWithFieldMeta<TField extends string> = {
	setFieldMeta: (
		field: TField,
		updater: (meta: AnyFieldLikeMetaBase) => AnyFieldLikeMetaBase,
	) => void;
};

type ParseServerErrorResponseOptions<TField extends string> = {
	fieldMap?: Record<string, string>;
	formApi?: FormApiWithFieldMeta<TField>;
};

export function parseServerErrorResponse<TField extends string>(
	error: unknown,
	options: ParseServerErrorResponseOptions<TField> = {},
) {
	const fieldErrors = getFieldErrorMap(error, options.fieldMap);

	if (Object.keys(fieldErrors).length > 0 && options.formApi) {
		applyFieldErrors(options.formApi, fieldErrors);
		appToast.atencao('Há dados incorretos no formulário.');
		return;
	}

	const message = getServerErrorMessage(error);

	if (isValidationError(error)) {
		appToast.atencao(message);
		return;
	}

	appToast.falha({
		title: 'Não foi possível concluir a operação',
		description: message,
	});
}

export function getServerErrorMessage(error: unknown) {
	if (error instanceof ApiHttpError) {
		return error.payload?.message ?? error.message;
	}

	if (error instanceof Error) {
		return error.message;
	}

	return 'Não foi possível concluir a operação. Tente novamente.';
}

function applyFieldErrors<TField extends string>(
	formApi: FormApiWithFieldMeta<TField>,
	fieldErrors: FieldErrorMap,
) {
	for (const [field, message] of Object.entries(fieldErrors)) {
		formApi.setFieldMeta(field as TField, (meta) => ({
			...meta,
			errorMap: {
				...meta.errorMap,
				onServer: message,
			},
			errorSourceMap: {
				...meta.errorSourceMap,
				onServer: 'form',
			},
		}));
	}
}

function getFieldErrorMap(
	error: unknown,
	fieldMap: Record<string, string> = {},
) {
	const fieldErrors: FieldErrorMap = {};
	const payload = getErrorPayload(error);

	if (!payload) {
		return fieldErrors;
	}

	for (const fieldError of payload.fieldErrors ?? []) {
		fieldErrors[fieldMap[fieldError.field] ?? fieldError.field] =
			fieldError.message;
	}

	for (const fieldError of payload.errors ?? []) {
		fieldErrors[fieldMap[fieldError.fieldName] ?? fieldError.fieldName] =
			fieldError.messages.join('\n');
	}

	return fieldErrors;
}

function getErrorPayload(error: unknown) {
	if (error instanceof ApiHttpError) {
		return error.payload;
	}

	return undefined;
}

function isValidationError(error: unknown) {
	return error instanceof ApiHttpError && error.status === 400;
}
