import {useForm} from '@tanstack/react-form';
import {Search} from 'lucide-react';

import Form from '@components/forms';
import FormButtonSubmit from '@components/forms/FormButtonSubmit';
import FormInputText from '@components/forms/FormInputText';
import {Button} from '@components/ui/button';
import type {GrupoSearchFormValues, GrupoSearchRequest,} from '@entities/grupo';
import {grupoSearchFormSchema, parseGrupoSearchForm} from '@entities/grupo';

const defaultValues: GrupoSearchFormValues = {
	nome: '',
};

type GrupoSearchFormProps = {
	onSearch: (filters: GrupoSearchRequest) => void;
};

export function GrupoSearchForm({ onSearch }: GrupoSearchFormProps) {
	const form = useForm({
		defaultValues,
		validators: {
			onSubmit: grupoSearchFormSchema as any,
		},
		onSubmit: async ({ value }) => {
			onSearch(parseGrupoSearchForm(value));
		},
	});

	function clearFilters() {
		form.reset();
		onSearch({});
	}

	return (
		<Form
			form={form}
			className="grid gap-3 md:grid-cols-[1fr_auto_auto] md:items-end"
		>
			<FormInputText name="nome" label="Nome" placeholder="Nome" />
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
