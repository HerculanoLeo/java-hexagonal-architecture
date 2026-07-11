import {useForm} from '@tanstack/react-form';
import {Search} from 'lucide-react';

import Form from '@components/forms';
import FormButtonSubmit from '@components/forms/FormButtonSubmit';
import FormDropdown from '@components/forms/FormDropdown';
import FormInputText from '@components/forms/FormInputText';
import {Button} from '@components/ui/button';
import {Status} from '@shared/model/status.enum';
import type {UsuarioSearchFormValues, UsuarioSearchRequest,} from '@entities/usuario';
import {parseUsuarioSearchForm, usuarioSearchFormSchema,} from '@entities/usuario';

const defaultValues: UsuarioSearchFormValues = {
	email: '',
	nome: '',
	status: '',
};

type UsuarioSearchFormProps = {
	onSearch: (filters: UsuarioSearchRequest) => void;
};

export function UsuarioSearchForm({ onSearch }: UsuarioSearchFormProps) {
	const form = useForm({
		defaultValues,
		validators: {
			onSubmit: usuarioSearchFormSchema as any,
		},
		onSubmit: async ({ value }) => {
			onSearch(parseUsuarioSearchForm(value));
		},
	});

	function clearFilters() {
		form.reset();
		onSearch({});
	}

	return (
		<Form
			form={form}
			className="grid gap-3 md:grid-cols-[1fr_1fr_180px_auto_auto] md:items-end"
		>
			<FormInputText name="nome" label="Nome" placeholder="Nome" />
			<FormInputText
				name="email"
				label="E-mail"
				type="email"
				placeholder="E-mail"
			/>
			<FormDropdown
				name="status"
				label="Status"
				placeholder="Status"
				options={[
					{ label: 'Todos', value: '' },
					{ label: 'Ativo', value: Status.ATIVO },
					{ label: 'Inativo', value: Status.INATIVO },
				]}
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
