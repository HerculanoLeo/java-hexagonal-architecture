import {Input} from '@components/ui/input';
import type {FormInputProps} from '@components/forms';
import {fieldErrors, FormFieldLayout, getFieldAriaDescribedBy, useFormContext,} from '@components/forms';

import type {ComponentProps} from 'react';

type Props = FormInputProps &
	Omit<
		ComponentProps<typeof Input>,
		'name' | 'type' | 'value' | 'onBlur' | 'onChange'
	>;

export default function FormInputNumber({
	description,
	label,
	loading,
	name,
	required,
	...inputProps
}: Props) {
	const { formApi } = useFormContext();

	return (
		<formApi.Field
			name={name}
			children={(field) => {
				const errors = fieldErrors(field.state.meta);

				return (
					<FormFieldLayout
						id={field.name}
						label={label}
						required={required}
						description={description}
						error={errors}
					>
						<Input
							id={field.name}
							name={field.name}
							type="number"
							value={
								field.state.value === undefined || field.state.value === null
									? ''
									: String(field.state.value)
							}
							onBlur={field.handleBlur}
							onChange={(event) => {
								const value = event.target.value;
								field.handleChange(value === '' ? undefined : Number(value));
							}}
							aria-label={label}
							aria-describedby={getFieldAriaDescribedBy({
								description,
								error: errors,
								id: field.name,
							})}
							aria-invalid={!field.state.meta.isValid}
							disabled={loading || inputProps.disabled}
							{...inputProps}
						/>
					</FormFieldLayout>
				);
			}}
		/>
	);
}
