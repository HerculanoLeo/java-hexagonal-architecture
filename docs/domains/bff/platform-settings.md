# Platform Settings

## Classificação

- **DDD:** Generic (UI config)
- **Camada:** BFF

## Responsabilidade

Branding white-label: nome do projeto, logo, cores do tema, layout do menu (sidebar/hybrid), título da aplicação.

## Regras de negócio

- Configuração carregada no root loader (`getLayoutConfig`) para CSS/branding síncrono.
- Atualização exige role `SISTEMAS.CONFIGURACAO`.
- Logo: estática ou URL externa (`LogoSource`).

## Onde encontrar

| Artefato       | Path                                                           |
|----------------|----------------------------------------------------------------|
| Entity         | `frontend/gestao/src/entities/platform-settings/`                     |
| Server fn      | `platform-settings.server-fn.ts`, `layout-config.server-fn.ts` |
| Repository     | `platform-settings.repository.ts`                              |
| Branding utils | `lib/layout-branding.utils.ts`                                 |
| Prisma         | `PlatformSettings` model                                       |
| UI             | `frontend/gestao/src/components/pages/administracao/configuracoes/`   |
| Widget         | `frontend/gestao/src/components/widgets/layout-branding/`             |
| Rota           | `/administracao/configuracoes`                                 |

## Contratos

- `PlatformSettingsDto`, `LayoutConfigDto`, `LayoutBrandingDto`
- `platformSettingsFormSchema`

## Dependências

- Auth (guard CONFIGURACAO)
- Consumido por: menu, AppShell, `__root.tsx`

## Testes

- `platform-settings.schema.test.ts`, `layout-branding.utils.test.ts`, `platform-settings.defaults.test.ts`

## Implementação futura

- Settings por tenant/organização
- Preview ao vivo no form de configurações
