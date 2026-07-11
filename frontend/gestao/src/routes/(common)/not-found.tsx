import {createFileRoute} from '@tanstack/react-router';

import {NotFoundPage} from '@components/pages/common/NotFoundPage';

export const Route = createFileRoute('/(common)/not-found')({
	component: NotFoundPage,
});
