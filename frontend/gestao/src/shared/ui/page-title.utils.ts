export function formatDocumentTitle(
	pageTitle: string | undefined,
	appTitle: string,
) {
	const normalizedPageTitle = pageTitle?.trim();
	const normalizedAppTitle = appTitle.trim();

	if (!normalizedPageTitle) {
		return normalizedAppTitle;
	}

	if (normalizedPageTitle === normalizedAppTitle) {
		return normalizedAppTitle;
	}

	return `${normalizedPageTitle} · ${normalizedAppTitle}`;
}

export function setDocumentTitle(
	pageTitle: string | undefined,
	appTitle: string,
) {
	document.title = formatDocumentTitle(pageTitle, appTitle);
}
