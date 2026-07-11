export type CacheStore<T> = {
	get: () => T | undefined;
	set: (value: T) => void;
	clear: () => void;
};

// Process-scoped in-memory store for single-instance deploys.
// Swap the factory for a Redis-backed implementation when scaling horizontally.
export function createInMemoryCacheStore<T>(cacheKey: string): CacheStore<T> {
	const globalScope = globalThis as typeof globalThis &
		Record<string, T | undefined>;

	return {
		get: () => globalScope[cacheKey],
		set: (value) => {
			globalScope[cacheKey] = value;
		},
		clear: () => {
			delete globalScope[cacheKey];
		},
	};
}
