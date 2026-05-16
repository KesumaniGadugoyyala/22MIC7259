import { useInfiniteQuery } from "@tanstack/react-query";
import { getNotifications, NotificationQuery } from "@/services/notifications";

const PAGE_SIZE = 10;

export const useNotifications = (query: NotificationQuery) => {
  return useInfiniteQuery({
    queryKey: ["notifications", query],
    queryFn: ({ pageParam }) => getNotifications({
      ...query,
      page: pageParam as number,
      size: PAGE_SIZE
    }),
    initialPageParam: 0,
    getNextPageParam: (lastPage) => {
      const nextPage = lastPage.page + 1;
      return nextPage < lastPage.totalPages ? nextPage : undefined;
    }
  });
};
