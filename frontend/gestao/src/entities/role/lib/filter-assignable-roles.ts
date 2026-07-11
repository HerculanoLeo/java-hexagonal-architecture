import {SISTEMAS} from '@shared/auth/auth-permissions.enum';

import type RoleDto from './role.dto';

/**
 * Roles atribuíveis a grupos. `admin-sistemas` é exclusiva do usuário principal
 * (direto no Keycloak) e não deve ser concedida via grupo.
 */
export function filterAssignableSistemaRoles(roles: RoleDto[]): RoleDto[] {
	return roles.filter((role) => role.nome !== SISTEMAS.ADMIN);
}
