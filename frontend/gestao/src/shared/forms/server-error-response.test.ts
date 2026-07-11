import {vi} from 'vitest';

import {ApiHttpError} from '@shared/api/api-error.type';
import {appToast} from '@shared/ui/toast';

import {getServerErrorMessage, parseServerErrorResponse,} from './server-error-response';

vi.mock('@shared/ui/toast', () => ({
	appToast: {
		atencao: vi.fn(),
		falha: vi.fn(),
	},
}));

describe('getServerErrorMessage', () => {
	it('returns payload message from ApiHttpError', () => {
		expect(
			getServerErrorMessage(
				new ApiHttpError('HTTP error', 500, { message: 'Erro no servidor' }),
			),
		).toBe('Erro no servidor');
	});

	it('returns Error message for generic errors', () => {
		expect(getServerErrorMessage(new Error('Falha inesperada'))).toBe(
			'Falha inesperada',
		);
	});

	it('returns a default message for unknown errors', () => {
		expect(getServerErrorMessage('oops')).toBe(
			'Não foi possível concluir a operação. Tente novamente.',
		);
	});
});

describe('parseServerErrorResponse', () => {
	beforeEach(() => {
		vi.clearAllMocks();
	});

	it('applies field errors to the form and shows attention toast', () => {
		const setFieldMeta = vi.fn(
			(_field: string, updater: (meta: { errorMap: object; errorSourceMap: object }) => object) =>
				updater({ errorMap: {}, errorSourceMap: {} }),
		);

		parseServerErrorResponse(
			new ApiHttpError('Validation failed', 400, {
				fieldErrors: [{ field: 'nome', message: 'Obrigatório' }],
			}),
			{
				formApi: { setFieldMeta },
			},
		);

		expect(setFieldMeta).toHaveBeenCalledWith('nome', expect.any(Function));
		expect(appToast.atencao).toHaveBeenCalledWith(
			'Há dados incorretos no formulário.',
		);
	});

	it('applies sentinel-flow errors payload to the form', () => {
		const setFieldMeta = vi.fn(
			(_field: string, updater: (meta: { errorMap: object; errorSourceMap: object }) => object) =>
				updater({ errorMap: {}, errorSourceMap: {} }),
		);

		parseServerErrorResponse(
			new ApiHttpError('there are invalid fields', 400, {
				message: 'there are invalid fields',
				errors: [{ fieldName: 'email', messages: ['e-mail inválido'] }],
			}),
			{
				formApi: { setFieldMeta },
			},
		);

		expect(setFieldMeta).toHaveBeenCalledWith('email', expect.any(Function));
		expect(appToast.atencao).toHaveBeenCalledWith(
			'Há dados incorretos no formulário.',
		);
	});

	it('shows attention toast for validation errors without field mapping', () => {
		parseServerErrorResponse(
			new ApiHttpError('Validation failed', 400, {
				message: 'Dados inválidos',
			}),
		);

		expect(appToast.atencao).toHaveBeenCalledWith('Dados inválidos');
	});

	it('shows failure toast for non-validation errors', () => {
		parseServerErrorResponse(new ApiHttpError('Server error', 500));

		expect(appToast.falha).toHaveBeenCalledWith({
			title: 'Não foi possível concluir a operação',
			description: 'Server error',
		});
	});
});
