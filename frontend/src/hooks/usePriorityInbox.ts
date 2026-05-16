import { useQuery } from "@tanstack/react-query";
import { getPriorityInbox } from "@/services/notifications";

export const usePriorityInbox = () => {
  return useQuery({
    queryKey: ["priorityInbox"],
    queryFn: getPriorityInbox,
    staleTime: 15000
  });
};
