import {useForm} from '@tanstack/react-form';
import {Save} from 'lucide-react';

import Form from '@components/forms';
import {formActionButtonClassName, FormActions, formActionSaveButtonClassName,} from '@components/forms/FormActions';
import FormButtonSubmit from '@components/forms/FormButtonSubmit';
import FormDropdown from '@components/forms/FormDropdown';
import FormInputColor from '@components/forms/FormInputColor';
import FormInputText from '@components/forms/FormInputText';
import {Button} from '@components/ui/button';
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from '@components/ui/card';
import type {PlatformSettingsDto, PlatformSettingsUpdateRequest,} from '@entities/platform-settings';
import {
    DEFAULT_STATIC_LOGO_SRC,
    parsePlatformSettingsForm,
    platformSettingsDtoToFormValues,
    platformSettingsFormSchema,
} from '@entities/platform-settings';
import {parseServerErrorResponse} from '@shared/forms/server-error-response';
import {getShellGradientPreviewStyle, getShellTextPreviewStyle,} from '@shared/ui/theme-colors.utils';
import {cn} from '@shared/ui/utils';

type ConfiguracoesFormProps = {
	isSubmitting?: boolean;
	onSubmit: (data: PlatformSettingsUpdateRequest) => Promise<unknown> | unknown;
	settings: PlatformSettingsDto;
};

const menuLayoutOptions = [
	{
		label: 'Híbrido — menu horizontal no desktop, sidebar no celular',
		value: 'hybrid',
	},
	{
		label: 'Lateral — somente sidebar (fixa no desktop, drawer no celular)',
		value: 'sidebar',
	},
];

const logoSourceOptions = [
	{ label: 'Estática (bundle)', value: 'static' },
	{ label: 'URL externa', value: 'external' },
];

function resolvePreviewLogoSrc(
	logoSource: string,
	logoUrl: string | undefined,
) {
	if (logoSource === 'external' && logoUrl?.trim()) {
		return logoUrl.trim();
	}

	return DEFAULT_STATIC_LOGO_SRC;
}

export function ConfiguracoesForm({
	isSubmitting,
	onSubmit,
	settings,
}: ConfiguracoesFormProps) {
	const form = useForm({
		defaultValues: platformSettingsDtoToFormValues(settings),
		validators: {
			onSubmit: platformSettingsFormSchema as any,
		},
		onSubmit: async ({ value, formApi }) => {
			try {
				await onSubmit(parsePlatformSettingsForm(value));
			} catch (error) {
				parseServerErrorResponse(error, {
					formApi,
				});
			}
		},
	});

	return (
		<Form form={form} className="space-y-6">
			<Card>
				<CardHeader>
					<CardTitle>Identidade</CardTitle>
					<CardDescription>
						Defina o nome do projeto e o título exibido na aba do navegador.
					</CardDescription>
				</CardHeader>
				<CardContent className="space-y-6">
					<div className="grid gap-4 md:grid-cols-2">
						<FormInputText
							name="projectName"
							label="Nome do projeto"
							required
						/>
						<FormInputText
							name="appTitle"
							label="Título da aplicação"
							required
						/>
					</div>
				</CardContent>
			</Card>

			<Card>
				<CardHeader>
					<CardTitle>Navegação</CardTitle>
					<CardDescription>
						Escolha como o menu principal será exibido no painel.
					</CardDescription>
				</CardHeader>
				<CardContent>
					<FormDropdown
						name="menuLayout"
						label="Tipo de menu"
						options={menuLayoutOptions}
						required
					/>
				</CardContent>
			</Card>

			<Card>
				<CardHeader>
					<CardTitle>Logo</CardTitle>
					<CardDescription>
						Use a logo do bundle ou informe uma URL externa (ex.: S3).
					</CardDescription>
				</CardHeader>
				<CardContent className="space-y-6">
					<div className="grid gap-4 md:grid-cols-2">
						<FormDropdown
							name="logoSource"
							label="Origem da logo"
							options={logoSourceOptions}
							required
						/>
						<form.Subscribe
							selector={(state) => ({
								logoSource: state.values.logoSource,
								logoUrl: state.values.logoUrl,
							})}
						>
							{({ logoSource }) =>
								logoSource === 'external' ? (
									<FormInputText
										name="logoUrl"
										label="URL da logo"
										type="url"
										placeholder="https://..."
										required
									/>
								) : (
									<div className="space-y-2">
										<p className="text-sm font-medium">URL da logo</p>
										<p className="text-sm text-muted-foreground">
											Usando asset estático {DEFAULT_STATIC_LOGO_SRC}
										</p>
									</div>
								)
							}
						</form.Subscribe>
					</div>

					<form.Subscribe
						selector={(state) => ({
							logoSource: state.values.logoSource,
							logoUrl: state.values.logoUrl,
							projectName: state.values.projectName,
						})}
					>
						{({ logoSource, logoUrl, projectName }) => (
							<div className="rounded-lg border border-dashed border-border bg-muted/30 p-4">
								<p className="mb-3 text-sm font-medium text-muted-foreground">
									Pré-visualização
								</p>
								<div className="flex h-16 w-40 items-center justify-center rounded-lg border border-border bg-background px-3">
									<img
										src={resolvePreviewLogoSrc(logoSource, logoUrl)}
										alt={projectName || 'Logo'}
										className="max-h-10 max-w-full object-contain"
									/>
								</div>
							</div>
						)}
					</form.Subscribe>
				</CardContent>
			</Card>

			<Card>
				<CardHeader>
					<CardTitle>Cores do tema</CardTitle>
					<CardDescription>
						Defina o gradiente, as cores de texto do header/menus e as cores dos
						botões primário e secundário para os temas claro e escuro.
					</CardDescription>
				</CardHeader>
				<CardContent className="space-y-8">
					<div className="space-y-4">
						<h3 className="text-sm font-semibold">Gradiente do painel</h3>
						<div className="space-y-4">
							<p className="text-sm text-muted-foreground">Tema claro</p>
							<div className="grid gap-4 md:grid-cols-2">
								<FormInputColor
									name="shellGradientFromLight"
									label="Cor inicial"
									required
								/>
								<FormInputColor
									name="shellGradientToLight"
									label="Cor final"
									required
								/>
							</div>
						</div>
						<div className="space-y-4">
							<p className="text-sm text-muted-foreground">Tema escuro</p>
							<div className="grid gap-4 md:grid-cols-2">
								<FormInputColor
									name="shellGradientFromDark"
									label="Cor inicial"
									required
								/>
								<FormInputColor
									name="shellGradientToDark"
									label="Cor final"
									required
								/>
							</div>
						</div>
					</div>

					<div className="space-y-4">
						<h3 className="text-sm font-semibold">Textos do header e menus</h3>
						<div className="space-y-4">
							<p className="text-sm text-muted-foreground">Tema claro</p>
							<div className="grid gap-4 md:grid-cols-2">
								<FormInputColor
									name="shellForegroundLight"
									label="Texto principal"
									required
								/>
								<FormInputColor
									name="shellMutedForegroundLight"
									label="Texto secundário"
									required
								/>
							</div>
						</div>
						<div className="space-y-4">
							<p className="text-sm text-muted-foreground">Tema escuro</p>
							<div className="grid gap-4 md:grid-cols-2">
								<FormInputColor
									name="shellForegroundDark"
									label="Texto principal"
									required
								/>
								<FormInputColor
									name="shellMutedForegroundDark"
									label="Texto secundário"
									required
								/>
							</div>
						</div>
					</div>

					<div className="space-y-4">
						<h3 className="text-sm font-semibold">
							Título do menu (selecionado)
						</h3>
						<div className="space-y-4">
							<p className="text-sm text-muted-foreground">Tema claro</p>
							<div className="grid gap-4 md:grid-cols-2">
								<FormInputColor
									name="menuTitleActiveForegroundLight"
									label="Cor do título ativo"
									required
								/>
							</div>
						</div>
						<div className="space-y-4">
							<p className="text-sm text-muted-foreground">Tema escuro</p>
							<div className="grid gap-4 md:grid-cols-2">
								<FormInputColor
									name="menuTitleActiveForegroundDark"
									label="Cor do título ativo"
									required
								/>
							</div>
						</div>
					</div>

					<div className="space-y-4">
						<h3 className="text-sm font-semibold">Itens do menu</h3>
						<div className="space-y-4">
							<p className="text-sm text-muted-foreground">Tema claro</p>
							<div className="grid gap-4 md:grid-cols-2">
								<FormInputColor
									name="menuItemHoverBgLight"
									label="Fundo ao passar o mouse"
									required
								/>
								<FormInputColor
									name="menuItemActiveBgLight"
									label="Fundo selecionado"
									required
								/>
								<FormInputColor
									name="menuItemActiveForegroundLight"
									label="Texto selecionado"
									required
								/>
							</div>
						</div>
						<div className="space-y-4">
							<p className="text-sm text-muted-foreground">Tema escuro</p>
							<div className="grid gap-4 md:grid-cols-2">
								<FormInputColor
									name="menuItemHoverBgDark"
									label="Fundo ao passar o mouse"
									required
								/>
								<FormInputColor
									name="menuItemActiveBgDark"
									label="Fundo selecionado"
									required
								/>
								<FormInputColor
									name="menuItemActiveForegroundDark"
									label="Texto selecionado"
									required
								/>
							</div>
						</div>
					</div>

					<div className="space-y-4">
						<h3 className="text-sm font-semibold">Submenus</h3>
						<div className="space-y-4">
							<p className="text-sm text-muted-foreground">Tema claro</p>
							<div className="grid gap-4 md:grid-cols-2">
								<FormInputColor
									name="menuSubmenuHoverBgLight"
									label="Fundo ao passar o mouse"
									required
								/>
								<FormInputColor
									name="menuSubmenuActiveBgLight"
									label="Fundo selecionado"
									required
								/>
								<FormInputColor
									name="menuSubmenuActiveForegroundLight"
									label="Texto selecionado"
									required
								/>
							</div>
						</div>
						<div className="space-y-4">
							<p className="text-sm text-muted-foreground">Tema escuro</p>
							<div className="grid gap-4 md:grid-cols-2">
								<FormInputColor
									name="menuSubmenuHoverBgDark"
									label="Fundo ao passar o mouse"
									required
								/>
								<FormInputColor
									name="menuSubmenuActiveBgDark"
									label="Fundo selecionado"
									required
								/>
								<FormInputColor
									name="menuSubmenuActiveForegroundDark"
									label="Texto selecionado"
									required
								/>
							</div>
						</div>
					</div>

					<div className="space-y-4">
						<h3 className="text-sm font-semibold">Botão primário</h3>
						<div className="space-y-4">
							<p className="text-sm text-muted-foreground">Tema claro</p>
							<div className="grid gap-4 md:grid-cols-2">
								<FormInputColor
									name="primaryBgLight"
									label="Cor de fundo"
									required
								/>
								<FormInputColor
									name="primaryForegroundLight"
									label="Cor do texto"
									required
								/>
							</div>
						</div>
						<div className="space-y-4">
							<p className="text-sm text-muted-foreground">Tema escuro</p>
							<div className="grid gap-4 md:grid-cols-2">
								<FormInputColor
									name="primaryBgDark"
									label="Cor de fundo"
									required
								/>
								<FormInputColor
									name="primaryForegroundDark"
									label="Cor do texto"
									required
								/>
							</div>
						</div>
					</div>

					<div className="space-y-4">
						<h3 className="text-sm font-semibold">Botão secundário</h3>
						<div className="space-y-4">
							<p className="text-sm text-muted-foreground">Tema claro</p>
							<div className="grid gap-4 md:grid-cols-2">
								<FormInputColor
									name="secondaryBgLight"
									label="Cor de fundo"
									required
								/>
								<FormInputColor
									name="secondaryForegroundLight"
									label="Cor do texto"
									required
								/>
							</div>
						</div>
						<div className="space-y-4">
							<p className="text-sm text-muted-foreground">Tema escuro</p>
							<div className="grid gap-4 md:grid-cols-2">
								<FormInputColor
									name="secondaryBgDark"
									label="Cor de fundo"
									required
								/>
								<FormInputColor
									name="secondaryForegroundDark"
									label="Cor do texto"
									required
								/>
							</div>
						</div>
					</div>

					<form.Subscribe
						selector={(state) => ({
							primaryBgDark: state.values.primaryBgDark,
							primaryBgLight: state.values.primaryBgLight,
							primaryForegroundDark: state.values.primaryForegroundDark,
							primaryForegroundLight: state.values.primaryForegroundLight,
							secondaryBgDark: state.values.secondaryBgDark,
							secondaryBgLight: state.values.secondaryBgLight,
							secondaryForegroundDark: state.values.secondaryForegroundDark,
							secondaryForegroundLight: state.values.secondaryForegroundLight,
							shellGradientFromDark: state.values.shellGradientFromDark,
							shellGradientFromLight: state.values.shellGradientFromLight,
							shellGradientToDark: state.values.shellGradientToDark,
							shellGradientToLight: state.values.shellGradientToLight,
							shellForegroundDark: state.values.shellForegroundDark,
							shellForegroundLight: state.values.shellForegroundLight,
							shellMutedForegroundDark: state.values.shellMutedForegroundDark,
							shellMutedForegroundLight: state.values.shellMutedForegroundLight,
						})}
					>
						{(values) => (
							<div className="space-y-4 rounded-lg border border-dashed border-border bg-muted/30 p-4">
								<p className="text-sm font-medium text-muted-foreground">
									Pré-visualização
								</p>
								<div className="grid gap-4 md:grid-cols-2">
									<div className="space-y-2">
										<p className="text-xs font-medium text-muted-foreground">
											Tema claro
										</p>
										<div
											className="flex h-16 items-center justify-between rounded-lg border border-border px-4 shadow-sm"
											style={{
												...getShellGradientPreviewStyle(
													values.shellGradientFromLight,
													values.shellGradientToLight,
												),
												...getShellTextPreviewStyle(
													values.shellForegroundLight,
													values.shellMutedForegroundLight,
												),
											}}
										>
											<span className="text-sm font-semibold">Menu</span>
											<span className="text-xs text-[var(--shell-muted-foreground)]">
												Secundário
											</span>
										</div>
										<div className="flex flex-wrap gap-2">
											<Button
												type="button"
												style={{
													backgroundColor: values.primaryBgLight,
													color: values.primaryForegroundLight,
												}}
											>
												Primário
											</Button>
											<Button
												type="button"
												variant="secondary"
												style={{
													backgroundColor: values.secondaryBgLight,
													color: values.secondaryForegroundLight,
												}}
											>
												Secundário
											</Button>
										</div>
									</div>
									<div className="space-y-2">
										<p className="text-xs font-medium text-muted-foreground">
											Tema escuro
										</p>
										<div
											className="flex h-16 items-center justify-between rounded-lg border border-border px-4 shadow-sm"
											style={{
												...getShellGradientPreviewStyle(
													values.shellGradientFromDark,
													values.shellGradientToDark,
												),
												...getShellTextPreviewStyle(
													values.shellForegroundDark,
													values.shellMutedForegroundDark,
												),
											}}
										>
											<span className="text-sm font-semibold">Menu</span>
											<span className="text-xs text-[var(--shell-muted-foreground)]">
												Secundário
											</span>
										</div>
										<div className="flex flex-wrap gap-2">
											<Button
												type="button"
												style={{
													backgroundColor: values.primaryBgDark,
													color: values.primaryForegroundDark,
												}}
											>
												Primário
											</Button>
											<Button
												type="button"
												variant="secondary"
												style={{
													backgroundColor: values.secondaryBgDark,
													color: values.secondaryForegroundDark,
												}}
											>
												Secundário
											</Button>
										</div>
									</div>
								</div>
							</div>
						)}
					</form.Subscribe>
				</CardContent>
			</Card>

			<Card>
				<CardHeader>
					<CardTitle>Cores dos botões de ação</CardTitle>
					<CardDescription>
						Personalize as cores dos botões de ação do painel.
					</CardDescription>
				</CardHeader>
				<CardContent className="space-y-8">
					<div className="space-y-4">
						<h3 className="text-sm font-semibold">Salvar</h3>
						<div className="grid gap-4 md:grid-cols-2">
							<FormInputColor
								name="buttonSaveBg"
								label="Cor de fundo"
								required
							/>
							<FormInputColor
								name="buttonSaveHover"
								label="Cor ao passar o mouse"
								required
							/>
						</div>
					</div>

					<div className="space-y-4">
						<h3 className="text-sm font-semibold">Ativar</h3>
						<div className="grid gap-4 md:grid-cols-2">
							<FormInputColor
								name="buttonActivateBg"
								label="Cor de fundo"
								required
							/>
							<FormInputColor
								name="buttonActivateHover"
								label="Cor ao passar o mouse"
								required
							/>
						</div>
					</div>

					<div className="space-y-4">
						<h3 className="text-sm font-semibold">Voltar</h3>
						<div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
							<FormInputColor
								name="buttonBackBg"
								label="Cor de fundo"
								required
							/>
							<FormInputColor
								name="buttonBackHover"
								label="Cor ao passar o mouse"
								required
							/>
							<FormInputColor
								name="buttonBackForeground"
								label="Cor do texto"
								required
							/>
						</div>
					</div>

					<div className="space-y-4">
						<h3 className="text-sm font-semibold">Excluir</h3>
						<div className="grid gap-4 md:grid-cols-2">
							<FormInputColor
								name="buttonDeleteBg"
								label="Cor de fundo"
								required
							/>
							<FormInputColor
								name="buttonDeleteHover"
								label="Cor ao passar o mouse"
								required
							/>
						</div>
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
				</CardContent>
			</Card>
		</Form>
	);
}
