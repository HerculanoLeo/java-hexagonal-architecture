import {useForm} from '@tanstack/react-form';
import {Save} from 'lucide-react';

import Form from '@components/forms';
import {formActionButtonClassName, FormActions, formActionSaveButtonClassName,} from '@components/forms/FormActions';
import FormButtonSubmit from '@components/forms/FormButtonSubmit';
import FormInputText from '@components/forms/FormInputText';
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from '@components/ui/card';
import type {MeusDadosFormValues, MeusDadosProfileDto, MeusDadosUpdateRequest,} from '@entities/auth';
import {meusDadosFormSchema, parseMeusDadosForm} from '@entities/auth';
import {getStatusLabel} from '@shared/ui/formatters/status.formatters';
import {parseServerErrorResponse} from '@shared/forms/server-error-response';
import {cn} from '@shared/ui/utils';

type MeusDadosFormProps = {
	isSubmitting?: boolean;
	onSubmit: (data: MeusDadosUpdateRequest) => Promise<unknown> | unknown;
	profile: MeusDadosProfileDto;
};

function getMeusDadosDefaultValues(
	profile: MeusDadosProfileDto,
): MeusDadosFormValues {
	return {
		email: profile.email,
		grupoNome: profile.grupo?.nome ?? 'Sem grupo',
		nome: profile.nome,
		status: getStatusLabel(profile.status),
	};
}

export function MeusDadosForm({
	isSubmitting,
	onSubmit,
	profile,
}: MeusDadosFormProps) {
	const form = useForm({
		defaultValues: getMeusDadosDefaultValues(profile),
		validators: {
			onSubmit: meusDadosFormSchema as any,
		},
		onSubmit: async ({ value, formApi }) => {
			try {
				await onSubmit(parseMeusDadosForm(value));
			} catch (error) {
				parseServerErrorResponse(error, {
					formApi,
				});
			}
		},
	});

	return (
		<Card>
			<CardHeader>
				<CardTitle>Meus Dados</CardTitle>
				<CardDescription>
					Atualize suas informações cadastrais de acesso ao painel.
				</CardDescription>
			</CardHeader>
			<CardContent>
				<Form form={form} className="space-y-6">
					<div className="grid gap-4 md:grid-cols-2">
						<FormInputText name="nome" label="Nome" required />
						<FormInputText name="email" label="E-mail" type="email" disabled />
						<FormInputText name="grupoNome" label="Grupo" disabled />
						<FormInputText name="status" label="Status" disabled />
					</div>

					<FormActions>
						<FormButtonSubmit
							className={cn(
								formActionButtonClassName,
								formActionSaveButtonClassName,
							)}
							loading={isSubmitting}
						>
							<Save className="size-4" />
							Salvar
						</FormButtonSubmit>
					</FormActions>
				</Form>
			</CardContent>
		</Card>
	);
}
