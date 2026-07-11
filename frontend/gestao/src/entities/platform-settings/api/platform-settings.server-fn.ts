import {createServerFn} from '@tanstack/react-start';

import {SISTEMAS} from '@shared/auth/auth-permissions.enum';
import {hasRole} from '@shared/auth/auth-permissions';
import type PlatformSettingsDto from '../model/platform-settings.dto';
import {PLATFORM_SETTINGS_ID} from '../lib/platform-settings.defaults';
import {platformSettingsFormSchema} from '../model/platform-settings.schema';

export const PLATFORM_SETTINGS_QUERY_KEY = 'platform-settings';

export const getPlatformSettings = createServerFn({ method: 'GET' }).handler(
	async (): Promise<PlatformSettingsDto> => {
		const { loadPlatformSettings } =
			await import('./platform-settings.repository');

		return loadPlatformSettings();
	},
);

export const updatePlatformSettings = createServerFn({ method: 'POST' })
	.validator(platformSettingsFormSchema)
	.handler(async ({ data }): Promise<PlatformSettingsDto> => {
		const { requireAuthenticatedContextValue } =
			await import('@shared/auth/auth.session.server');
		const authContext = await requireAuthenticatedContextValue();

		if (!hasRole(authContext, { roles: [SISTEMAS.CONFIGURACAO] })) {
			throw new Error('Unauthorized');
		}

		const {
			mapPlatformSettingsRecord,
			setPlatformSettingsCache,
			toPlatformSettingsRecord,
		} = await import('./platform-settings.repository');
		const { prismaClient } = await import('@shared/database/prisma-client');

		if (!prismaClient) {
			throw new Error(
				'Banco de dados não configurado. Defina DATABASE_URL para salvar as configurações.',
			);
		}

		const payload = toPlatformSettingsRecord({
			...data,
			id: PLATFORM_SETTINGS_ID,
			logoUrl:
				data.logoSource === 'external' ? (data.logoUrl?.trim() ?? null) : null,
		});

		const record = await prismaClient.platformSettings.upsert({
			create: payload,
			update: payload,
			where: { id: PLATFORM_SETTINGS_ID },
		});

		const settings = mapPlatformSettingsRecord(record);
		setPlatformSettingsCache(settings);

		return settings;
	});
