import type {QueryClient} from '@tanstack/react-query';

import type PlatformSettingsDto from '../model/platform-settings.dto';
import type {PlatformSettingsUpdateRequest} from '../model/platform-settings.schema';
import {getPlatformSettings, PLATFORM_SETTINGS_QUERY_KEY, updatePlatformSettings,} from './platform-settings.server-fn';

export function getConfiguracoesQueryKey() {
	return [PLATFORM_SETTINGS_QUERY_KEY] as const;
}

export async function fetchPlatformSettings(): Promise<PlatformSettingsDto> {
	return getPlatformSettings();
}

export async function savePlatformSettings(
	data: PlatformSettingsUpdateRequest,
): Promise<PlatformSettingsDto> {
	return updatePlatformSettings({
		data: {
			...data,
			logoUrl: data.logoUrl ?? undefined,
		},
	});
}

export async function invalidatePlatformSettings(queryClient: QueryClient) {
	await queryClient.invalidateQueries({
		queryKey: getConfiguracoesQueryKey(),
	});
}
