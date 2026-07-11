import {createFileRoute} from '@tanstack/react-router';

import {UnauthorizedPage} from '@components/pages/common/UnauthorizedPage';

export const Route = createFileRoute('/(common)/unauthorized')({
	component: UnauthorizedPage,
});
