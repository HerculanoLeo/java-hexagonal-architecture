import type {Status} from '@shared/model/status.enum';

import type UsuarioGrupoDto from './usuario-grupo.dto';

type UsuarioDto = {
	id: string;
	email: string;
	main?: boolean;
	nome: string;
	status: Status;
	grupo?: UsuarioGrupoDto;
};

export type { UsuarioDto as default };
