import type {FormInputProps} from '@components/forms';
import {
    fieldErrors,
    FormFieldErrors,
    getFieldAriaDescribedBy,
    getFieldDescriptionId,
    getFieldErrorId,
    useFormContext,
} from '@components/forms';
import {Checkbox} from '@components/ui/checkbox';
import {Label} from '@components/ui/label';

import type {ComponentProps} from 'react';

type Props = FormInputProps &
	Omit<
		ComponentProps<typeof Checkbox>,
		'id' | 'name' | 'checked' | 'onBlur' | 'onCheckedChange'
	>;

export default function FormCheckbox({
	description,
	label,
	loading,
	name,
	...checkboxProps
}: Props) {
	const { formApi } = useFormContext();

	return (
		<formApi.Field
			name={name}
			children={(field) => {
				const errors = fieldErrors(field.state.meta);

				return (
					<div className="space-y-2">
						<div className="flex items-start gap-3">
							<Checkbox
								id={field.name}
								name={field.name}
								checked={Boolean(field.state.value)}
								onBlur={field.handleBlur}
								onCheckedChange={(checked) => {
									field.handleChange(checked === true);
								}}
								aria-label={label}
								aria-describedby={getFieldAriaDescribedBy({
									description,
									error: errors,
									id: field.name,
								})}
								aria-invalid={!field.state.meta.isValid}
								disabled={loading || checkboxProps.disabled}
								{...checkboxProps}
							/>
							<div className="grid gap-1.5 leading-none">
								<Label htmlFor={field.name}>{label}</Label>
								{description && (
									<p
										id={getFieldDescriptionId(field.name)}
										className="text-sm text-muted-foreground"
									>
										{description}
									</p>
								)}
							</div>
						</div>
						<FormFieldErrors id={getFieldErrorId(field.name)} errors={errors} />
					</div>
				);
			}}
		/>
	);
}
