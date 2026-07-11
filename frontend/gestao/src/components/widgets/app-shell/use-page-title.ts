import {useEffect} from 'react';

import {getDefaultAppTitle} from '@entities/platform-settings';
import {setDocumentTitle} from '@shared/ui/page-title.utils';

import {useOptionalAppShell} from './AppShellProvider';

export function usePageTitle(pageTitle?: string, appTitle?: string) {
	const shell = useOptionalAppShell();
	const resolvedAppTitle =
		appTitle ?? shell?.branding.appTitle ?? getDefaultAppTitle();

	useEffect(() => {
		setDocumentTitle(pageTitle, resolvedAppTitle);
	}, [pageTitle, resolvedAppTitle]);
}
