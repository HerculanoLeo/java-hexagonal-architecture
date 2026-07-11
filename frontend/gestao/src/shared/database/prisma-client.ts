import {PrismaPg} from '@prisma/adapter-pg';

import {env} from '@/env';
import type {Prisma} from '@/generated/prisma/client';
import {PrismaClient} from '@/generated/prisma/client';

const globalForPrisma = globalThis as typeof globalThis & {
	prismaClient?: PrismaClient<'query'>;
};

function createPrismaClient() {
	if (!env.DATABASE_URL) {
		return undefined;
	}

	const adapter = new PrismaPg({
		connectionString: env.DATABASE_URL,
	});
	const shouldLogQueries =
		env.PRISMA_LOG_QUERIES || env.NODE_ENV === 'development';

	const prismaClient = new PrismaClient({
		adapter,
		log: [{ emit: 'event', level: 'query' }],
	});

	if (shouldLogQueries) {
		prismaClient.$on('query', (event: Prisma.QueryEvent) => {
			console.info('[prisma:query]', {
				duration: `${event.duration}ms`,
				params: event.params,
				query: event.query,
			});
		});
	}

	return prismaClient;
}

export const prismaClient =
	globalForPrisma.prismaClient ?? createPrismaClient();

if (env.NODE_ENV !== 'production') {
	globalForPrisma.prismaClient = prismaClient;
}
