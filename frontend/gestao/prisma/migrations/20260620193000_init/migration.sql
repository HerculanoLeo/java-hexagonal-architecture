-- CreateSchema
CREATE SCHEMA IF NOT EXISTS "frontend";

-- CreateTable
CREATE TABLE "frontend"."user" (
    "id" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "email" TEXT NOT NULL,
    "emailVerified" BOOLEAN NOT NULL DEFAULT false,
    "image" TEXT,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "user_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "frontend"."session" (
    "id" TEXT NOT NULL,
    "expiresAt" TIMESTAMP(3) NOT NULL,
    "token" TEXT NOT NULL,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,
    "ipAddress" TEXT,
    "userAgent" TEXT,
    "userId" TEXT NOT NULL,

    CONSTRAINT "session_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "frontend"."account" (
    "id" TEXT NOT NULL,
    "accountId" TEXT NOT NULL,
    "providerId" TEXT NOT NULL,
    "userId" TEXT NOT NULL,
    "accessToken" TEXT,
    "refreshToken" TEXT,
    "idToken" TEXT,
    "accessTokenExpiresAt" TIMESTAMP(3),
    "refreshTokenExpiresAt" TIMESTAMP(3),
    "scope" TEXT,
    "password" TEXT,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "account_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "frontend"."verification" (
    "id" TEXT NOT NULL,
    "identifier" TEXT NOT NULL,
    "value" TEXT NOT NULL,
    "expiresAt" TIMESTAMP(3) NOT NULL,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "verification_pkey" PRIMARY KEY ("id")
);

-- CreateEnum
CREATE TYPE "frontend"."LogoSource" AS ENUM ('static', 'external');

-- CreateEnum
CREATE TYPE "frontend"."MenuLayout" AS ENUM ('hybrid', 'sidebar');

-- CreateTable
CREATE TABLE "frontend"."platform_settings" (
    "id" TEXT NOT NULL DEFAULT 'default',
    "projectName" TEXT NOT NULL,
    "appTitle" TEXT NOT NULL,
    "menuLayout" "frontend"."MenuLayout" NOT NULL DEFAULT 'hybrid',
    "logoSource" "frontend"."LogoSource" NOT NULL DEFAULT 'static',
    "logoUrl" TEXT,
    "buttonSaveBg" TEXT NOT NULL,
    "buttonSaveHover" TEXT NOT NULL,
    "buttonActivateBg" TEXT NOT NULL,
    "buttonActivateHover" TEXT NOT NULL,
    "buttonBackBg" TEXT NOT NULL,
    "buttonBackHover" TEXT NOT NULL,
    "buttonBackForeground" TEXT NOT NULL,
    "buttonDeleteBg" TEXT NOT NULL,
    "buttonDeleteHover" TEXT NOT NULL,
    "shellGradientFromLight" TEXT NOT NULL,
    "shellGradientToLight" TEXT NOT NULL,
    "shellGradientFromDark" TEXT NOT NULL,
    "shellGradientToDark" TEXT NOT NULL,
    "primaryBgLight" TEXT NOT NULL,
    "primaryForegroundLight" TEXT NOT NULL,
    "secondaryBgLight" TEXT NOT NULL,
    "secondaryForegroundLight" TEXT NOT NULL,
    "primaryBgDark" TEXT NOT NULL,
    "primaryForegroundDark" TEXT NOT NULL,
    "secondaryBgDark" TEXT NOT NULL,
    "secondaryForegroundDark" TEXT NOT NULL,
    "shellForegroundLight" TEXT NOT NULL,
    "shellMutedForegroundLight" TEXT NOT NULL,
    "shellForegroundDark" TEXT NOT NULL,
    "shellMutedForegroundDark" TEXT NOT NULL,
    "menuTitleActiveForegroundLight" TEXT NOT NULL,
    "menuItemHoverBgLight" TEXT NOT NULL,
    "menuItemActiveBgLight" TEXT NOT NULL,
    "menuItemActiveForegroundLight" TEXT NOT NULL,
    "menuSubmenuHoverBgLight" TEXT NOT NULL,
    "menuSubmenuActiveBgLight" TEXT NOT NULL,
    "menuSubmenuActiveForegroundLight" TEXT NOT NULL,
    "menuTitleActiveForegroundDark" TEXT NOT NULL,
    "menuItemHoverBgDark" TEXT NOT NULL,
    "menuItemActiveBgDark" TEXT NOT NULL,
    "menuItemActiveForegroundDark" TEXT NOT NULL,
    "menuSubmenuHoverBgDark" TEXT NOT NULL,
    "menuSubmenuActiveBgDark" TEXT NOT NULL,
    "menuSubmenuActiveForegroundDark" TEXT NOT NULL,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "platform_settings_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "frontend"."menus" (
    "id" TEXT NOT NULL,
    "parentId" TEXT,
    "label" TEXT NOT NULL,
    "description" TEXT,
    "icon" TEXT,
    "sortOrder" INTEGER NOT NULL DEFAULT 0,
    "enabled" BOOLEAN NOT NULL DEFAULT true,
    "roles" TEXT[] DEFAULT ARRAY[]::TEXT[],
    "route" TEXT,
    "externalUrl" TEXT,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "menus_pkey" PRIMARY KEY ("id")
);

-- CreateIndex
CREATE UNIQUE INDEX "user_email_key" ON "frontend"."user"("email");

-- CreateIndex
CREATE INDEX "session_userId_idx" ON "frontend"."session"("userId");

-- CreateIndex
CREATE UNIQUE INDEX "session_token_key" ON "frontend"."session"("token");

-- CreateIndex
CREATE INDEX "account_userId_idx" ON "frontend"."account"("userId");

-- CreateIndex
CREATE INDEX "verification_identifier_idx" ON "frontend"."verification"("identifier");

-- CreateIndex
CREATE INDEX "menus_parentId_sortOrder_idx" ON "frontend"."menus"("parentId", "sortOrder");

-- AddForeignKey
ALTER TABLE "frontend"."session" ADD CONSTRAINT "session_userId_fkey" FOREIGN KEY ("userId") REFERENCES "frontend"."user"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "frontend"."account" ADD CONSTRAINT "account_userId_fkey" FOREIGN KEY ("userId") REFERENCES "frontend"."user"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "frontend"."menus" ADD CONSTRAINT "menus_parentId_fkey" FOREIGN KEY ("parentId") REFERENCES "frontend"."menus"("id") ON DELETE CASCADE ON UPDATE CASCADE;
