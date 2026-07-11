import {Link} from '@tanstack/react-router';

import {CommonLayout} from '@components/widgets/common-layout';
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from '@components/ui/card';
import {Button} from '@components/ui/button';

export function NotFoundPage() {
	return (
		<CommonLayout
			title="Página não encontrada"
			description="O endereço solicitado não existe ou foi movido."
		>
			<Card className="text-center">
				<CardHeader>
					<CardTitle>404</CardTitle>
					<CardDescription>
						Verifique o endereço ou volte ao painel principal.
					</CardDescription>
				</CardHeader>
				<CardContent>
					<Button className="w-full" asChild>
						<Link to="/">Ir para o início</Link>
					</Button>
				</CardContent>
			</Card>
		</CommonLayout>
	);
}
