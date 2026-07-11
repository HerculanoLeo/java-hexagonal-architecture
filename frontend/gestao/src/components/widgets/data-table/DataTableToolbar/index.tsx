import type {PropsWithChildren} from 'react';

import {dataTableToolbarDisplayName} from '../data-table.types';

export function DataTableToolbar({ children }: PropsWithChildren) {
	return <>{children}</>;
}

DataTableToolbar.displayName = dataTableToolbarDisplayName;
