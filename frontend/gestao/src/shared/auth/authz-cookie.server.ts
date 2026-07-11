import {createHmac, timingSafeEqual} from 'node:crypto';
import {deleteCookie, getCookie, setCookie,} from '@tanstack/react-start/server';

import {env} from '@/env';

import type {AuthzCookiePayload} from '@shared/auth/auth-token.types';

const cookiePath = '/';

export function getAuthzCookie() {
	const cookie = getCookie(env.AUTHZ_COOKIE_NAME);

	if (!cookie) {
		return undefined;
	}

	const payload = verifyCookieValue(cookie);

	if (!payload || isExpired(payload)) {
		clearAuthzCookie();
		return undefined;
	}

	return payload;
}

export function setAuthzCookie(payload: AuthzCookiePayload) {
	if (isExpired(payload)) {
		clearAuthzCookie();
		return;
	}

	const nowSeconds = Math.floor(Date.now() / 1000);

	setCookie(env.AUTHZ_COOKIE_NAME, signCookiePayload(payload), {
		httpOnly: true,
		maxAge: payload.exp - nowSeconds,
		path: cookiePath,
		sameSite: 'lax',
		secure: env.NODE_ENV === 'production',
	});
}

export function clearAuthzCookie() {
	deleteCookie(env.AUTHZ_COOKIE_NAME, {
		path: cookiePath,
	});
}

function verifyCookieValue(value: string) {
	const [encodedPayload, signature] = value.split('.');

	if (!encodedPayload || !signature) {
		return undefined;
	}

	const expectedSignature = signValue(encodedPayload);

	if (!safeEqual(signature, expectedSignature)) {
		return undefined;
	}

	try {
		const json = Buffer.from(encodedPayload, 'base64url').toString('utf-8');

		return JSON.parse(json) as AuthzCookiePayload;
	} catch {
		return undefined;
	}
}

function signCookiePayload(payload: AuthzCookiePayload) {
	const encodedPayload = Buffer.from(JSON.stringify(payload)).toString(
		'base64url',
	);

	return `${encodedPayload}.${signValue(encodedPayload)}`;
}

function signValue(value: string) {
	return createHmac('sha256', getCookieSecret())
		.update(value)
		.digest('base64url');
}

function getCookieSecret() {
	if (!env.BETTER_AUTH_SECRET) {
		throw new Error('BETTER_AUTH_SECRET is required to sign authz cookies.');
	}

	return env.BETTER_AUTH_SECRET;
}

function safeEqual(value: string, expectedValue: string) {
	const valueBuffer = Buffer.from(value);
	const expectedValueBuffer = Buffer.from(expectedValue);

	if (valueBuffer.length !== expectedValueBuffer.length) {
		return false;
	}

	return timingSafeEqual(valueBuffer, expectedValueBuffer);
}

function isExpired(payload: AuthzCookiePayload) {
	return payload.exp <= Math.floor(Date.now() / 1000);
}
