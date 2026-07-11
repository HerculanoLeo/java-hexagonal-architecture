import type {CacheStore} from '@shared/cache/cache-store.type';

// Auth sessions are persisted in the database (Prisma). For multi-instance deploys,
// use a Redis-backed CacheStore here for distributed session lookups or revocation
// coordination when process-local caches are no longer sufficient.
export type SessionDistributedCacheStore<T> = CacheStore<T>;
