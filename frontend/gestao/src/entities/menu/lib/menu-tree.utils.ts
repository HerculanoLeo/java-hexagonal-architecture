import type {MenuDto, MenuItemDto} from '../model/menu.dto';

export type MenuRecord = {
	id: string;
	parentId: string | null;
	label: string;
	description: string | null;
	icon: string | null;
	sortOrder: number;
	enabled: boolean;
	roles: string[];
	route: string | null;
	externalUrl: string | null;
};

function compareBySortOrder(left: MenuRecord, right: MenuRecord) {
	return left.sortOrder - right.sortOrder;
}

function mapNodeToMenuItem(
	node: MenuRecord,
	children: MenuItemDto[],
): MenuItemDto {
	return {
		description: node.description ?? undefined,
		externalUrl: node.externalUrl ?? undefined,
		icon: node.icon ?? undefined,
		id: node.id,
		items: children.length > 0 ? children : undefined,
		label: node.label,
		roles: node.roles.length > 0 ? node.roles : undefined,
		route: node.route ?? undefined,
	};
}

function buildMenuItems(nodes: MenuRecord[], parentId: string): MenuItemDto[] {
	const children = nodes
		.filter((node) => node.parentId === parentId)
		.sort(compareBySortOrder);

	return children.map((node) => {
		const nestedItems = buildMenuItems(nodes, node.id);
		return mapNodeToMenuItem(node, nestedItems);
	});
}

export function buildMenuTreeFromNodes(nodes: MenuRecord[]): MenuDto[] {
	const roots = nodes
		.filter((node) => node.parentId === null)
		.sort(compareBySortOrder);

	return roots.map((root) => ({
		description: root.description ?? undefined,
		icon: root.icon ?? undefined,
		id: root.id,
		items: buildMenuItems(nodes, root.id),
		label: root.label,
		roles: root.roles.length > 0 ? root.roles : undefined,
	}));
}
