# Runtime only — artefatos (dist, prisma generate) vêm da pipeline (pnpm build)
FROM node:24-slim

ENV PNPM_HOME="/pnpm"
ENV PATH="$PNPM_HOME/bin:$PATH"
ENV NODE_ENV=production

RUN corepack enable && mkdir -p "$PNPM_HOME"
RUN apt-get update -y && apt-get install -y openssl curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY package.json pnpm-lock.yaml pnpm-workspace.yaml ./
RUN pnpm install --frozen-lockfile --prod
RUN pnpm add -g prisma

COPY dist ./dist
COPY prisma ./prisma
COPY prisma.config.ts ./
COPY src/generated ./src/generated

EXPOSE 3000

HEALTHCHECK --interval=1m --timeout=15s --retries=5 \
    CMD curl --fail http://localhost:3000/health || exit 1

CMD ["pnpm", "start:prod"]
