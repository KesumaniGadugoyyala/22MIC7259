import { useQuery } from "@tanstack/react-query";
import { getNotificationDetails } from "@/services/notifications";

export const useNotificationDetails = (id: string) => {
  return useQuery({
    queryKey: ["notification", id],
    queryFn: () => getNotificationDetails(id)
  });
};
