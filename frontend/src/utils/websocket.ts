import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

export const createStompClient = (url: string) => {
  return new Client({
    webSocketFactory: () => new SockJS(url),
    reconnectDelay: 4000
  });
};
