export type NotificationType = "EVENT" | "RESULT" | "PLACEMENT";

export interface Notification {
  id: string;
  studentId: string;
  type: NotificationType;
  message: string;
  read: boolean;
  priorityScore: number;
  createdAt: string;
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface ApiResponse {
  success: boolean;
  message: string;
}
