import {useForm} from '@tanstack/react-form';
import {ArrowLeft, Save, Trash2} from 'lucide-react';
import {useState} from 'react';

import Form from '@components/forms';
import {
    formActionBackButtonClassName,
    formActionButtonClassName,
    formActionDeleteButtonClassName,
    FormActions,
    formActionSaveButtonClassName,
    FormActionsGroup,
} from '@components/forms/FormActions';
import FormButtonSubmit from '@components/forms/FormButtonSubmit';
import FormCheckboxGroup from '@components/forms/FormCheckboxGroup';
import FormInputText from '@components/forms/FormInputText';
import {Button} from '@components/ui/button';
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from '@components/ui/card';
import {
    Dialog,
    DialogClose,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from '@components/ui/dialog';
import type {RoleDto} from '@entities/role';
import {filterAssignableSistemaRoles} from '@entities/role';
import type {
    GrupoDto,
    GrupoEditFormValues,
    GrupoRegisterFormValues,
    GrupoRegisterRequest,
    GrupoUpdateRequest,
} from '@entities/grupo';
import {
    grupoEditFormSchema,
    grupoRegisterFormSchema,
    parseGrupoRegisterForm,
    parseGrupoUpdateForm,
} from '@entities/grupo';
import {parseServerErrorResponse} from '@shared/forms/server-error-response';
import {cn} from '@shared/ui/utils';

type GrupoFormBaseProps = {
	isSubmitting?: boolean;
	onBack: () => void;
	roles: RoleDto[];
};

type GrupoCreateFormProps = GrupoFormBaseProps & {
	mode: 'create';
	onSubmit: (data: GrupoRegisterRequest) => Promise<unknown> | unknown;
};

type GrupoEditFormProps = GrupoFormBaseProps & {
	deleteActionLoading?: boolean;
	grupo: GrupoDto;
	mode: 'edit';
	onDelete?: () => void;
	onSubmit: (data: GrupoUpdateRequest) => Promise<unknown> | unknown;
};

export type GrupoFormProps = GrupoCreateFormProps | GrupoEditFormProps;

const grupoRegisterDefaultValues: GrupoRegisterFormValues = {
	nome: '',
	roles: [],
};

function getGrupoEditDefaultValues(grupo: GrupoDto): GrupoEditFormValues {
	return {
		nome: grupo.nome,
		roles: [...grupo.roles],
	};
}

function getRoleOptions(roles: RoleDto[]) {
	return filterAssignableSistemaRoles(roles).map((role) => ({
		label: role.descricao,
		value: role.nome,
	}));
}

function GrupoRolesField({ roles }: { roles: RoleDto[] }) {
	return (
		<FormCheckboxGroup
			name="roles"
			label="Permissões"
			options={getRoleOptions(roles)}
			required
		/>
	);
}

function GrupoCreateForm({
	isSubmitting,
	onBack,
	onSubmit,
	roles,
}: GrupoCreateFormProps) {
	const form = useForm({
		defaultValues: grupoRegisterDefaultValues,
		validators: {
			onSubmit: grupoRegisterFormSchema as any,
		},
		onSubmit: async ({ value, formApi }) => {
			try {
				await onSubmit(parseGrupoRegisterForm(value));
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
				<CardTitle>Novo Grupo</CardTitle>
				<CardDescription>
					Cadastre um grupo e defina as permissões de acesso ao painel.
				</CardDescription>
			</CardHeader>
			<CardContent>
				<Form form={form} className="space-y-6">
					<div className="grid gap-4">
						<FormInputText name="nome" label="Nome" required />
						<GrupoRolesField roles={roles} />
					</div>

					<GrupoFormActions isSubmitting={isSubmitting} onBack={onBack} />
				</Form>
			</CardContent>
		</Card>
	);
}

function GrupoEditForm({
	deleteActionLoading,
	grupo,
	isSubmitting,
	onBack,
	onDelete,
	onSubmit,
	roles,
}: GrupoEditFormProps) {
	const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);

	const form = useForm({
		defaultValues: getGrupoEditDefaultValues(grupo),
		validators: {
			onSubmit: grupoEditFormSchema as any,
		},
		onSubmit: async ({ value, formApi }) => {
			try {
				await onSubmit(parseGrupoUpdateForm(value));
			} catch (error) {
				parseServerErrorResponse(error, {
					formApi,
				});
			}
		},
	});

	return (
		<>
			<Card>
				<CardHeader>
					<CardTitle>Editar Grupo</CardTitle>
					<CardDescription>
						Atualize o nome e as permissões do grupo.
					</CardDescription>
				</CardHeader>
				<CardContent>
					<Form form={form} className="space-y-6">
						<div className="grid gap-4">
							<FormInputText name="nome" label="Nome" required />
							<GrupoRolesField roles={roles} />
						</div>

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
								<Button
									type="button"
									className={cn(
										formActionButtonClassName,
										formActionDeleteButtonClassName,
									)}
									onClick={() => setIsDeleteDialogOpen(true)}
									disabled={deleteActionLoading}
								>
									<Trash2 className="size-4" />
									Excluir
								</Button>
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

			<Dialog open={isDeleteDialogOpen} onOpenChange={setIsDeleteDialogOpen}>
				<DialogContent>
					<DialogHeader>
						<DialogTitle>Excluir grupo</DialogTitle>
						<DialogDescription>
							Tem certeza que deseja excluir o grupo &quot;{grupo.nome}&quot;?
							Esta ação não pode ser desfeita.
						</DialogDescription>
					</DialogHeader>
					<DialogFooter>
						<DialogClose asChild>
							<Button type="button" variant="outline">
								Cancelar
							</Button>
						</DialogClose>
						<Button
							type="button"
							className={formActionDeleteButtonClassName}
							disabled={deleteActionLoading}
							onClick={() => {
								setIsDeleteDialogOpen(false);
								onDelete?.();
							}}
						>
							Excluir
						</Button>
					</DialogFooter>
				</DialogContent>
			</Dialog>
		</>
	);
}

function GrupoFormActions({
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

export default function GrupoForm(props: GrupoFormProps) {
	if (props.mode === 'create') {
		return <GrupoCreateForm {...props} />;
	}

	return (
		<GrupoEditForm
			key={`${props.grupo.id}-${props.grupo.roles.join(',')}`}
			{...props}
		/>
	);
}
