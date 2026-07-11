import {createFileRoute} from '@tanstack/react-router';

import {RegisterPage} from '@components/pages/auth/RegisterPage';

export const Route = createFileRoute('/(unauthenticated)/register')({
	component: RegisterPage,
});
