export type DataTablePageRequest = {
	first: number;
	filters?: Record<string, unknown>;
	rows: number;
	sortField?: string;
	sortOrder?: 'asc' | 'desc';
};

export type DataTablePageResponse<T> = {
	content: T[];
	totalRecords: number;
};
