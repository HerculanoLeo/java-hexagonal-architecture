# Menu

## Classificação

- **DDD:** Generic (UI config)
- **Camada:** BFF

## Responsabilidade

Árvore de navegação do AppShell (sidebar/topbar), filtrada por roles do usuário. Fallback para nós padrão quando DB
vazio.

## Regras de negócio

- Montagem e filtragem **server-side** via `createServerFn`.
- Nós padrão em `DEFAULT_MENU_NODES` (Administração → Acesso, Segurança, Configurações).
- Ícones lucide opcionais; rotas internas e links externos.
- Layout sidebar vs hybrid controlado por `platform-settings`.

## Onde encontrar

| Artefato     | Path                                                |
|--------------|-----------------------------------------------------|
| Entity       | `frontend/src/entities/menu/`                       |
| Server fn    | `frontend/src/entities/menu/api/menu.server-fn.ts`  |
| Repository   | `frontend/src/entities/menu/api/menu.repository.ts` |
| Defaults     | `frontend/src/entities/menu/lib/menu.defaults.ts`   |
| Tree utils   | `frontend/src/entities/menu/lib/menu-tree.utils.ts` |
| Prisma model | `frontend/prisma/schema.prisma` → `Menu`            |
| AppShell     | `frontend/src/components/widgets/app-shell/`        |

## Contratos

- `MenuDto`, `MenuItemDto`, `MenuDefaultNode`
- `getMenus` server function

## Dependências

- Auth (roles), platform-settings (layout), rotas de outros domínios

## Testes

- `menu-tree.utils.test.ts`, `menu.defaults.test.ts`, `menu.cache.test.ts`

## Implementação futura

- CRUD admin de menus via UI
- Menus por tenant
