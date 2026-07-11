import type {FormInputProps, FormOption} from '@components/forms';
import {
    fieldErrors,
    FormFieldLayout,
    getFieldAriaDescribedBy,
    getOptionLabel,
    getOptionValue,
    useFormContext,
} from '@components/forms';
import {Checkbox} from '@components/ui/checkbox';
import {Label} from '@components/ui/label';
import {cn} from '@shared/ui/utils';

type Props = FormInputProps & {
	disabled?: boolean;
	options: Array<FormOption | string>;
	optionsClassName?: string;
};

export default function FormCheckboxGroup({
	description,
	disabled,
	label,
	loading,
	name,
	options,
	optionsClassName,
	required,
}: Props) {
	const { formApi } = useFormContext();

	return (
		<formApi.Field
			name={name}
			children={(field) => {
				const errors = fieldErrors(field.state.meta);
				const selectedValues = Array.isArray(field.state.value)
					? field.state.value.map(String)
					: [];

				function toggleOption(value: string) {
					const nextValue = selectedValues.includes(value)
						? selectedValues.filter((selectedValue) => selectedValue !== value)
						: [...selectedValues, value];

					field.handleChange(nextValue);
				}

				return (
					<FormFieldLayout
						id={field.name}
						label={label}
						required={required}
						description={description}
						error={errors}
					>
						<div
							role="group"
							aria-label={label}
							aria-describedby={getFieldAriaDescribedBy({
								description,
								error: errors,
								id: field.name,
							})}
							aria-invalid={!field.state.meta.isValid}
							className={cn(
								'grid grid-cols-1 gap-3 md:grid-cols-2 xl:grid-cols-3',
								optionsClassName,
							)}
						>
							{options.map((option) => {
								const value = getOptionValue(option);
								const optionLabel = getOptionLabel(option);
								const optionId = `${field.name}-${value}`;
								const isChecked = selectedValues.includes(value);

								return (
									<div
										key={value}
										className="flex items-start gap-3 rounded-md border border-border p-3"
									>
										<Checkbox
											id={optionId}
											checked={isChecked}
											disabled={loading || disabled}
											onBlur={field.handleBlur}
											onCheckedChange={() => toggleOption(value)}
											aria-label={optionLabel}
										/>
										<Label
											htmlFor={optionId}
											className="cursor-pointer font-normal leading-snug"
										>
											{optionLabel}
										</Label>
									</div>
								);
							})}
						</div>
					</FormFieldLayout>
				);
			}}
		/>
	);
}
