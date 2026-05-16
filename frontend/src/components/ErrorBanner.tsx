import { Alert } from "@mui/material";

export const ErrorBanner = ({ message }: { message: string }) => {
  return <Alert severity="error">{message}</Alert>;
};
