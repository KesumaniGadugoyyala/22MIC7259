"use client";

import { useState } from "react";
import Link from "next/link";

import {
  AppBar,
  Badge,
  Box,
  Drawer,
  IconButton,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Toolbar,
  Typography,
  useMediaQuery
} from "@mui/material";
import { useTheme } from "@mui/material/styles";
import DashboardIcon from "@mui/icons-material/Dashboard";
import NotificationsActiveIcon from "@mui/icons-material/NotificationsActive";
import PriorityHighIcon from "@mui/icons-material/PriorityHigh";
import MenuIcon from "@mui/icons-material/Menu";

import { useNotificationContext } from "@/context/NotificationContext";

const drawerWidth = 250;

const navItems = [
  { label: "Dashboard", href: "/", icon: <DashboardIcon /> },
  { label: "Notifications", href: "/notifications", icon: <NotificationsActiveIcon /> },
  { label: "Priority Inbox", href: "/priority", icon: <PriorityHighIcon /> }
];

export const AppShell = ({ children }: { children: React.ReactNode }) => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("md"));
  const [mobileOpen, setMobileOpen] = useState(false);
  const { unreadCount } = useNotificationContext();

  const drawerContent = (
    <Box sx={{ p: 3 }}>
      <Typography variant="h5" sx={{ mb: 3 }}>
        Campus Pulse
      </Typography>
      <List sx={{ display: "flex", flexDirection: "column", gap: 1 }}>
        {navItems.map((item) => (
          <ListItem key={item.href} disablePadding>
            <ListItemButton component={Link} href={item.href} sx={{ borderRadius: 2 }}>
              <ListItemIcon>{item.icon}</ListItemIcon>
              <ListItemText primary={item.label} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Box>
  );

  return (
    <Box sx={{ display: "flex" }}>
      <AppBar position="fixed" color="transparent" elevation={0}>
        <Toolbar sx={{ justifyContent: "space-between" }}>
          <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
            {isMobile && (
              <IconButton color="primary" onClick={() => setMobileOpen(true)}>
                <MenuIcon />
              </IconButton>
            )}
            <Typography variant="h6">Campus Notification Platform</Typography>
          </Box>
          <Badge color="secondary" badgeContent={unreadCount}>
            <NotificationsActiveIcon />
          </Badge>
        </Toolbar>
      </AppBar>
      <Box component="nav">
        {isMobile ? (
          <Drawer
            variant="temporary"
            open={mobileOpen}
            onClose={() => setMobileOpen(false)}
            ModalProps={{ keepMounted: true }}
            sx={{ "& .MuiDrawer-paper": { width: drawerWidth } }}
          >
            {drawerContent}
          </Drawer>
        ) : (
          <Drawer
            variant="permanent"
            sx={{ "& .MuiDrawer-paper": { width: drawerWidth, border: 0 } }}
            open
          >
            {drawerContent}
          </Drawer>
        )}
      </Box>
      <Box
        component="main"
        sx={{ flexGrow: 1, p: { xs: 3, md: 5 }, mt: 8, ml: isMobile ? 0 : `${drawerWidth}px` }}
      >
        {children}
      </Box>
    </Box>
  );
};
