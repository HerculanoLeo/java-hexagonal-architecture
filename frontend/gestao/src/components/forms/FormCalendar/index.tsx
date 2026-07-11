import {CalendarIcon} from 'lucide-react';

import {Button} from '@components/ui/button';
import {Calendar} from '@components/ui/calendar';
import {Popover, PopoverContent, PopoverTrigger,} from '@components/ui/popover';
import type {FormInputProps} from '@components/forms';
import {fieldErrors, FormFieldLayout, getFieldAriaDescribedBy, useFormContext,} from '@components/forms';
import {cn} from '@shared/ui/utils';
import {formatDate} from '@shared/ui/formatters/date.formatters';

import type {ComponentProps} from 'react';

type Props = FormInputProps & {
	disabled?: boolean;
	placeholder?: string;
} & Omit<
		ComponentProps<typeof Calendar>,
		'mode' | 'selected' | 'onSelect' | 'disabled'
	>;

export default function FormCalendar({
	description,
	disabled,
	label,
	loading,
	name,
	placeholder = 'Selecione uma data',
	required,
	...calendarProps
}: Props) {
	const { formApi } = useFormContext();

	return (
		<formApi.Field
			name={name}
			children={(field) => {
				const selectedDate = toDate(field.state.value);
				const errors = fieldErrors(field.state.meta);

				return (
					<FormFieldLayout
						id={field.name}
						label={label}
						required={required}
						description={description}
						error={errors}
					>
						<Popover>
							<PopoverTrigger asChild>
								<Button
									id={field.name}
									type="button"
									variant="outline"
									className={cn(
										'w-full justify-start text-left font-normal',
										!selectedDate && 'text-muted-foreground',
									)}
									disabled={loading || disabled}
									aria-label={label}
									aria-describedby={getFieldAriaDescribedBy({
										description,
										error: errors,
										id: field.name,
									})}
									aria-invalid={!field.state.meta.isValid}
									onBlur={field.handleBlur}
								>
									<CalendarIcon className="size-4" />
									{selectedDate ? formatDate(selectedDate) : placeholder}
								</Button>
							</PopoverTrigger>
							<PopoverContent className="w-auto p-0" align="start">
								<Calendar
									mode="single"
									selected={selectedDate}
									onSelect={(date) => field.handleChange(date)}
									{...calendarProps}
								/>
							</PopoverContent>
						</Popover>
					</FormFieldLayout>
				);
			}}
		/>
	);
}

function toDate(value: unknown) {
	if (value instanceof Date) {
		return value;
	}

	if (typeof value === 'string' || typeof value === 'number') {
		const date = new Date(value);

		return Number.isNaN(date.getTime()) ? undefined : date;
	}

	return undefined;
}
