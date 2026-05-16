"use client";

import { useEffect } from "react";

import { Box, Button, Chip, Stack, Typography } from "@mui/material";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import { useMutation, useQueryClient } from "@tanstack/react-query";

import { MainLayout } from "@/layouts/MainLayout";
import { EmptyState } from "@/components/EmptyState";
import { ErrorBanner } from "@/components/ErrorBanner";
import { LoadingSkeleton } from "@/components/LoadingSkeleton";
import { useNotificationDetails } from "@/hooks/useNotificationDetails";
import { markNotificationRead } from "@/services/notifications";
import { formatDateTime } from "@/utils/date";

const typeColor = {
  EVENT: "primary",
  RESULT: "secondary",
  PLACEMENT: "success"
} as const;

export default function NotificationDetailsPage({ params }: { params: { id: string } }) {
  const { data, isLoading, isError } = useNotificationDetails(params.id);
  const queryClient = useQueryClient();
  const { mutateAsync } = useMutation({
    mutationFn: markNotificationRead,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["notifications"] })
  });

  useEffect(() => {
    if (data && !data.read) {
      mutateAsync(data.id);
    }
  }, [data, mutateAsync]);

  return (
    <MainLayout>
      <Stack spacing={3}>
        <Button startIcon={<ArrowBackIcon />} href="/notifications">
          Back to notifications
        </Button>

        {isError && <ErrorBanner message="Unable to load notification." />}

        {isLoading && <LoadingSkeleton />}

        {!isLoading && !data && !isError && (
          <EmptyState title="Notification not found" subtitle="Return to the list to pick another item." />
        )}

        {!isLoading && data && (
          <Box sx={{ p: 4, borderRadius: 4, bgcolor: "rgba(255, 255, 255, 0.85)" }}>
            <Stack spacing={2}>
              <Stack direction="row" spacing={2} alignItems="center">
                <Chip label={data.type} color={typeColor[data.type]} />
                {!data.read && <Chip label="Unread" color="secondary" />}
              </Stack>
              <Typography variant="h4">{data.message}</Typography>
              <Typography variant="body2" color="text.secondary">
                {formatDateTime(data.createdAt)}
              </Typography>
              <Typography variant="body2">
                Priority score: <strong>{data.priorityScore}</strong>
              </Typography>
            </Stack>
          </Box>
        )}
      </Stack>
    </MainLayout>
  );
}
