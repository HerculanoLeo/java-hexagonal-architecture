import {createInMemoryCacheStore} from './cache-store.type';

describe('createInMemoryCacheStore', () => {
	const cacheKey = `test-cache-${crypto.randomUUID()}`;

	afterEach(() => {
		createInMemoryCacheStore<string>(cacheKey).clear();
	});

	it('returns undefined before a value is set', () => {
		const store = createInMemoryCacheStore<string>(cacheKey);

		expect(store.get()).toBeUndefined();
	});

	it('stores and retrieves values', () => {
		const store = createInMemoryCacheStore<string>(cacheKey);

		store.set('cached-value');

		expect(store.get()).toBe('cached-value');
	});

	it('clears stored values', () => {
		const store = createInMemoryCacheStore<string>(cacheKey);

		store.set('cached-value');
		store.clear();

		expect(store.get()).toBeUndefined();
	});

	it('isolates values by cache key', () => {
		const firstKey = `test-cache-a-${crypto.randomUUID()}`;
		const secondKey = `test-cache-b-${crypto.randomUUID()}`;
		const firstStore = createInMemoryCacheStore<string>(firstKey);
		const secondStore = createInMemoryCacheStore<string>(secondKey);

		firstStore.set('first');
		secondStore.set('second');

		expect(firstStore.get()).toBe('first');
		expect(secondStore.get()).toBe('second');

		firstStore.clear();
		secondStore.clear();
	});
});
