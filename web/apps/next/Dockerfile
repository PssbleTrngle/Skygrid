FROM node:20 AS base
ENV PNPM_HOME="/pnpm"
ENV PATH="$PNPM_HOME:$PATH"
RUN corepack enable
ENV CI=true

FROM base AS builder
#RUN apk add --no-cache libc6-compat
#RUN apk update
# Set working directory
WORKDIR /app
RUN pnpm add -g turbo
COPY . .
RUN turbo prune --scope=next --docker

# Add lockfile and package.json's of isolated subworkspace
FROM base AS installer

ENV NODE_GYP_FORCE_PYTHON=/usr/local/bin/python3.9

# required dependencies for node-canvas
RUN apt-get update
RUN apt-get install -y software-properties-common build-essential libcairo2-dev libpango1.0-dev libjpeg-dev libgif-dev librsvg2-dev pkg-config python3-launchpadlib

RUN add-apt-repository -y ppa:deadsnakes/ppa
RUN apt-get update
RUN apt-get install -y python3.9
RUN ls /usr/local/bin
RUN whereis python3.9
RUN /usr/local/bin/python3.9 -v

ARG GITHUB_TOKEN
ARG VANILLA_DATA
ARG FORGE_DATA
ARG CURSEFORGE_TOKEN
ARG MODRINTH_TOKEN

ENV GITHUB_TOKEN=$GITHUB_TOKEN
ENV VANILLA_DATA=$VANILLA_DATA
ENV FORGE_DATA=$FORGE_DATA
ENV CURSEFORGE_TOKEN=$CURSEFORGE_TOKEN
ENV MODRINTH_TOKEN=$MODRINTH_TOKEN
WORKDIR /app

# First install the dependencies (as they change less often)
COPY .gitignore .gitignore
COPY --from=builder /app/out/json/ .
COPY --from=builder /app/out/pnpm-lock.yaml ./pnpm-lock.yaml
RUN --mount=type=cache,id=pnpm,target=/pnpm/store pnpm install -r --workspace-root

# Build the project
COPY --from=builder /app/out/full/ .
RUN pnpm turbo run build --filter=next...

FROM node:20-alpine AS runner
WORKDIR /app

# Don't run production as root
RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nextjs
USER nextjs

COPY --from=installer /app/apps/next/next.config.js .
COPY --from=installer /app/apps/next/package.json .

# Automatically leverage output traces to reduce image size
# https://nextjs.org/docs/advanced-features/output-file-tracing
COPY --from=installer --chown=nextjs:nodejs /app/apps/next/.next/standalone ./
COPY --from=installer --chown=nextjs:nodejs /app/apps/next/.next/static ./apps/next/.next/static
COPY --from=installer --chown=nextjs:nodejs /app/apps/next/public ./apps/next/public
COPY --from=installer --chown=nextjs:nodejs /app/apps/next/data ./apps/next/data
COPY --from=installer --chown=nextjs:nodejs /app/apps/next/lang ./apps/next/lang

EXPOSE 3000

# Next.js collects completely anonymous telemetry data about general usage.
# Learn more here: https://nextjs.org/telemetry
# Uncomment the following line in case you want to disable telemetry.
# RUN npx next telemetry disable

CMD ["node", "apps/next/server.js"]