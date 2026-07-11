import {formatDocumentTitle, setDocumentTitle} from './page-title.utils';

describe('formatDocumentTitle', () => {
	it('returns only the app title when page title is empty', () => {
		expect(formatDocumentTitle(undefined, 'Gestão')).toBe('Gestão');
		expect(formatDocumentTitle('   ', 'Gestão')).toBe('Gestão');
	});

	it('returns only the app title when both titles match', () => {
		expect(formatDocumentTitle('Gestão', 'Gestão')).toBe('Gestão');
	});

	it('combines page and app titles', () => {
		expect(formatDocumentTitle('Usuários', 'Gestão')).toBe('Usuários · Gestão');
	});
});

describe('setDocumentTitle', () => {
	it('updates document.title', () => {
		setDocumentTitle('Usuários', 'Gestão');

		expect(document.title).toBe('Usuários · Gestão');
	});
});
