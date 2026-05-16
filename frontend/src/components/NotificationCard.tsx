"use client";

import Link from "next/link";
import { Box, Card, CardActionArea, Chip, Stack, Typography } from "@mui/material";
import BoltIcon from "@mui/icons-material/Bolt";
import MailIcon from "@mui/icons-material/Mail";

import { Notification } from "@/types/notification";
import { formatDateTime } from "@/utils/date";

const typeColor: Record<Notification["type"], "default" | "primary" | "secondary" | "success"> = {
  EVENT: "primary",
  RESULT: "secondary",
  PLACEMENT: "success"
};

export const NotificationCard = ({ notification }: { notification: Notification }) => {
  return (
    <Card sx={{ borderRadius: 3, border: notification.read ? "1px solid transparent" : "1px solid #f0a202" }}>
      <CardActionArea component={Link} href={`/notifications/${notification.id}`}>
        <Box sx={{ p: 3 }}>
          <Stack direction="row" justifyContent="space-between" alignItems="center" spacing={2}>
            <Stack direction="row" spacing={1} alignItems="center">
              <Chip label={notification.type} color={typeColor[notification.type]} size="small" />
              {!notification.read && <Chip label="Unread" color="secondary" size="small" />}
            </Stack>
            <Stack direction="row" spacing={1} alignItems="center">
              <BoltIcon fontSize="small" color="action" />
              <Typography variant="body2" color="text.secondary">
                {notification.priorityScore}
              </Typography>
            </Stack>
          </Stack>
          <Typography variant="h6" sx={{ mt: 2, mb: 1 }}>
            {notification.message}
          </Typography>
          <Stack direction="row" spacing={1} alignItems="center">
            <MailIcon fontSize="small" color="action" />
            <Typography variant="body2" color="text.secondary">
              {formatDateTime(notification.createdAt)}
            </Typography>
          </Stack>
        </Box>
      </CardActionArea>
    </Card>
  );
};
