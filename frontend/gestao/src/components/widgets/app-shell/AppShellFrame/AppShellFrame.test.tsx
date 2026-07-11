import {render, screen} from '@testing-library/react';
import {vi} from 'vitest';

import {useAppShell} from '../AppShellProvider';
import {AppShellFrame} from './index';

vi.mock('../AppShellProvider', () => ({
	useAppShell: vi.fn(),
}));

vi.mock('../AppSidebar', () => ({
	AppSidebar: ({
		variant,
		isEnabled,
	}: {
		variant: 'drawer' | 'fixed';
		isEnabled?: boolean;
	}) => (
		<div
			data-testid={`sidebar-${variant}`}
			data-enabled={String(isEnabled ?? true)}
			aria-hidden={variant === 'fixed' && !isEnabled ? true : undefined}
		/>
	),
}));

vi.mock('../AppTopbar', () => ({
	AppTopbar: () => <div data-testid="topbar" />,
}));

vi.mock('../AppMenuBar', () => ({
	AppMenuBar: () => <nav aria-label="Menu principal" />,
}));

vi.mock('../AppShellFooter', () => ({
	AppShellFooter: () => <footer data-testid="footer" />,
}));

const mockedUseAppShell = vi.mocked(useAppShell);

function renderFrame(
	context: { hasFixedSidebar: boolean; showMenuBar: boolean },
) {
	mockedUseAppShell.mockReturnValue({
		appVersion: '1.0.0',
		branding: {
			appTitle: 'Gestão',
			projectName: 'Starter',
			logoSrc: '/logo.png',
			logoAlt: 'Starter',
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
		hasFixedSidebar: context.hasFixedSidebar,
		layoutConfig: {
			appVersion: '1.0.0',
			menuLayout: context.hasFixedSidebar ? 'sidebar' : 'hybrid',
		},
		menus: [],
		showMenuBar: context.showMenuBar,
		usuario: {
			id: 'user-1',
			nome: 'Test User',
			email: 'test@example.com',
			status: 'ATIVO',
		},
	});

	return render(
		<AppShellFrame
			isSidebarOpen={false}
			onCloseSidebar={() => undefined}
			onOpenSidebar={() => undefined}
		>
			<div data-testid="page-content">Conteúdo da página</div>
		</AppShellFrame>,
	);
}

describe('AppShellFrame', () => {
	it('renders page children in the main column', () => {
		renderFrame({ hasFixedSidebar: true, showMenuBar: false });

		expect(screen.getByTestId('page-content')).toBeInTheDocument();
	});

	it('shows the horizontal menu bar only when showMenuBar is true', () => {
		renderFrame({ hasFixedSidebar: false, showMenuBar: true });

		expect(screen.getByRole('navigation', { name: 'Menu principal' })).toBeInTheDocument();
	});

	it('hides the horizontal menu bar when showMenuBar is false', () => {
		renderFrame({ hasFixedSidebar: true, showMenuBar: false });

		expect(
			screen.queryByRole('navigation', { name: 'Menu principal' }),
		).not.toBeInTheDocument();
	});

	it('enables the fixed sidebar only when hasFixedSidebar is true', () => {
		renderFrame({ hasFixedSidebar: true, showMenuBar: false });

		expect(screen.getByTestId('sidebar-fixed')).toHaveAttribute(
			'data-enabled',
			'true',
		);
		expect(screen.getByTestId('sidebar-fixed')).not.toHaveAttribute('aria-hidden');
	});

	it('disables the fixed sidebar when hasFixedSidebar is false', () => {
		renderFrame({ hasFixedSidebar: false, showMenuBar: true });

		expect(screen.getByTestId('sidebar-fixed')).toHaveAttribute(
			'data-enabled',
			'false',
		);
		expect(screen.getByTestId('sidebar-fixed')).toHaveAttribute('aria-hidden', 'true');
	});
});
