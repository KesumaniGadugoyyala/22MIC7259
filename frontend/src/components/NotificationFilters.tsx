"use client";

import { Box, FormControl, InputLabel, MenuItem, Select, SelectChangeEvent, Stack, Switch, Typography } from "@mui/material";
import { NotificationType } from "@/types/notification";

interface FiltersProps {
  type: NotificationType | "";
  readOnlyUnread: boolean;
  onTypeChange: (value: NotificationType | "") => void;
  onReadToggle: (value: boolean) => void;
}

export const NotificationFilters = ({ type, readOnlyUnread, onTypeChange, onReadToggle }: FiltersProps) => {
  const handleTypeChange = (event: SelectChangeEvent) => {
    onTypeChange(event.target.value as NotificationType | "");
  };

  return (
    <Stack direction={{ xs: "column", md: "row" }} spacing={2} alignItems={{ xs: "stretch", md: "center" }}>
      <FormControl sx={{ minWidth: 200 }}>
        <InputLabel id="notification-type">Type</InputLabel>
        <Select labelId="notification-type" value={type} label="Type" onChange={handleTypeChange}>
          <MenuItem value="">All</MenuItem>
          <MenuItem value="EVENT">Event</MenuItem>
          <MenuItem value="RESULT">Result</MenuItem>
          <MenuItem value="PLACEMENT">Placement</MenuItem>
        </Select>
      </FormControl>
      <Box display="flex" alignItems="center" gap={1}>
        <Switch checked={readOnlyUnread} onChange={(event) => onReadToggle(event.target.checked)} />
        <Typography variant="body2">Show only unread</Typography>
      </Box>
    </Stack>
  );
};
