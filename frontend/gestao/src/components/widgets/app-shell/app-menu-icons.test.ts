import {Building2, Users} from 'lucide-react';

import {getMenuIcon} from './app-menu-icons';

describe('getMenuIcon', () => {
	it('returns undefined when icon is missing', () => {
		expect(getMenuIcon()).toBeUndefined();
	});

	it('returns undefined for unknown icon names', () => {
		expect(getMenuIcon('UnknownIcon')).toBeUndefined();
	});

	it('returns the mapped lucide icon component', () => {
		expect(getMenuIcon('Users')).toBe(Users);
		expect(getMenuIcon('Building2')).toBe(Building2);
	});
});
