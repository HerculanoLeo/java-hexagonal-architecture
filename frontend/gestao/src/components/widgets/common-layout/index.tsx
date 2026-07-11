import type {PropsWithChildren} from 'react';

import {usePageTitle} from '@components/widgets/app-shell/use-page-title';
import {useLayoutBranding} from '@components/widgets/layout-branding';
import {shellChromeClassName, shellMutedForegroundClassName,} from '@shared/ui/theme-colors.utils';
import {cn} from '@shared/ui/utils.ts';

type CommonLayoutProps = PropsWithChildren<{
	title: string;
	description?: string;
	brand?: string;
	className?: string;
}>;

export function CommonLayout({
	title,
	description,
	children,
	brand,
	className,
}: CommonLayoutProps) {
	const { branding } = useLayoutBranding();
	const resolvedBrand = brand ?? branding.projectName;

	usePageTitle(title, branding.appTitle);

	return (
		<div className="grid min-h-screen bg-background text-foreground lg:grid-cols-2">
			<div
				className={cn(
					'relative hidden flex-col justify-between p-10 lg:flex',
					shellChromeClassName,
				)}
			>
				<div className="space-y-3">
					{branding.logoSrc ? (
						<img
							src={branding.logoSrc}
							alt={branding.logoAlt}
							className="max-h-10 max-w-40 object-contain"
						/>
					) : (
						<div className="text-lg font-semibold tracking-tight">
							{resolvedBrand}
						</div>
					)}
				</div>
				<div className="space-y-2">
					<h2 className="text-3xl font-semibold tracking-tight">{title}</h2>
					{description && (
						<p className={cn('text-sm', shellMutedForegroundClassName)}>
							{description}
						</p>
					)}
				</div>
				<p className={cn('text-xs', shellMutedForegroundClassName)}>
					{branding.projectName}
				</p>
			</div>
			<div
				className={cn(
					'flex flex-col items-center justify-center bg-muted/40 p-6 sm:p-10',
					className,
				)}
			>
				<div className="mb-8 w-full max-w-md lg:hidden">
					{branding.logoSrc ? (
						<img
							src={branding.logoSrc}
							alt={branding.logoAlt}
							className="mb-3 max-h-10 max-w-40 object-contain"
						/>
					) : (
						<p className="text-lg font-semibold">{resolvedBrand}</p>
					)}
					<h1 className="mt-2 text-2xl font-semibold tracking-tight">
						{title}
					</h1>
					{description && (
						<p className="mt-1 text-sm text-muted-foreground">{description}</p>
					)}
				</div>
				<div className="w-full max-w-md">{children}</div>
			</div>
		</div>
	);
}
