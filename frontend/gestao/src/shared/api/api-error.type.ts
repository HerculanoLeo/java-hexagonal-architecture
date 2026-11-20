export type ApiFieldError = {
	field: string;
	message: string;
};

export type ApiErrorResponse = {
	errors?: Array<{
		fieldName: string;
		messages: string[];
	}>;
	fieldErrors?: ApiFieldError[];
	message?: string;
	timestamp?: string;
	/** Correlação com logs do backend (`LogMdc.uid`). */
	uid?: string;
};

export class ApiHttpError extends Error {
	constructor(
		message: string,
		public readonly status: number,
		public readonly payload?: ApiErrorResponse,
	) {
		super(message);
		this.name = 'ApiHttpError';
	}
}
