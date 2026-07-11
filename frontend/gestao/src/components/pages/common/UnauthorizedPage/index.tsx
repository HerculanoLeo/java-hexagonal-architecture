import {Link} from '@tanstack/react-router';

import {CommonLayout} from '@components/widgets/common-layout';
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from '@components/ui/card';
import {Button} from '@components/ui/button';

export function UnauthorizedPage() {
	return (
		<CommonLayout
			title="Acesso negado"
			description="Você não possui permissão para acessar este recurso."
		>
			<Card className="text-center">
				<CardHeader>
					<CardTitle>Não autorizado</CardTitle>
					<CardDescription>
						Solicite acesso ao administrador do sistema se acredita que isso é
						um erro.
					</CardDescription>
				</CardHeader>
				<CardContent>
					<Button className="w-full" asChild>
						<Link to="/">Voltar ao início</Link>
					</Button>
				</CardContent>
			</Card>
		</CommonLayout>
	);
}
