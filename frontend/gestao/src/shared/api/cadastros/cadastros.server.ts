import {getRequestHeaders} from '@tanstack/react-start/server';

import {env} from '#/env.ts';
import {getSessionAccessToken} from '@shared/auth/auth-token.server.ts';
import type {ApiErrorResponse} from '@shared/api/api-error.type.ts';
import {ApiHttpError} from '@shared/api/api-error.type.ts';

type ServerOptions = Omit<RequestInit, 'body' | 'headers'> & {
	body?: BodyInit | Record<string, unknown> | null;
	headers?: HeadersInit;
	params?: Record<string, unknown>;
};

export async function fetchCadastros<TResponse>(
	path: string,
	options: ServerOptions = {},
): Promise<TResponse> {
	const requestHeaders = getRequestHeaders();
	const authToken = await getSessionAccessToken(requestHeaders);

	if (!authToken) {
		throw new ApiHttpError('Usuário não autenticado.', 401, {
			message: 'Usuário não autenticado.',
			timestamp: new Date().toISOString(),
		});
	}

	const { params, ...requestOptions } = options;
	let response: Response;

	try {
		response = await fetch(getCadastrosBackendUrl(path, params), {
			...requestOptions,
			body: getRequestBody(options.body),
			headers: getRequestHeadersForBackend(options, authToken.accessToken),
		});
	} catch {
		throw new ApiHttpError(
			'Não foi possível conectar ao serviço de cadastros.',
			503,
			{
				message: 'Não foi possível conectar ao serviço de cadastros.',
				timestamp: new Date().toISOString(),
			},
		);
	}

	if (!response.ok) {
		const payload = await parseJson<ApiErrorResponse>(response);

		throw new ApiHttpError(
			payload?.message ?? 'Não foi possível concluir a operação.',
			response.status,
			payload,
		);
	}

	if (response.status === 204 || response.status === 202) {
		return undefined as TResponse;
	}

	return (await parseJson<TResponse>(response)) as TResponse;
}

function getCadastrosBackendUrl(
	path: string,
	params?: ServerOptions['params'],
) {
	const normalizedBaseUrl = env.CADASTROS_URL.replace(/\/+$/, '');
	const normalizedPath = path.replace(/^\/+/, '');
	const url = new URL(`${normalizedBaseUrl}/${normalizedPath}`);

	appendSearchParams(url.searchParams, params);

	return url.toString();
}

function appendSearchParams(
	searchParams: URLSearchParams,
	params?: ServerOptions['params'],
) {
	if (!params) {
		return;
	}

	for (const [key, value] of Object.entries(params)) {
		appendSearchParam(searchParams, key, value);
	}
}

function appendSearchParam(
	searchParams: URLSearchParams,
	key: string,
	value: unknown,
) {
	if (value === null || value === undefined) {
		return;
	}

	if (Array.isArray(value)) {
		for (const item of value) {
			appendSearchParam(searchParams, key, item);
		}
		return;
	}

	const normalizedValue =
		value instanceof Date ? value.toISOString() : String(value).trim();

	if (!normalizedValue) {
		return;
	}

	searchParams.append(key, normalizedValue);
}

function getRequestBody(body: ServerOptions['body']) {
	if (!body) {
		return undefined;
	}

	if (isBodyInit(body)) {
		return body;
	}

	return JSON.stringify(body);
}

function getRequestHeadersForBackend(
	options: ServerOptions,
	accessToken: string,
) {
	const headers = new Headers(options.headers);

	headers.set('authorization', `Bearer ${accessToken}`);

	if (
		options.body &&
		!isBodyInit(options.body) &&
		!headers.has('content-type')
	) {
		headers.set('content-type', 'application/json');
	}

	return headers;
}

async function parseJson<TResponse>(response: Response) {
	const text = await response.text();

	if (!text) {
		return undefined;
	}

	try {
		return JSON.parse(text) as TResponse;
	} catch {
		return undefined;
	}
}

function isBodyInit(body: ServerOptions['body']): body is BodyInit {
	return (
		typeof body === 'string' ||
		body instanceof Blob ||
		body instanceof FormData ||
		body instanceof URLSearchParams ||
		body instanceof ArrayBuffer ||
		ArrayBuffer.isView(body) ||
		body instanceof ReadableStream
	);
}
