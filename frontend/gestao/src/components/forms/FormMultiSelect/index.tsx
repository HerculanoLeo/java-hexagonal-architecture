import {Check, ChevronsUpDown, X} from 'lucide-react';

import {Badge} from '@components/ui/badge';
import {Button} from '@components/ui/button';
import {Command, CommandEmpty, CommandGroup, CommandInput, CommandItem, CommandList,} from '@components/ui/command';
import {Popover, PopoverContent, PopoverTrigger,} from '@components/ui/popover';
import type {FormInputProps, FormOption} from '@components/forms';
import {
    fieldErrors,
    FormFieldLayout,
    getFieldAriaDescribedBy,
    getOptionLabel,
    getOptionValue,
    useFormContext,
} from '@components/forms';
import {cn} from '@shared/ui/utils';

type Props = FormInputProps & {
	disabled?: boolean;
	onChangeValue?: (value: string[]) => void;
	options: Array<FormOption | string>;
	placeholder?: string;
};

export default function FormMultiSelect({
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
				const selectedValues = Array.isArray(field.state.value)
					? field.state.value.map(String)
					: [];
				const selectedOptions = options.filter((option) =>
					selectedValues.includes(getOptionValue(option)),
				);

				function handleChange(nextValue: string[]) {
					field.handleChange(nextValue);
					onChangeValue?.(nextValue);
				}

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
									className="min-h-9 w-full justify-between"
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
									<span className="flex min-w-0 flex-1 flex-wrap gap-1">
										{selectedOptions.length > 0 ? (
											selectedOptions.map((option) => {
												const value = getOptionValue(option);

												return (
													<Badge key={value} variant="secondary">
														{getOptionLabel(option)}
														<span
															role="button"
															tabIndex={0}
															className="ml-1 rounded-full outline-none hover:text-destructive"
															onClick={(event) => {
																event.preventDefault();
																event.stopPropagation();
																handleChange(
																	selectedValues.filter(
																		(selectedValue) => selectedValue !== value,
																	),
																);
															}}
															onKeyDown={(event) => {
																if (
																	event.key === 'Enter' ||
																	event.key === ' '
																) {
																	event.preventDefault();
																	handleChange(
																		selectedValues.filter(
																			(selectedValue) =>
																				selectedValue !== value,
																		),
																	);
																}
															}}
														>
															<X className="size-3" />
														</span>
													</Badge>
												);
											})
										) : (
											<span className="text-muted-foreground">
												{placeholder}
											</span>
										)}
									</span>
									<ChevronsUpDown className="size-4 shrink-0 opacity-50" />
								</Button>
							</PopoverTrigger>
							<PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
								<Command>
									<CommandInput placeholder="Buscar..." />
									<CommandList>
										<CommandEmpty>Nenhuma opção encontrada.</CommandEmpty>
										<CommandGroup>
											{options.map((option) => {
												const value = getOptionValue(option);
												const optionLabel = getOptionLabel(option);
												const isSelected = selectedValues.includes(value);

												return (
													<CommandItem
														key={value}
														value={`${optionLabel} ${value}`}
														onSelect={() => {
															handleChange(
																isSelected
																	? selectedValues.filter(
																			(selectedValue) =>
																				selectedValue !== value,
																		)
																	: [...selectedValues, value],
															);
														}}
													>
														<Check
															className={cn(
																'size-4',
																isSelected ? 'opacity-100' : 'opacity-0',
															)}
														/>
														<div className="grid gap-0.5">
															<span>{getOptionLabel(option)}</span>
															{typeof option !== 'string' &&
																option.description && (
																	<span className="text-xs text-muted-foreground">
																		{option.description}
																	</span>
																)}
														</div>
													</CommandItem>
												);
											})}
										</CommandGroup>
									</CommandList>
								</Command>
							</PopoverContent>
						</Popover>
					</FormFieldLayout>
				);
			}}
		/>
	);
}
