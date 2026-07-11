import type GrupoDto from '@entities/grupo/model/grupo.dto';
import type {Status} from '@shared/model/status.enum';

type MeusDadosProfileDto = {
	email: string;
	grupo?: GrupoDto;
	id: string;
	nome: string;
	status: Status;
};

export type { MeusDadosProfileDto as default };
