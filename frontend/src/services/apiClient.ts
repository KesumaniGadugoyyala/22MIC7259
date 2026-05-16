import axios from "axios";

const apiClient = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080",
  timeout: 15000
});

apiClient.interceptors.request.use((config) => {
  const studentId = process.env.NEXT_PUBLIC_STUDENT_ID || "S-1001";
  config.headers = config.headers || {};
  config.headers["X-Student-Id"] = studentId;
  return config;
});

export default apiClient;
