import { Card, CardContent, Skeleton, Stack } from "@mui/material";

export const LoadingSkeleton = () => {
  return (
    <Card sx={{ borderRadius: 3 }}>
      <CardContent>
        <Stack spacing={2}>
          <Skeleton variant="text" width="40%" />
          <Skeleton variant="text" width="80%" height={32} />
          <Skeleton variant="text" width="60%" />
        </Stack>
      </CardContent>
    </Card>
  );
};
