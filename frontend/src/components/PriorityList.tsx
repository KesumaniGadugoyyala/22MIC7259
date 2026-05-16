"use client";

import { Stack, Typography } from "@mui/material";

import { Notification } from "@/types/notification";
import { NotificationCard } from "@/components/NotificationCard";

export const PriorityList = ({ items }: { items: Notification[] }) => {
  return (
    <Stack spacing={2}>
      {items.map((item, index) => (
        <Stack key={item.id} spacing={1}>
          <Typography variant="overline">Priority #{index + 1}</Typography>
          <NotificationCard notification={item} />
        </Stack>
      ))}
    </Stack>
  );
};
