import {buildMenuTreeFromNodes, type MenuRecord} from './menu-tree.utils';

function createNode(overrides: Partial<MenuRecord> & Pick<MenuRecord, 'id' | 'label' | 'sortOrder'>): MenuRecord {
	return {
		parentId: null,
		description: null,
		icon: null,
		enabled: true,
		roles: [],
		route: null,
		externalUrl: null,
		...overrides,
	};
}

describe('buildMenuTreeFromNodes', () => {
	it('builds a simple tree with one root and two children', () => {
		const nodes: MenuRecord[] = [
			createNode({ id: 'root', label: 'Root', sortOrder: 0 }),
			createNode({ id: 'child-a', parentId: 'root', label: 'Child A', sortOrder: 0 }),
			createNode({ id: 'child-b', parentId: 'root', label: 'Child B', sortOrder: 1 }),
		];

		const tree = buildMenuTreeFromNodes(nodes);

		expect(tree).toHaveLength(1);
		expect(tree[0].label).toBe('Root');
		expect(tree[0].items).toHaveLength(2);
		expect(tree[0].items?.[0].label).toBe('Child A');
		expect(tree[0].items?.[1].label).toBe('Child B');
	});

	it('sorts siblings by sortOrder', () => {
		const nodes: MenuRecord[] = [
			createNode({ id: 'root', label: 'Root', sortOrder: 0 }),
			createNode({ id: 'third', parentId: 'root', label: 'Third', sortOrder: 2 }),
			createNode({ id: 'first', parentId: 'root', label: 'First', sortOrder: 0 }),
			createNode({ id: 'second', parentId: 'root', label: 'Second', sortOrder: 1 }),
		];

		const tree = buildMenuTreeFromNodes(nodes);
		const labels = tree[0].items?.map((item) => item.label);

		expect(labels).toEqual(['First', 'Second', 'Third']);
	});

	it('builds multi-level nesting', () => {
		const nodes: MenuRecord[] = [
			createNode({ id: 'root', label: 'Root', sortOrder: 0 }),
			createNode({ id: 'group', parentId: 'root', label: 'Group', sortOrder: 0 }),
			createNode({ id: 'leaf', parentId: 'group', label: 'Leaf', sortOrder: 0, route: '/leaf' }),
		];

		const tree = buildMenuTreeFromNodes(nodes);
		const group = tree[0].items?.[0];
		const leaf = group?.items?.[0];

		expect(group?.label).toBe('Group');
		expect(leaf?.label).toBe('Leaf');
		expect(leaf?.route).toBe('/leaf');
	});

	it('normalizes null optional fields to undefined and omits empty roles', () => {
		const nodes: MenuRecord[] = [
			createNode({
				id: 'root',
				label: 'Root',
				sortOrder: 0,
				description: null,
				icon: null,
				roles: [],
			}),
			createNode({
				id: 'child',
				parentId: 'root',
				label: 'Child',
				sortOrder: 0,
				description: null,
				icon: null,
				route: null,
				externalUrl: null,
				roles: [],
			}),
		];

		const tree = buildMenuTreeFromNodes(nodes);
		const root = tree[0];
		const child = root.items?.[0];

		expect(root.description).toBeUndefined();
		expect(root.icon).toBeUndefined();
		expect(root.roles).toBeUndefined();
		expect(child?.description).toBeUndefined();
		expect(child?.icon).toBeUndefined();
		expect(child?.route).toBeUndefined();
		expect(child?.externalUrl).toBeUndefined();
		expect(child?.roles).toBeUndefined();
	});

	it('preserves populated roles on menu items', () => {
		const nodes: MenuRecord[] = [
			createNode({ id: 'root', label: 'Root', sortOrder: 0 }),
			createNode({
				id: 'child',
				parentId: 'root',
				label: 'Child',
				sortOrder: 0,
				roles: ['SISTEMAS.USUARIOS'],
			}),
		];

		const tree = buildMenuTreeFromNodes(nodes);

		expect(tree[0].items?.[0].roles).toEqual(['SISTEMAS.USUARIOS']);
	});

	it('returns an empty array for an empty node list', () => {
		expect(buildMenuTreeFromNodes([])).toEqual([]);
	});

	it('excludes orphan nodes with a missing parent from roots and children', () => {
		const nodes: MenuRecord[] = [
			createNode({ id: 'root', label: 'Root', sortOrder: 0 }),
			createNode({ id: 'orphan', parentId: 'missing-parent', label: 'Orphan', sortOrder: 0 }),
		];

		const tree = buildMenuTreeFromNodes(nodes);

		expect(tree).toHaveLength(1);
		expect(tree[0].label).toBe('Root');
		expect(tree[0].items).toEqual([]);
	});
});
