// @ts-check

import {tanstackConfig} from '@tanstack/eslint-config';
import boundaries from 'eslint-plugin-boundaries';

export default [
	...tanstackConfig,
	{
		plugins: { boundaries },
		settings: {
			'boundaries/elements': [
				{ type: 'app', pattern: 'src/routes/**' },
				{ type: 'app', pattern: 'src/configurations/**' },
				{ type: 'pages', pattern: 'src/components/pages/**' },
				{ type: 'widgets', pattern: 'src/components/widgets/**' },
				{ type: 'features', pattern: 'src/components/features/**' },
				{ type: 'entities', pattern: 'src/entities/**' },
				{ type: 'shared', pattern: 'src/shared/**' },
				{ type: 'shared', pattern: 'src/components/ui/**' },
				{ type: 'shared', pattern: 'src/components/forms/**' },
				{ type: 'generated', pattern: 'src/generated/**' },
			],
		},
		rules: {
			'import/no-cycle': 'warn',
			'import/order': 'off',
			'sort-imports': 'off',
			'@typescript-eslint/array-type': 'off',
			'@typescript-eslint/require-await': 'off',
			'pnpm/json-enforce-catalog': 'off',
			'boundaries/dependencies': [
				'error',
				{
					default: 'disallow',
					policies: [
						{
							from: { element: { types: 'shared' } },
							allow: {
								to: {
									element: {
										types: { anyOf: ['shared', 'entities', 'generated'] },
									},
								},
							},
						},
						{
							from: { element: { types: 'entities' } },
							allow: {
								to: {
									element: {
										types: {
											anyOf: ['entities', 'shared', 'generated'],
										},
									},
								},
							},
						},
						{
							from: { element: { types: 'features' } },
							allow: {
								to: {
									element: {
										types: {
											anyOf: ['features', 'entities', 'shared', 'widgets'],
										},
									},
								},
							},
						},
						{
							from: { element: { types: 'widgets' } },
							allow: {
								to: {
									element: {
										types: {
											anyOf: ['widgets', 'features', 'entities', 'shared'],
										},
									},
								},
							},
						},
						{
							from: { element: { types: 'pages' } },
							allow: {
								to: {
									element: {
										types: {
											anyOf: [
												'pages',
												'widgets',
												'features',
												'entities',
												'shared',
											],
										},
									},
								},
							},
						},
						{
							from: { element: { types: 'app' } },
							allow: {
								to: {
									element: {
										types: {
											anyOf: [
												'app',
												'pages',
												'widgets',
												'features',
												'entities',
												'shared',
												'generated',
											],
										},
									},
								},
							},
						},
					],
				},
			],
		},
	},
	{
		ignores: [
			'eslint.config.js',
			'prettier.config.js',
			'.output/**',
			'.nitro/**',
			'src/generated/prisma/**',
		],
	},
];
