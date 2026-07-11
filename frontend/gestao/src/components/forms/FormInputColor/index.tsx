import type {FormInputProps} from '@components/forms';
import {fieldErrors, FormFieldLayout, getFieldAriaDescribedBy, useFormContext,} from '@components/forms';
import {Input} from '@components/ui/input';
import {cn} from '@shared/ui/utils';

type FormInputColorProps = FormInputProps;

function normalizeHexColor(value: string) {
	const trimmed = value.trim();

	if (/^#[0-9A-Fa-f]{6}$/.test(trimmed)) {
		return trimmed;
	}

	if (/^[0-9A-Fa-f]{6}$/.test(trimmed)) {
		return `#${trimmed}`;
	}

	return trimmed;
}

export default function FormInputColor({
	description,
	label,
	loading,
	name,
	required,
}: FormInputColorProps) {
	const { formApi } = useFormContext();

	return (
		<formApi.Field
			name={name}
			children={(field) => {
				const errors = fieldErrors(field.state.meta);
				const value = normalizeHexColor(String(field.state.value ?? '#000000'));
				const pickerValue = /^#[0-9A-Fa-f]{6}$/.test(value) ? value : '#000000';

				return (
					<FormFieldLayout
						id={field.name}
						label={label}
						required={required}
						description={description}
						error={errors}
					>
						<div className="flex items-center gap-3">
							<Input
								id={`${field.name}-picker`}
								type="color"
								value={pickerValue}
								onChange={(event) => field.handleChange(event.target.value)}
								onBlur={field.handleBlur}
								disabled={loading}
								className="h-10 w-14 shrink-0 cursor-pointer p-1"
								aria-label={`${label} (seletor de cor)`}
							/>
							<Input
								id={field.name}
								name={field.name}
								value={value}
								onChange={(event) =>
									field.handleChange(normalizeHexColor(event.target.value))
								}
								onBlur={field.handleBlur}
								disabled={loading}
								placeholder="#RRGGBB"
								aria-label={label}
								aria-describedby={getFieldAriaDescribedBy({
									description,
									error: errors,
									id: field.name,
								})}
								aria-invalid={!field.state.meta.isValid}
								className={cn('font-mono', errors && 'border-destructive')}
							/>
						</div>
					</FormFieldLayout>
				);
			}}
		/>
	);
}
