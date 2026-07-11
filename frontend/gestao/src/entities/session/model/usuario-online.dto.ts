export type UsuarioOnlineSessionDto = {
	id: string;
	ipAddress: string | null;
	userAgent: string | null;
	createdAt: string;
	expiresAt: string;
	updatedAt: string;
};

export type UsuarioOnlineGroupDto = {
	userId: string;
	userName: string;
	userEmail: string;
	sessionCount: number;
	lastActivityAt: string;
	sessions: UsuarioOnlineSessionDto[];
};
