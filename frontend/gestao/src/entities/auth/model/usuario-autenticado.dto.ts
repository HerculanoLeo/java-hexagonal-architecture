import type {Status} from '@shared/model/status.enum';
import type GrupoDto from '@entities/grupo/model/grupo.dto';

type UsuarioAutenticadoDto = {
	email?: string;
	grupo?: GrupoDto;
	id: string;
	nome: string;
	status: Status;
};

export type { UsuarioAutenticadoDto as default };
