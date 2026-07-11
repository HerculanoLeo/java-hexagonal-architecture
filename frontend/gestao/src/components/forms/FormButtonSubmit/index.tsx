import {Loader2} from 'lucide-react';

import {Button} from '@components/ui/button';
import {useFormContext} from '@components/forms';

import type {ComponentProps} from 'react';

type Props = ComponentProps<typeof Button> & {
	loading?: boolean;
};

export default function FormButtonSubmit({
	children,
	disabled,
	loading,
	type = 'submit',
	...rest
}: Props) {
	const { formApi } = useFormContext();

	return (
		<formApi.Subscribe
			selector={(state) => [state.canSubmit, state.isSubmitting]}
			children={([canSubmit, isSubmitting]) => (
				<Button type={type} disabled={!canSubmit || disabled} {...rest}>
					{(isSubmitting || loading) && (
						<Loader2 className="size-4 animate-spin" />
					)}
					{children}
				</Button>
			)}
		/>
	);
}
