import {normalizeUsuarioGrupoId, resolveUsuarioGrupoFormValue, USUARIO_SEM_GRUPO_VALUE,} from './usuario-grupo.utils';

describe('usuario-grupo.utils', () => {
	it('normalizes sentinel and blank grupo ids to empty string', () => {
		expect(normalizeUsuarioGrupoId(USUARIO_SEM_GRUPO_VALUE)).toBe('');
		expect(normalizeUsuarioGrupoId('   ')).toBe('');
		expect(normalizeUsuarioGrupoId('grupo-1')).toBe('grupo-1');
	});

	it('uses sentinel when main user has no grupo', () => {
		expect(resolveUsuarioGrupoFormValue(undefined, true)).toBe(
			USUARIO_SEM_GRUPO_VALUE,
		);
		expect(resolveUsuarioGrupoFormValue('grupo-1', true)).toBe('grupo-1');
		expect(resolveUsuarioGrupoFormValue(undefined, false)).toBe('');
	});
});
