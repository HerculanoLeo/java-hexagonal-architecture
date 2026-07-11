/** Valor sentinela do select: usuário principal sem grupo (Radix Select não aceita value vazio). */
export const USUARIO_SEM_GRUPO_VALUE = '__none__';

export function normalizeUsuarioGrupoId(grupoId: string): string {
	const trimmed = grupoId.trim();

	if (!trimmed || trimmed === USUARIO_SEM_GRUPO_VALUE) {
		return '';
	}

	return trimmed;
}

export function resolveUsuarioGrupoFormValue(
	grupoId: string | undefined,
	main: boolean,
): string {
	if (grupoId && grupoId.trim()) {
		return grupoId.trim();
	}

	return main ? USUARIO_SEM_GRUPO_VALUE : '';
}
