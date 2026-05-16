"use client";

import { Stack, Typography } from "@mui/material";

import { MainLayout } from "@/layouts/MainLayout";
import { EmptyState } from "@/components/EmptyState";
import { ErrorBanner } from "@/components/ErrorBanner";
import { LoadingSkeleton } from "@/components/LoadingSkeleton";
import { PriorityList } from "@/components/PriorityList";
import { usePriorityInbox } from "@/hooks/usePriorityInbox";

export default function PriorityPage() {
  const { data, isLoading, isError } = usePriorityInbox();

  return (
    <MainLayout>
      <Stack spacing={3}>
        <Typography variant="h3">Priority Inbox</Typography>
        <Typography variant="body1" color="text.secondary">
          Placement, result, and event notifications scored by urgency and recency.
        </Typography>

        {isError && <ErrorBanner message="Unable to load priority inbox." />}

        {isLoading && (
          <Stack spacing={2}>
            <LoadingSkeleton />
            <LoadingSkeleton />
          </Stack>
        )}

        {!isLoading && data && data.length > 0 && <PriorityList items={data} />}

        {!isLoading && data && data.length === 0 && (
          <EmptyState title="Priority inbox is empty" subtitle="No urgent notifications right now." />
        )}
      </Stack>
    </MainLayout>
  );
}
