"use client";

import { useEffect, useMemo } from "react";

import { Box, Button, Grid, Stack, Typography } from "@mui/material";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import TrendingUpIcon from "@mui/icons-material/TrendingUp";

import { MainLayout } from "@/layouts/MainLayout";
import { LoadingSkeleton } from "@/components/LoadingSkeleton";
import { NotificationCard } from "@/components/NotificationCard";
import { useNotifications } from "@/hooks/useNotifications";
import { useNotificationContext } from "@/context/NotificationContext";
import { useRealtimeNotifications } from "@/hooks/useRealtimeNotifications";

export default function DashboardPage() {
  const { data, isLoading } = useNotifications({});
  const { setUnreadCount } = useNotificationContext();
  useRealtimeNotifications();

  const notifications = useMemo(() => data?.pages.flatMap((page) => page.content) ?? [], [data]);

  useEffect(() => {
    const unread = notifications.filter((item) => !item.read).length;
    setUnreadCount(unread);
  }, [notifications, setUnreadCount]);

  return (
    <MainLayout>
      <Stack spacing={4}>
        <Stack direction={{ xs: "column", md: "row" }} justifyContent="space-between" spacing={2}>
          <Box>
            <Typography variant="h3" sx={{ mb: 1 }}>
              Welcome back
            </Typography>
            <Typography variant="body1" color="text.secondary">
              Stay on top of campus news, results, and placement updates in real time.
            </Typography>
          </Box>
          <Button variant="contained" color="secondary" href="/notifications">
            View all notifications
          </Button>
        </Stack>

        <Grid container spacing={3}>
          <Grid item xs={12} md={4}>
            <Box sx={{ p: 3, borderRadius: 4, bgcolor: "rgba(15, 45, 58, 0.9)", color: "#fff" }}>
              <Typography variant="overline">Unread alerts</Typography>
              <Typography variant="h3" sx={{ mt: 1 }}>
                {notifications.filter((item) => !item.read).length}
              </Typography>
              <Stack direction="row" spacing={1} alignItems="center" sx={{ mt: 2 }}>
                <TrendingUpIcon fontSize="small" />
                <Typography variant="body2">Live feed enabled</Typography>
              </Stack>
            </Box>
          </Grid>
          <Grid item xs={12} md={4}>
            <Box sx={{ p: 3, borderRadius: 4, bgcolor: "rgba(240, 162, 2, 0.95)" }}>
              <Typography variant="overline">Priority focus</Typography>
              <Typography variant="h3" sx={{ mt: 1 }}>
                Top 10
              </Typography>
              <Stack direction="row" spacing={1} alignItems="center" sx={{ mt: 2 }}>
                <CheckCircleIcon fontSize="small" />
                <Typography variant="body2">Placement-first ranking</Typography>
              </Stack>
            </Box>
          </Grid>
          <Grid item xs={12} md={4}>
            <Box sx={{ p: 3, borderRadius: 4, bgcolor: "rgba(255, 255, 255, 0.75)" }}>
              <Typography variant="overline">Throughput</Typography>
              <Typography variant="h3" sx={{ mt: 1 }}>
                {notifications.length}
              </Typography>
              <Typography variant="body2" color="text.secondary" sx={{ mt: 2 }}>
                Notifications processed this session.
              </Typography>
            </Box>
          </Grid>
        </Grid>

        <Stack spacing={2}>
          <Typography variant="h4">Recent activity</Typography>
          {isLoading && (
            <Stack spacing={2}>
              <LoadingSkeleton />
              <LoadingSkeleton />
            </Stack>
          )}
          {!isLoading && notifications.slice(0, 4).map((notification) => (
            <NotificationCard key={notification.id} notification={notification} />
          ))}
        </Stack>
      </Stack>
    </MainLayout>
  );
}
