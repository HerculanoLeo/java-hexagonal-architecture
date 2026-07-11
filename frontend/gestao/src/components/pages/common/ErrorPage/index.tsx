import {Link} from '@tanstack/react-router';

import {CommonLayout} from '@components/widgets/common-layout';
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from '@components/ui/card';
import {Button} from '@components/ui/button';

export function ErrorPage() {
	return (
		<CommonLayout
			title="Algo deu errado"
			description="Não foi possível processar sua solicitação."
		>
			<Card className="text-center">
				<CardHeader>
					<CardTitle>Erro</CardTitle>
					<CardDescription>
						Ocorreu um erro inesperado. Tente novamente ou entre em contato com
						o suporte.
					</CardDescription>
				</CardHeader>
				<CardContent>
					<Button className="w-full" asChild>
						<Link to="/login">Tentar novamente</Link>
					</Button>
				</CardContent>
			</Card>
		</CommonLayout>
	);
}
