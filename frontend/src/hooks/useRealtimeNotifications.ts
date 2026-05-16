import { useEffect } from "react";
import { useQueryClient } from "@tanstack/react-query";
import { createStompClient } from "@/utils/websocket";
import { Notification, PageResponse } from "@/types/notification";

export const useRealtimeNotifications = () => {
  const queryClient = useQueryClient();

  useEffect(() => {
    const studentId = process.env.NEXT_PUBLIC_STUDENT_ID || "S-1001";
    const wsUrl = process.env.NEXT_PUBLIC_WS_URL || "http://localhost:8080/ws";
    const client = createStompClient(wsUrl);

    client.onConnect = () => {
      client.subscribe(`/topic/notifications/${studentId}`, (message) => {
        const notification = JSON.parse(message.body) as Notification;
        queryClient.setQueriesData({ queryKey: ["notifications"] }, (data) => {
          if (!data) {
            return data;
          }
          const pages = (data as { pages: PageResponse<Notification>[] }).pages;
          if (!pages || pages.length === 0) {
            return data;
          }
          const firstPage = pages[0];
          return {
            ...(data as object),
            pages: [
              {
                ...firstPage,
                content: [notification, ...firstPage.content]
              },
              ...pages.slice(1)
            ]
          };
        });
      });
    };

    client.activate();
    return () => client.deactivate();
  }, [queryClient]);
};
