import type {PropsWithChildren} from 'react';

export type AppShellProps = PropsWithChildren<{
	onSignOut?: () => void;
}>;
