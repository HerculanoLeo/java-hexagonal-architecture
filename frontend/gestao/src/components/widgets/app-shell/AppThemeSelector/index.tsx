import {Monitor, Moon, Sun} from 'lucide-react';
import {useMemo, useSyncExternalStore} from 'react';
import {getRouteApi} from '@tanstack/react-router';

import {Button} from '@components/ui/button';
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuRadioGroup,
    DropdownMenuRadioItem,
    DropdownMenuTrigger,
} from '@components/ui/dropdown-menu';
import type {ThemePreference} from '@shared/ui/theme-preference.utils';
import {
    getThemePreferenceSnapshot,
    setThemePreference,
    subscribeToThemePreference,
} from '@shared/ui/theme-preference.utils';

const themeOptions: Array<{
	icon: typeof Sun;
	label: string;
	value: ThemePreference;
}> = [
	{
		icon: Sun,
		label: 'Claro',
		value: 'light',
	},
	{
		icon: Moon,
		label: 'Escuro',
		value: 'dark',
	},
	{
		icon: Monitor,
		label: 'Sistema',
		value: 'system',
	},
];

const rootRoute = getRouteApi('__root__');

export function AppThemeSelector() {
	const { themePreference: serverThemePreference } = rootRoute.useLoaderData();
	const theme = useSyncExternalStore(
		subscribeToThemePreference,
		getThemePreferenceSnapshot,
		() => serverThemePreference,
	);

	const selectedOption = useMemo(
		() =>
			themeOptions.find((option) => option.value === theme) ?? themeOptions[2],
		[theme],
	);
	const Icon = selectedOption.icon;

	return (
		<DropdownMenu>
			<DropdownMenuTrigger asChild>
				<Button
					variant="ghost"
					size="icon-sm"
					aria-label={`Tema: ${selectedOption.label}`}
				>
					<Icon className="size-4" />
				</Button>
			</DropdownMenuTrigger>

			<DropdownMenuContent align="end" sideOffset={10} className="min-w-40">
				<DropdownMenuRadioGroup
					value={theme}
					onValueChange={(value) =>
						setThemePreference(value as ThemePreference)
					}
				>
					{themeOptions.map((option) => {
						const OptionIcon = option.icon;

						return (
							<DropdownMenuRadioItem key={option.value} value={option.value}>
								<OptionIcon className="size-4" />
								{option.label}
							</DropdownMenuRadioItem>
						);
					})}
				</DropdownMenuRadioGroup>
			</DropdownMenuContent>
		</DropdownMenu>
	);
}
