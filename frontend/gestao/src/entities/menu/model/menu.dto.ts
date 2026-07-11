export type MenuIconDto = string;

export type MenuItemDto = {
	id: string;
	label: string;
	description?: string;
	icon?: MenuIconDto;
	roles?: string[];
	route?: string;
	externalUrl?: string;
	items?: MenuItemDto[];
};

export type MenuDto = {
	id: string;
	label: string;
	description?: string;
	icon?: MenuIconDto;
	roles?: string[];
	items: MenuItemDto[];
};
