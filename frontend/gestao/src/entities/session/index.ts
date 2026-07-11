export type {
	UsuarioOnlineGroupDto,
	UsuarioOnlineSessionDto,
} from './model/usuario-online.dto';

export type { SessionDistributedCacheStore } from './lib/session-cache-store.type';

export { revokeAllSessionsSchema } from './model/revoke-all-sessions.schema';
export { revokeSessionSchema } from './model/revoke-session.schema';

export {
	getUsuariosOnline,
	revokeAllUsuarioSessions,
	revokeUsuarioSession,
	USUARIOS_ONLINE_QUERY_KEY,
} from './api/usuarios-online.server-fn';
export {
	loadUsuariosOnline,
	revokeAllUsuarioSessions as revokeAllUsuarioSessionsInRepository,
	revokeUsuarioSession as revokeUsuarioSessionInRepository,
} from './api/usuarios-online.repository';

export {
	fetchUsuariosOnline,
	getUsuariosOnlineErrorMessage,
	getUsuariosOnlineQueryKey,
	invalidateUsuariosOnline,
	revokeAllSessions,
	revokeSession,
} from './api/session.queries';
