import type {PropsWithChildren} from 'react';

export function AppShellMain({ children }: PropsWithChildren) {
	return (
		<main className="min-h-0 flex-1 overflow-y-auto px-5 py-8 sm:px-8 lg:px-10">
			{children}
		</main>
	);
}
