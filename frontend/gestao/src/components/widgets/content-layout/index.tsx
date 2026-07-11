import {Link} from '@tanstack/react-router';
import {ChevronRight} from 'lucide-react';

import {usePageTitle} from '@components/widgets/app-shell/use-page-title';
import {cn} from '@shared/ui/utils';

import type {PropsWithChildren} from 'react';

export type ContentBreadcrumbItem = {
	label: string;
	to?: string;
};

type ContentLayoutProps = PropsWithChildren<{
	breadcrumbs?: ContentBreadcrumbItem[];
	maxWidth?: 'default' | 'full';
	pageTitle?: string;
}>;

export function ContentLayout({
	breadcrumbs = [],
	children,
	maxWidth = 'default',
	pageTitle,
}: ContentLayoutProps) {
	usePageTitle(pageTitle ?? breadcrumbs.at(-1)?.label);

	return (
		<div
			className={cn(
				'mx-auto w-full space-y-6',
				maxWidth === 'default' && 'max-w-7xl',
			)}
		>
			<ContentBreadcrumb items={breadcrumbs} />
			{children}
		</div>
	);
}

function ContentBreadcrumb({ items }: { items: ContentBreadcrumbItem[] }) {
	if (items.length === 0) {
		return null;
	}

	return (
		<nav aria-label="Breadcrumb">
			<ol className="flex min-w-0 flex-wrap items-center gap-2 text-sm">
				{items.map((item, index) => {
					const isLast = index === items.length - 1;

					return (
						<li
							key={`${item.label}-${index}`}
							className="flex min-w-0 items-center gap-2"
						>
							<BreadcrumbLabel item={item} isLast={isLast} />
							{!isLast && (
								<ChevronRight className="size-4 shrink-0 text-muted-foreground/70" />
							)}
						</li>
					);
				})}
			</ol>
		</nav>
	);
}

function BreadcrumbLabel({
	item,
	isLast,
}: {
	item: ContentBreadcrumbItem;
	isLast: boolean;
}) {
	const className = isLast
		? 'block truncate font-medium text-foreground'
		: 'block truncate text-muted-foreground transition-colors hover:text-foreground';

	if (item.to && !isLast) {
		return (
			<Link to={item.to} className={className}>
				{item.label}
			</Link>
		);
	}

	return <span className={className}>{item.label}</span>;
}
