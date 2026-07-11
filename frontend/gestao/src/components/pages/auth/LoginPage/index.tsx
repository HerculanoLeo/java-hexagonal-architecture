import {getRouteApi} from '@tanstack/react-router';
import {KeyRound, Loader2} from 'lucide-react';
import {useState} from 'react';

import {CommonLayout} from '@components/widgets/common-layout';
import {Button} from '@components/ui/button';
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from '@components/ui/card';
import {authClient} from '@shared/auth/auth-client.configuration';

const routeApi = getRouteApi('/(unauthenticated)/login');

export function LoginPage() {
	const search = routeApi.useSearch();
	const [isLoading, setIsLoading] = useState(false);

	async function signInWithKeycloak() {
		setIsLoading(true);
		await authClient.signIn.oauth2({
			providerId: 'keycloak',
			callbackURL: search.redirect || '/',
			errorCallbackURL: '/login',
		});
	}

	return (
		<CommonLayout
			title="Acesse o painel"
			description="Entre com seu usuário corporativo para continuar."
		>
			<Card>
				<CardHeader>
					<CardTitle>Login</CardTitle>
					<CardDescription>
						Use o provedor de identidade configurado para acessar a área
						administrativa.
					</CardDescription>
				</CardHeader>
				<CardContent>
					<Button
						type="button"
						className="w-full"
						onClick={() => void signInWithKeycloak()}
						disabled={isLoading}
					>
						{isLoading ? (
							<Loader2 className="size-4 animate-spin" />
						) : (
							<KeyRound className="size-4" />
						)}
						Entrar com Keycloak
					</Button>
				</CardContent>
			</Card>
		</CommonLayout>
	);
}
