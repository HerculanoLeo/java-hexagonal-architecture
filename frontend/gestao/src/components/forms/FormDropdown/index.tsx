import type {FormInputProps} from '@components/forms';
import {
    fieldErrors,
    FormFieldLayout,
    getFieldAriaDescribedBy,
    getOptionLabel,
    getOptionValue,
    useFormContext,
} from '@components/forms';
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue,} from '@components/ui/select';

type Props = {
	disabled?: boolean;
	onChangeValue?: (value: string) => void;
	options: Array<
		string | { label: string; value: string; description?: string }
	>;
	placeholder?: string;
} & FormInputProps;

export default function FormDropdown({
	description,
	disabled,
	label,
	loading,
	name,
	onChangeValue,
	options,
	placeholder = 'Selecione...',
	required,
}: Props) {
	const { formApi } = useFormContext();

	return (
		<formApi.Field
			name={name}
			children={(field) => {
				const errors = fieldErrors(field.state.meta);

				function onValueChange(value: string) {
					field.handleChange(value);
					onChangeValue?.(value);
				}

				return (
					<FormFieldLayout
						id={field.name}
						label={label}
						required={required}
						description={description}
						error={errors}
					>
						<Select
							value={String(field.state.value ?? '')}
							onValueChange={onValueChange}
							disabled={loading || disabled}
							name={field.name}
						>
							<SelectTrigger
								id={field.name}
								className="w-full mb-0"
								aria-label={label}
								aria-describedby={getFieldAriaDescribedBy({
									description,
									error: errors,
									id: field.name,
								})}
								aria-invalid={!field.state.meta.isValid}
								onBlur={field.handleBlur}
							>
								<SelectValue placeholder={placeholder} />
							</SelectTrigger>
							<SelectContent>
								{options.map((option) => {
									const value = getOptionValue(option);

									return (
										<SelectItem key={value} value={value}>
											{getOptionLabel(option)}
										</SelectItem>
									);
								})}
							</SelectContent>
						</Select>
					</FormFieldLayout>
				);
			}}
		/>
	);
}
