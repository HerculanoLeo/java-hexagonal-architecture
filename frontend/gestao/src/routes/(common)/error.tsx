import {createFileRoute} from '@tanstack/react-router';

import {ErrorPage} from '@components/pages/common/ErrorPage';

export const Route = createFileRoute('/(common)/error')({
	component: ErrorPage,
});
