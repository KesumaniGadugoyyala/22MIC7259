"use client";

import { useEffect, useMemo, useRef, useState } from "react";

import {
  Box,
  Button,
  Divider,
  Pagination,
  Stack,
  Typography
} from "@mui/material";
import DoneAllIcon from "@mui/icons-material/DoneAll";
import { useMutation, useQueryClient } from "@tanstack/react-query";

import { MainLayout } from "@/layouts/MainLayout";
import { EmptyState } from "@/components/EmptyState";
import { ErrorBanner } from "@/components/ErrorBanner";
import { LoadingSkeleton } from "@/components/LoadingSkeleton";
import { NotificationCard } from "@/components/NotificationCard";
import { NotificationFilters } from "@/components/NotificationFilters";
import { useNotifications } from "@/hooks/useNotifications";
import { useRealtimeNotifications } from "@/hooks/useRealtimeNotifications";
import { useNotificationContext } from "@/context/NotificationContext";
import { markAllRead } from "@/services/notifications";
import { NotificationType } from "@/types/notification";
import { useSnack } from "@/hooks/useSnack";
import { Alert, Snackbar } from "@mui/material";

export default function NotificationsPage() {
  const [type, setType] = useState<NotificationType | "">("");
  const [onlyUnread, setOnlyUnread] = useState(false);
  const [activePage, setActivePage] = useState(1);

  const queryClient = useQueryClient();
  const { snack, showSnack, closeSnack } = useSnack();
  const { setUnreadCount } = useNotificationContext();

  const { data, isLoading, isError, fetchNextPage, hasNextPage, isFetchingNextPage } = useNotifications({
    type: type || undefined,
    read: onlyUnread ? false : undefined
  });

  useRealtimeNotifications();

  const notifications = useMemo(() => data?.pages.flatMap((page) => page.content) ?? [], [data]);
  const totalPages = data?.pages[0]?.totalPages ?? 0;

  useEffect(() => {
    const unread = notifications.filter((item) => !item.read).length;
    setUnreadCount(unread);
  }, [notifications, setUnreadCount]);

  const { mutateAsync: handleMarkAllRead } = useMutation({
    mutationFn: markAllRead,
    onSuccess: () => {
      showSnack("Marked all notifications as read", "success");
      queryClient.invalidateQueries({ queryKey: ["notifications"] });
    },
    onError: () => {
      showSnack("Unable to mark all as read", "error");
    }
  });

  const sentinelRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    if (!sentinelRef.current) {
      return;
    }

    const observer = new IntersectionObserver((entries) => {
      if (entries[0].isIntersecting && hasNextPage && !isFetchingNextPage) {
        fetchNextPage();
      }
    });

    observer.observe(sentinelRef.current);
    return () => observer.disconnect();
  }, [fetchNextPage, hasNextPage, isFetchingNextPage]);

  useEffect(() => {
    const ensurePageLoaded = async () => {
      if (!data || !hasNextPage) {
        return;
      }
      let loadedPages = data.pages.length;
      while (activePage > loadedPages && hasNextPage) {
        await fetchNextPage();
        loadedPages += 1;
      }
    };
    ensurePageLoaded();
  }, [activePage, data, fetchNextPage, hasNextPage]);

  return (
    <MainLayout>
      <Stack spacing={3}>
        <Stack direction={{ xs: "column", md: "row" }} justifyContent="space-between" spacing={2}>
          <Box>
            <Typography variant="h3" sx={{ mb: 1 }}>
              Notifications
            </Typography>
            <Typography variant="body1" color="text.secondary">
              Filter and track all campus alerts in one timeline.
            </Typography>
          </Box>
          <Button variant="contained" color="secondary" startIcon={<DoneAllIcon />} onClick={() => handleMarkAllRead()}>
            Mark all read
          </Button>
        </Stack>

        <NotificationFilters
          type={type}
          readOnlyUnread={onlyUnread}
          onTypeChange={setType}
          onReadToggle={setOnlyUnread}
        />

        <Divider />

        {isError && <ErrorBanner message="Failed to load notifications." />}

        {isLoading && (
          <Stack spacing={2}>
            <LoadingSkeleton />
            <LoadingSkeleton />
          </Stack>
        )}

        {!isLoading && notifications.length === 0 && (
          <EmptyState title="No notifications" subtitle="Try adjusting your filters or check back later." />
        )}

        <Stack spacing={2}>
          {notifications.map((notification) => (
            <NotificationCard key={notification.id} notification={notification} />
          ))}
        </Stack>

        <Box ref={sentinelRef} />

        {hasNextPage && (
          <Button variant="outlined" onClick={() => fetchNextPage()} disabled={isFetchingNextPage}>
            {isFetchingNextPage ? "Loading..." : "Load more"}
          </Button>
        )}

        {totalPages > 1 && (
          <Pagination
            count={totalPages}
            page={activePage}
            onChange={(_, value) => setActivePage(value)}
          />
        )}

        <Snackbar open={!!snack} autoHideDuration={4000} onClose={closeSnack}>
          {snack ? (
            <Alert onClose={closeSnack} severity={snack.severity} sx={{ width: "100%" }}>
              {snack.message}
            </Alert>
          ) : null}
        </Snackbar>
      </Stack>
    </MainLayout>
  );
}
