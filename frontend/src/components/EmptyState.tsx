import { Box, Typography } from "@mui/material";

export const EmptyState = ({ title, subtitle }: { title: string; subtitle: string }) => {
  return (
    <Box sx={{ textAlign: "center", p: 6, borderRadius: 4, bgcolor: "rgba(255, 255, 255, 0.7)" }}>
      <Typography variant="h5" sx={{ mb: 1 }}>
        {title}
      </Typography>
      <Typography variant="body2" color="text.secondary">
        {subtitle}
      </Typography>
    </Box>
  );
};
