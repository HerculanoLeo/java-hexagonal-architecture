import {useForm} from '@tanstack/react-form';
import {ArrowLeft, Ban, CheckCircle2, KeyRound, Save} from 'lucide-react';

import Form from '@components/forms';
import {
    formActionActivateButtonClassName,
    formActionBackButtonClassName,
    formActionButtonClassName,
    FormActions,
    formActionSaveButtonClassName,
    FormActionsGroup,
} from '@components/forms/FormActions';
import FormButtonSubmit from '@components/forms/FormButtonSubmit';
import FormCheckbox from '@components/forms/FormCheckbox';
import FormDropdown from '@components/forms/FormDropdown';
import FormInputText from '@components/forms/FormInputText';
import FormPassword from '@components/forms/FormPassword';
import {Button} from '@components/ui/button';
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from '@components/ui/card';
import type {GrupoDto} from '@entities/grupo';
import {Status} from '@shared/model/status.enum';
import type {
    UsuarioDto,
    UsuarioEditFormValues,
    UsuarioRegisterFormValues,
    UsuarioRegisterRequest,
    UsuarioUpdateRequest,
} from '@entities/usuario';
import {
    parseUsuarioRegisterForm,
    parseUsuarioUpdateForm,
    resolveUsuarioGrupoFormValue,
    USUARIO_SEM_GRUPO_VALUE,
    usuarioEditFormSchema,
    usuarioRegisterFormSchema,
} from '@entities/usuario';
import {getStatusLabel} from '@shared/ui/formatters/status.formatters';
import {parseServerErrorResponse} from '@shared/forms/server-error-response';
import {cn} from '@shared/ui/utils';

type UsuarioFormBaseProps = {
	authenticatedUserId: string;
	grupos: GrupoDto[];
	isSubmitting?: boolean;
	onBack: () => void;
};

type UsuarioCreateFormProps = UsuarioFormBaseProps & {
	mode: 'create';
	onSubmit: (data: UsuarioRegisterRequest) => Promise<unknown> | unknown;
};

type UsuarioEditFormProps = UsuarioFormBaseProps & {
	mode: 'edit';
	onAtivar?: () => void;
	onInativar?: () => void;
	onResetPassword?: () => void;
	onSubmit: (data: UsuarioUpdateRequest) => Promise<unknown> | unknown;
	statusActionLoading?: boolean;
	usuario: UsuarioDto;
};

export type UsuarioFormProps = UsuarioCreateFormProps | UsuarioEditFormProps;

const usuarioRegisterDefaultValues: UsuarioRegisterFormValues = {
	email: '',
	grupoId: '',
	main: false,
	nome: '',
	senha: '',
};

function getUsuarioEditDefaultValues(
	usuario: UsuarioDto,
): UsuarioEditFormValues {
	return {
		email: usuario.email,
		grupoId: resolveUsuarioGrupoFormValue(usuario.grupo?.id, usuario.main ?? false),
		main: usuario.main ?? false,
		nome: usuario.nome,
		status: getStatusLabel(usuario.status),
	};
}

function getGrupoOptions(grupos: GrupoDto[], main: boolean) {
	const options = grupos.map((grupo) => ({
		label: grupo.nome,
		value: grupo.id,
	}));

	if (!main) {
		return options;
	}

	return [
		{
			label: 'Sem grupo',
			value: USUARIO_SEM_GRUPO_VALUE,
		},
		...options,
	];
}

function UsuarioGrupoField({
	grupos,
	main,
}: {
	grupos: GrupoDto[];
	main: boolean;
}) {
	return (
		<FormDropdown
			name="grupoId"
			label="Grupo"
			options={getGrupoOptions(grupos, main)}
			placeholder={
				main ? 'Opcional para usuário principal' : 'Selecione um grupo'
			}
			description={
				main
					? 'Opcional para usuário principal, que tem acesso total à plataforma.'
					: undefined
			}
			required={!main}
		/>
	);
}

function UsuarioCreateForm({
	authenticatedUserId: _authenticatedUserId,
	grupos,
	isSubmitting,
	onBack,
	onSubmit,
}: UsuarioCreateFormProps) {
	const form = useForm({
		defaultValues: usuarioRegisterDefaultValues,
		validators: {
			onSubmit: usuarioRegisterFormSchema as any,
		},
		onSubmit: async ({ value, formApi }) => {
			try {
				await onSubmit(parseUsuarioRegisterForm(value));
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
				<CardTitle>Novo Usuário</CardTitle>
				<CardDescription>
					Cadastre um usuário para acesso ao painel administrativo. Se a senha
					não for informada, será enviado um e-mail para definir o primeiro
					acesso.
				</CardDescription>
			</CardHeader>
			<CardContent>
				<Form form={form} className="space-y-6">
					<div className="grid gap-4 md:grid-cols-2">
						<FormInputText name="nome" label="Nome" required />
						<FormInputText name="email" label="E-mail" type="email" required />
						<FormPassword
							name="senha"
							label="Senha"
							description="Opcional. Se vazio, será enviado um e-mail para o usuário definir a senha."
						/>
						<form.Subscribe
							selector={(state) => state.values.main}
							children={(main) => (
								<UsuarioGrupoField grupos={grupos} main={main} />
							)}
						/>
					</div>

					<FormCheckbox
						name="main"
						label="Usuário principal"
						description="Indica se este é o usuário principal do sistema."
					/>

					<UsuarioFormActions isSubmitting={isSubmitting} onBack={onBack} />
				</Form>
			</CardContent>
		</Card>
	);
}

function UsuarioEditForm({
	authenticatedUserId,
	grupos,
	isSubmitting,
	onAtivar,
	onBack,
	onInativar,
	onResetPassword,
	onSubmit,
	statusActionLoading,
	usuario,
}: UsuarioEditFormProps) {
	const form = useForm({
		defaultValues: getUsuarioEditDefaultValues(usuario),
		validators: {
			onSubmit: usuarioEditFormSchema as any,
		},
		onSubmit: async ({ value, formApi }) => {
			try {
				await onSubmit(parseUsuarioUpdateForm(value));
			} catch (error) {
				parseServerErrorResponse(error, {
					formApi,
				});
			}
		},
	});

	const canToggleStatus = usuario.id !== authenticatedUserId;

	return (
		<Card>
			<CardHeader>
				<CardTitle>Editar Usuário</CardTitle>
				<CardDescription>
					Atualize os dados cadastrais e o status do usuário.
				</CardDescription>
			</CardHeader>
			<CardContent>
				<Form form={form} className="space-y-6">
					<div className="grid gap-4 md:grid-cols-2">
						<FormInputText name="nome" label="Nome" required />
						<FormInputText name="email" label="E-mail" type="email" disabled />
						<form.Subscribe
							selector={(state) => state.values.main}
							children={(main) => (
								<UsuarioGrupoField grupos={grupos} main={main} />
							)}
						/>
						<FormInputText name="status" label="Status" disabled />
					</div>

					<FormCheckbox
						name="main"
						label="Usuário principal"
						description="Indica se este é o usuário tem acesso total a plataforma."
					/>

					<FormActions>
						<Button
							type="button"
							className={cn(
								formActionButtonClassName,
								formActionBackButtonClassName,
							)}
							onClick={onBack}
						>
							<ArrowLeft className="size-4" />
							Voltar
						</Button>

						<FormActionsGroup>
							{canToggleStatus && (
								<Button
									type="button"
									className={cn(formActionButtonClassName)}
									onClick={onResetPassword}
									disabled={statusActionLoading}
									variant="outline"
								>
									<KeyRound className="size-4" />
									Redefinir senha
								</Button>
							)}
							{canToggleStatus && usuario.status === Status.ATIVO && (
								<Button
									type="button"
									className={cn(
										formActionButtonClassName,
										'bg-orange-500 text-white hover:bg-orange-600',
									)}
									onClick={onInativar}
									disabled={statusActionLoading}
								>
									<Ban className="size-4" />
									Inativar
								</Button>
							)}
							{canToggleStatus && usuario.status === Status.INATIVO && (
								<Button
									type="button"
									className={cn(
										formActionButtonClassName,
										formActionActivateButtonClassName,
									)}
									onClick={onAtivar}
									disabled={statusActionLoading}
								>
									<CheckCircle2 className="size-4" />
									Ativar
								</Button>
							)}
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
						</FormActionsGroup>
					</FormActions>
				</Form>
			</CardContent>
		</Card>
	);
}

function UsuarioFormActions({
	isSubmitting,
	onBack,
}: {
	isSubmitting?: boolean;
	onBack: () => void;
}) {
	return (
		<FormActions>
			<Button
				type="button"
				className={cn(formActionButtonClassName, formActionBackButtonClassName)}
				onClick={onBack}
			>
				<ArrowLeft className="size-4" />
				Voltar
			</Button>
			<FormButtonSubmit
				className={cn(formActionButtonClassName, formActionSaveButtonClassName)}
				loading={isSubmitting}
			>
				<Save className="size-4" />
				Salvar
			</FormButtonSubmit>
		</FormActions>
	);
}

export default function UsuarioForm(props: UsuarioFormProps) {
	if (props.mode === 'create') {
		return <UsuarioCreateForm {...props} />;
	}

	return (
		<UsuarioEditForm
			key={`${props.usuario.id}-${props.usuario.status}`}
			{...props}
		/>
	);
}
