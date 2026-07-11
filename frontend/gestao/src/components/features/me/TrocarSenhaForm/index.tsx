import {useForm} from '@tanstack/react-form';
import {KeyRound} from 'lucide-react';

import Form from '@components/forms';
import {formActionButtonClassName, FormActions, formActionSaveButtonClassName,} from '@components/forms/FormActions';
import FormButtonSubmit from '@components/forms/FormButtonSubmit';
import FormPassword from '@components/forms/FormPassword';
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from '@components/ui/card';
import type {TrocarSenhaRequest} from '@entities/auth';
import {parseTrocarSenhaForm, trocarSenhaDefaultValues, trocarSenhaFormSchema,} from '@entities/auth';
import {parseServerErrorResponse} from '@shared/forms/server-error-response';
import {cn} from '@shared/ui/utils';

type TrocarSenhaFormProps = {
	isSubmitting?: boolean;
	onSubmit: (data: TrocarSenhaRequest) => Promise<unknown> | unknown;
};

export function TrocarSenhaForm({
	isSubmitting,
	onSubmit,
}: TrocarSenhaFormProps) {
	const form = useForm({
		defaultValues: trocarSenhaDefaultValues,
		validators: {
			onSubmit: trocarSenhaFormSchema as any,
		},
		onSubmit: async ({ value, formApi }) => {
			try {
				await onSubmit(parseTrocarSenhaForm(value));
				formApi.reset();
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
				<CardTitle>Trocar Senha</CardTitle>
				<CardDescription>
					Informe a senha atual e defina uma nova senha de acesso.
				</CardDescription>
			</CardHeader>
			<CardContent>
				<Form form={form} className="space-y-6">
					<div className="grid gap-4 md:grid-cols-2">
						<FormPassword
							name="senhaAtual"
							label="Senha atual"
							required
							autoComplete="current-password"
						/>
					</div>
					<div className="grid gap-4 md:grid-cols-2">
						<FormPassword
							name="novaSenha"
							label="Nova senha"
							required
							autoComplete="new-password"
						/>
						<FormPassword
							name="confirmacaoSenha"
							label="Confirmar nova senha"
							required
							autoComplete="new-password"
						/>
					</div>

					<FormActions>
						<FormButtonSubmit
							className={cn(
								formActionButtonClassName,
								formActionSaveButtonClassName,
							)}
							loading={isSubmitting}
						>
							<KeyRound className="size-4" />
							Trocar senha
						</FormButtonSubmit>
					</FormActions>
				</Form>
			</CardContent>
		</Card>
	);
}
