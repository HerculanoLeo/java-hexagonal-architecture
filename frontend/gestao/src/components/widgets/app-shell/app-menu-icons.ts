import type {LucideIcon} from 'lucide-react';
import {Activity, Building2, History, Settings, Shield, ShieldCheck, UserRound, Users,} from 'lucide-react';

const menuIconByName: Record<string, LucideIcon> = {
	Activity,
	Building2,
	History,
	Settings,
	Shield,
	ShieldCheck,
	UserRound,
	Users,
};

export function getMenuIcon(icon?: string) {
	if (!icon) {
		return undefined;
	}

	return menuIconByName[icon];
}
