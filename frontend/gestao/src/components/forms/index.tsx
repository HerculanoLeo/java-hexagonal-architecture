import type {ComponentProps, PropsWithChildren} from 'react';
import {createContext, useContext} from 'react';
import type {AnyFieldMeta, FormAsyncValidateOrFn, FormValidateOrFn, ReactFormExtendedApi,} from '@tanstack/react-form';

import {Label} from '@components/ui/label';
import {cn} from '@shared/ui/utils';

type AnyFormData = Record<string, unknown>;
type AnyFormValidate = undefined | FormValidateOrFn<AnyFormData>;
type AnyFormAsyncValidate = undefined | FormAsyncValidateOrFn<AnyFormData>;

export type AnyReactFormExtendedApi = ReactFormExtendedApi<
	AnyFormData,
	AnyFormValidate,
	AnyFormValidate,
	AnyFormAsyncValidate,
	AnyFormValidate,
	AnyFormAsyncValidate,
	AnyFormValidate,
	AnyFormAsyncValidate,
	AnyFormValidate,
	AnyFormAsyncValidate,
	AnyFormAsyncValidate,
	unknown
>;

export type FormInputProps = {
	name: string;
	label: string;
	required?: boolean;
	description?: string;
	loading?: boolean;
};

export type FormOption<TValue extends string = string> = {
	label: string;
	value: TValue;
	description?: string;
};

export function fieldErrors({
	isValid,
	errors,
}: AnyFieldMeta): Array<string> | undefined {
	if (!isValid) {
		const messages = errors.flatMap(toErrorMessages).filter(Boolean);

		return messages.length > 0 ? messages : undefined;
	}
	return undefined;
}

function toErrorMessages(error: unknown): string[] {
	if (Array.isArray(error)) {
		return error.flatMap(toErrorMessages);
	}

	if (error instanceof Error) {
		return [error.message];
	}

	if (typeof error === 'object' && error !== null && 'message' in error) {
		return [String(error.message)];
	}

	if (typeof error === 'string') {
		return [error];
	}

	if (error === undefined || error === null) {
		return [];
	}

	return [String(error)];
}

export function FormFieldLayout({
	children,
	className,
	description,
	error,
	id,
	label,
	required,
}: PropsWithChildren<{
	className?: string;
	description?: string;
	error?: string[];
	id: string;
	label: string;
	required?: boolean;
}>) {
	const descriptionId = description ? getFieldDescriptionId(id) : undefined;
	const errorId = error?.length ? getFieldErrorId(id) : undefined;

	return (
		<div className={cn('space-y-2', className)}>
			<Label htmlFor={id}>
				{label}
				{required && <span className="ml-1 text-destructive">*</span>}
			</Label>
			{children}
			{description && (
				<p id={descriptionId} className="text-sm text-muted-foreground">
					{description}
				</p>
			)}
			<FormFieldErrors id={errorId} errors={error} />
		</div>
	);
}

export function FormFieldErrors({
	errors,
	id,
}: {
	errors?: string[];
	id?: string;
}) {
	if (!errors?.length) {
		return null;
	}

	return (
		<div id={id} className="space-y-1">
			{errors.map((error) => (
				<p key={error} className="text-sm font-medium text-destructive">
					{error}
				</p>
			))}
		</div>
	);
}

export function getOptionLabel(option: FormOption | string) {
	return typeof option === 'string' ? option : option.label;
}

export function getOptionValue(option: FormOption | string) {
	return typeof option === 'string' ? option : option.value;
}

export function getFieldDescriptionId(fieldId: string) {
	return `${fieldId}-description`;
}

export function getFieldErrorId(fieldId: string) {
	return `${fieldId}-error`;
}

export function getFieldAriaDescribedBy({
	description,
	error,
	id,
}: {
	description?: string;
	error?: string[];
	id: string;
}) {
	const describedBy = cn(
		description && getFieldDescriptionId(id),
		error?.length && getFieldErrorId(id),
	);

	return describedBy || undefined;
}

const Context = createContext<{ formApi: AnyReactFormExtendedApi } | null>(
	null,
);

export function useFormContext() {
	const context = useContext(Context);

	if (!context) {
		throw new Error('useFormContext must be used inside <Form>.');
	}

	return context;
}

interface FormProps {
	form: {
		handleSubmit: () => Promise<void> | void;
	};
	className?: string;
	onSubmit?: ComponentProps<'form'>['onSubmit'];
}

type Props = FormProps & PropsWithChildren;

export default function Form({ form, children, className, onSubmit }: Props) {
	return (
		<Context.Provider
			value={{ formApi: form as unknown as AnyReactFormExtendedApi }}
		>
			<form
				onSubmit={async (e) => {
					onSubmit?.(e);

					if (e.defaultPrevented) {
						return;
					}

					e.preventDefault();
					e.stopPropagation();
					await form.handleSubmit();
				}}
				className={cn(className)}
			>
				{children}
			</form>
		</Context.Provider>
	);
}
