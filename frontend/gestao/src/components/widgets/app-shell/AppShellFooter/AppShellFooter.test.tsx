import {render, screen} from '@testing-library/react';
import {vi} from 'vitest';

import {useAppShell} from '../AppShellProvider';
import {AppShellFooter} from './index';

vi.mock('../AppShellProvider', () => ({
	useAppShell: vi.fn(),
}));

const mockedUseAppShell = vi.mocked(useAppShell);

describe('AppShellFooter', () => {
	beforeEach(() => {
		mockedUseAppShell.mockReturnValue({
			appVersion: '1.2.3',
			branding: {
				appTitle: 'Gestão',
				projectName: 'Starter Admin',
				logoSrc: '/logo.png',
				logoAlt: 'Starter Admin',
				buttonColors: {
					activateBg: '#2563eb',
					activateHover: '#1d4ed8',
					backBg: '#f1f5f9',
					backForeground: '#1e3a8a',
					backHover: '#e2e8f0',
					deleteBg: '#dc2626',
					deleteHover: '#b91c1c',
					saveBg: '#059669',
					saveHover: '#047857',
				},
				themeColors: {
					primary: {
						dark: { bg: '#000000', foreground: '#ffffff' },
						light: { bg: '#ffffff', foreground: '#000000' },
					},
					secondary: {
						dark: { bg: '#111111', foreground: '#eeeeee' },
						light: { bg: '#eeeeee', foreground: '#111111' },
					},
					shellGradient: {
						dark: { from: '#000000', to: '#111111' },
						light: { from: '#ffffff', to: '#eeeeee' },
					},
					shellText: {
						dark: { foreground: '#ffffff', mutedForeground: '#cccccc' },
						light: { foreground: '#000000', mutedForeground: '#666666' },
					},
					menuNavigation: {
						dark: {
							titleActiveForeground: '#ffffff',
							itemHoverBg: '#111111',
							itemActiveBg: '#222222',
							itemActiveForeground: '#ffffff',
							submenuHoverBg: '#333333',
							submenuActiveBg: '#444444',
							submenuActiveForeground: '#ffffff',
						},
						light: {
							titleActiveForeground: '#000000',
							itemHoverBg: '#f5f5f5',
							itemActiveBg: '#e5e5e5',
							itemActiveForeground: '#000000',
							submenuHoverBg: '#f0f0f0',
							submenuActiveBg: '#e0e0e0',
							submenuActiveForeground: '#000000',
						},
					},
				},
			},
			hasFixedSidebar: true,
			layoutConfig: {
				appVersion: '1.2.3',
				menuLayout: 'sidebar',
			},
			menus: [],
			showMenuBar: false,
			usuario: {
				id: 'user-1',
				nome: 'Test User',
				email: 'test@example.com',
				status: 'ATIVO',
			},
		});
	});

	it('renders copyright with the project name and current year', () => {
		const currentYear = new Date().getFullYear();

		render(<AppShellFooter />);

		expect(
			screen.getByText(
				`© ${currentYear} Starter Admin. Todos os direitos reservados.`,
			),
		).toBeInTheDocument();
	});

	it('renders the application version from context', () => {
		render(<AppShellFooter />);

		expect(screen.getByText('Versão 1.2.3')).toBeInTheDocument();
	});

	it('exposes a footer landmark', () => {
		render(<AppShellFooter />);

		expect(screen.getByRole('contentinfo')).toBeInTheDocument();
	});
});
