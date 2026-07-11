import {createFileRoute} from '@tanstack/react-router';

import {TrocarSenhaPage} from '@components/pages/me/TrocarSenhaPage';

export const Route = createFileRoute('/(authenticated)/me/senha/')({
	component: TrocarSenhaPage,
});
