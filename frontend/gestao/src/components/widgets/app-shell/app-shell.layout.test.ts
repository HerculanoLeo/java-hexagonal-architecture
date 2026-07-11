import {getMenuLayoutFlags} from './app-shell.layout';

describe('getMenuLayoutFlags', () => {
	it('enables the fixed sidebar only for sidebar layout', () => {
		expect(getMenuLayoutFlags('sidebar')).toEqual({
			hasFixedSidebar: true,
			showMenuBar: false,
		});
	});

	it('enables the horizontal menu bar only for hybrid layout', () => {
		expect(getMenuLayoutFlags('hybrid')).toEqual({
			hasFixedSidebar: false,
			showMenuBar: true,
		});
	});
});
