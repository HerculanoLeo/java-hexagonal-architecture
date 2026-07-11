import {Link} from '@tanstack/react-router';
import {ArrowRight, KeyRound, UserRound} from 'lucide-react';

import {ContentLayout} from '@components/widgets/content-layout';
import {Card, CardDescription, CardHeader, CardTitle,} from '@components/ui/card';

import {meuPerfilBreadcrumbs} from '@components/pages/me/lib/me.breadcrumbs';

const profileFunctions = [
	{
		description:
			'Atualize seu nome e visualize e-mail, grupo e status da sua conta.',
		icon: UserRound,
		title: 'Meus Dados',
		to: '/me/dados' as const,
	},
	{
		description: 'Altere sua senha de acesso informando a senha atual.',
		icon: KeyRound,
		title: 'Trocar Senha',
		to: '/me/senha' as const,
	},
];

export function MeuPerfilPage() {
	return (
		<ContentLayout breadcrumbs={meuPerfilBreadcrumbs}>
			<div className="grid gap-4 md:grid-cols-2">
				{profileFunctions.map((item) => (
					<Link
						key={item.to}
						to={item.to}
						className="group block rounded-xl focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
					>
						<Card className="h-full transition-colors group-hover:bg-muted/40">
							<CardHeader className="gap-3">
								<div className="flex items-start justify-between gap-3">
									<div className="flex size-10 items-center justify-center rounded-lg bg-muted text-muted-foreground">
										<item.icon className="size-5" />
									</div>
									<ArrowRight className="size-5 text-muted-foreground transition-transform group-hover:translate-x-0.5" />
								</div>
								<div className="space-y-2">
									<CardTitle>{item.title}</CardTitle>
									<CardDescription>{item.description}</CardDescription>
								</div>
							</CardHeader>
						</Card>
					</Link>
				))}
			</div>
		</ContentLayout>
	);
}
