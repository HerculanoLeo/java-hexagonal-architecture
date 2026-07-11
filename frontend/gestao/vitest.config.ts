import {defineConfig} from 'vitest/config';

/**
 * Cobertura limitada ao escopo unitário (ver .cursor/rules/frontend-testing.mdc).
 * Rotas, pages, server-fn, repositórios e UI não testada ficam fora do relatório.
 */
const unitCoverageInclude = [
	'src/entities/**/lib/**',
	'src/entities/**/model/**',
	'src/shared/**',
	'src/components/widgets/data-table/**',
	'src/components/widgets/app-shell/**',
	'src/env.ts',
];

const unitCoverageExclude = [
	'src/**/*.{test,spec}.{ts,tsx}',
	'src/test/**',
	'src/generated/**',
	'src/routeTree.gen.ts',
	'src/configurations/**',
	// Tipos e barrels sem lógica
	'src/**/*.dto.ts',
	'src/**/index.ts',
	'src/**/*.types.ts',
	'src/**/*.type.ts',
	// Integração pendente
	'src/**/*.server-fn.ts',
	'src/**/*.server.ts',
	'src/**/*.repository.ts',
	'src/**/*.service.ts',
	'src/**/*.queries.ts',
	'src/shared/database/**',
	'src/**/*.configuration.ts',
	'src/**/*.enum.ts',
	'src/shared/ui/theme-preference.server.ts',
	'src/shared/ui/theme-preference.server-fn.ts',
	// UI de widgets não coberta por RTL seletivo
	'src/components/widgets/app-shell/**/index.tsx',
	'src/components/widgets/app-shell/**/*.styles.ts',
	'src/components/widgets/data-table/DataTableColumn/**',
	'src/components/widgets/data-table/DataTableToolbar/**',
	'src/components/widgets/data-table/DataTablePaginator/**',
	'src/components/widgets/data-table/DataTableMobileList/**',
	'src/components/widgets/data-table/DataTableMobileSort/**',
];

export default defineConfig({
	resolve: { tsconfigPaths: true },
	test: {
		environment: 'jsdom',
		globals: true,
		setupFiles: ['src/test/setup.ts'],
		include: ['src/**/*.{test,spec}.{ts,tsx}'],
		clearMocks: true,
		restoreMocks: true,
		coverage: {
			provider: 'v8',
			reporter: ['text', 'text-summary', 'html', 'lcov'],
			reportsDirectory: './coverage',
			include: unitCoverageInclude,
			exclude: unitCoverageExclude,
		},
	},
});
