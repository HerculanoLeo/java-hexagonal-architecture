import 'dotenv/config';

import {PrismaPg} from '@prisma/adapter-pg';

import {PrismaClient} from '../src/generated/prisma/client';
import type {MenuDefaultNode} from '../src/entities/menu/lib/menu.defaults';
import {DEFAULT_MENU_NODES} from '../src/entities/menu/lib/menu.defaults';
import {
    getPlatformSettingsSeedData,
    PLATFORM_SETTINGS_ID,
} from '../src/entities/platform-settings/lib/platform-settings.defaults';

function sortNodesForUpsert(nodes: MenuDefaultNode[]) {
	return [...nodes].sort((left, right) => {
		const leftDepth = getNodeDepth(left.id, nodes);
		const rightDepth = getNodeDepth(right.id, nodes);

		if (leftDepth !== rightDepth) {
			return leftDepth - rightDepth;
		}

		return left.sortOrder - right.sortOrder;
	});
}

function getNodeDepth(nodeId: string, nodes: MenuDefaultNode[]) {
	let depth = 0;
	let current = nodes.find((node) => node.id === nodeId);

	while (current?.parentId) {
		depth += 1;
		current = nodes.find((node) => node.id === current?.parentId);
	}

	return depth;
}

async function seedNavigationMenu(prisma: PrismaClient) {
	const orderedNodes = sortNodesForUpsert(DEFAULT_MENU_NODES);

	for (const node of orderedNodes) {
		await prisma.menu.upsert({
			create: {
				description: node.description ?? null,
				enabled: node.enabled ?? true,
				externalUrl: node.externalUrl ?? null,
				icon: node.icon ?? null,
				id: node.id,
				label: node.label,
				parentId: node.parentId,
				roles: node.roles ?? [],
				route: node.route ?? null,
				sortOrder: node.sortOrder,
			},
			update: {
				description: node.description ?? null,
				enabled: node.enabled ?? true,
				externalUrl: node.externalUrl ?? null,
				icon: node.icon ?? null,
				label: node.label,
				parentId: node.parentId,
				roles: node.roles ?? [],
				route: node.route ?? null,
				sortOrder: node.sortOrder,
			},
			where: { id: node.id },
		});
	}

	console.info(`[prisma:seed] menus upserted (${orderedNodes.length} nodes)`);
}

async function main() {
	const databaseUrl = process.env.DATABASE_URL;

	if (!databaseUrl) {
		throw new Error(
			'DATABASE_URL is required to run the seed. Configure it in .env.local.',
		);
	}

	const adapter = new PrismaPg({
		connectionString: databaseUrl,
	});
	const prisma = new PrismaClient({ adapter });

	try {
		const data = getPlatformSettingsSeedData({
			appTitle: process.env.VITE_APP_TITLE,
		});

		await prisma.platformSettings.upsert({
			create: data,
			update: data,
			where: { id: PLATFORM_SETTINGS_ID },
		});

		console.info(
			`[prisma:seed] platform_settings upserted (id=${PLATFORM_SETTINGS_ID})`,
		);

		await seedNavigationMenu(prisma);
	} finally {
		await prisma.$disconnect();
	}
}

main().catch((error) => {
	console.error('[prisma:seed] failed:', error);
	process.exit(1);
});
