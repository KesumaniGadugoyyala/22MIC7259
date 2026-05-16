import { useCallback, useState } from "react";

export interface SnackState {
  message: string;
  severity: "success" | "error" | "info" | "warning";
}

export const useSnack = () => {
  const [snack, setSnack] = useState<SnackState | null>(null);

  const showSnack = useCallback((message: string, severity: SnackState["severity"]) => {
    setSnack({ message, severity });
  }, []);

  const closeSnack = useCallback(() => setSnack(null), []);

  return { snack, showSnack, closeSnack };
};
