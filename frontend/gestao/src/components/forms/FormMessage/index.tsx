import {AlertCircle} from 'lucide-react';

import {useFormContext} from '@components/forms';

export default function FormMessage() {
	const { formApi } = useFormContext();

	return (
		<formApi.Subscribe
			selector={(state) => [state.errors]}
			children={([errors]) => {
				if (errors.length === 0) {
					return null;
				}

				return (
					<div className="flex items-start gap-3 rounded-lg border border-destructive/30 bg-destructive/10 px-4 py-3 text-sm text-destructive">
						<AlertCircle className="mt-0.5 size-4 shrink-0" />
						<div>
							<p className="font-semibold">Formulário</p>
							<p>Há dados incorretos no formulário.</p>
						</div>
					</div>
				);
			}}
		/>
	);
}
