import apiClient from "@/services/apiClient";
import { ApiResponse, Notification, NotificationType, PageResponse } from "@/types/notification";

export interface NotificationQuery {
  type?: NotificationType;
  read?: boolean;
  page?: number;
  size?: number;
  sort?: string;
}

export const getNotifications = async (query: NotificationQuery) => {
  const response = await apiClient.get<PageResponse<Notification>>("/api/v1/notifications", {
    params: {
      type: query.type,
      read: query.read,
      page: query.page ?? 0,
      size: query.size ?? 10,
      sort: query.sort ?? "createdAt,desc"
    }
  });
  return response.data;
};

export const markNotificationRead = async (id: string) => {
  const response = await apiClient.patch<Notification>(`/api/v1/notifications/${id}/read`);
  return response.data;
};

export const markAllRead = async () => {
  const response = await apiClient.patch<ApiResponse>("/api/v1/notifications/read-all");
  return response.data;
};

export const getPriorityInbox = async () => {
  const response = await apiClient.get<Notification[]>("/api/v1/notifications/priority");
  return response.data;
};

export const getNotificationDetails = async (id: string) => {
  const response = await apiClient.get<Notification>(`/api/v1/notifications/${id}`);
  return response.data;
};
