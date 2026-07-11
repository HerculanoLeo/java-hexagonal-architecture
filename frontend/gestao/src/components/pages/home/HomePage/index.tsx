import {usePageTitle} from '@components/widgets/app-shell/use-page-title';

export function HomePage() {
	usePageTitle('Início');

	return (
		<div className="rounded-lg border border-dashed border-border bg-background/60 p-8 text-center text-muted-foreground">
			Bem-vindo ao painel administrativo.
		</div>
	);
}
