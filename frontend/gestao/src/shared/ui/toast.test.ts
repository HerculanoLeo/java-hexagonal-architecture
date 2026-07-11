import {toast} from 'sonner';
import {vi} from 'vitest';

import {appToast} from './toast';

vi.mock('sonner', () => ({
	toast: {
		error: vi.fn(),
		info: vi.fn(),
		success: vi.fn(),
		warning: vi.fn(),
		mockImplementation: vi.fn(),
	},
}));

describe('appToast', () => {
	beforeEach(() => {
		vi.clearAllMocks();
	});

	it('shows a warning toast from a string message', () => {
		appToast.atencao('Atenção necessária');

		expect(toast.warning).toHaveBeenCalledWith('Atenção necessária', undefined);
	});

	it('shows an error toast with title and description', () => {
		appToast.falha({
			title: 'Falha ao salvar',
			description: 'Tente novamente',
		});

		expect(toast.error).toHaveBeenCalledWith('Falha ao salvar', {
			description: 'Tente novamente',
		});
	});

	it('merges extra options when title is provided as an object', () => {
		appToast.sucesso(
			{
				title: 'Salvo com sucesso',
			},
			{ duration: 3000 },
		);

		expect(toast.success).toHaveBeenCalledWith('Salvo com sucesso', {
			duration: 3000,
		});
	});
});
