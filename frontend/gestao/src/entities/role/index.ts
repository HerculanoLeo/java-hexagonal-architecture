export type { default as RoleDto } from './model/role.dto';
export { default as rolesService, ROLES_QUERY_KEY } from './api/role.service';
export { filterAssignableSistemaRoles } from './lib/filter-assignable-roles';
