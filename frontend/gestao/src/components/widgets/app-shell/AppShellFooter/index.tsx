import {useAppShell} from '../AppShellProvider';

export function AppShellFooter() {
	const { appVersion, branding } = useAppShell();
	const currentYear = new Date().getFullYear();

	return (
		<footer className="shrink-0 border-t border-border bg-background/80 px-5 py-4 text-xs text-muted-foreground shadow-[0_-1px_6px_rgba(15,23,42,0.03)] sm:px-8">
			<div className="grid gap-2 sm:grid-cols-3 sm:items-center">
				<span className="hidden sm:block" aria-hidden="true" />
				<span className="text-center">
					© {currentYear} {branding.projectName}. Todos os direitos reservados.
				</span>
				<span className="text-center sm:text-right">Versão {appVersion}</span>
			</div>
		</footer>
	);
}
