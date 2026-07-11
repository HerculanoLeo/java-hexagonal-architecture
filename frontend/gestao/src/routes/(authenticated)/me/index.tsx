import {createFileRoute} from '@tanstack/react-router';

import {MeuPerfilPage} from '@components/pages/me/MeuPerfilPage';

export const Route = createFileRoute('/(authenticated)/me/')({
	component: MeuPerfilPage,
});
