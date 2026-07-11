import {useForm} from '@tanstack/react-form';
import {Search} from 'lucide-react';

import Form from '@components/forms';
import FormButtonSubmit from '@components/forms/FormButtonSubmit';
import FormCalendar from '@components/forms/FormCalendar';
import FormDropdown from '@components/forms/FormDropdown';
import FormInputText from '@components/forms/FormInputText';
import {Button} from '@components/ui/button';
import type {HistoricoLoginSearchFormValues, HistoricoLoginSearchRequest,} from '@entities/historico-login';
import {historicoLoginSearchFormSchema, parseHistoricoLoginSearchForm,} from '@entities/historico-login';

const defaultValues: HistoricoLoginSearchFormValues = {
	dataEventoDe: undefined,
	dataEventoAte: undefined,
	email: '',
	tipo: '',
	relacionadoId: '',
};

type HistoricoLoginSearchFormProps = {
	onSearch: (filters: HistoricoLoginSearchRequest) => void;
};

export function HistoricoLoginSearchForm({
	onSearch,
}: HistoricoLoginSearchFormProps) {
	const form = useForm({
		defaultValues,
		validators: {
			onSubmit: historicoLoginSearchFormSchema as any,
		},
		onSubmit: async ({ value }) => {
			onSearch(parseHistoricoLoginSearchForm(value));
		},
	});

	function clearFilters() {
		form.reset();
		onSearch({});
	}

	return (
		<Form
			form={form}
			className="grid gap-3 md:grid-cols-[1fr_1fr_1fr_140px_1fr_auto_auto] md:items-end"
		>
			<FormCalendar name="dataEventoDe" label="De" />
			<FormCalendar name="dataEventoAte" label="Até" />
			<FormInputText
				name="email"
				label="E-mail"
				type="email"
				placeholder="E-mail"
			/>
			<FormDropdown
				name="tipo"
				label="Tipo"
				placeholder="Tipo"
				options={[
					{ label: 'Todos', value: '' },
					{ label: 'Sistema (ST)', value: 'ST' },
					{ label: 'Cliente (CL)', value: 'CL' },
				]}
			/>
			<FormInputText
				name="relacionadoId"
				label="Tenant (id relacionado)"
				placeholder="UUID do tenant"
			/>
			<FormButtonSubmit>
				<Search className="size-4" />
				Buscar
			</FormButtonSubmit>
			<Button type="button" variant="outline" onClick={clearFilters}>
				Limpar
			</Button>
		</Form>
	);
}
