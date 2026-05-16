import { createTheme } from "@mui/material/styles";

export const theme = createTheme({
  palette: {
    mode: "light",
    primary: {
      main: "#0f2d3a",
      light: "#1d4d5c"
    },
    secondary: {
      main: "#f0a202",
      light: "#f9c74f"
    },
    background: {
      default: "#f6f2ea",
      paper: "#ffffff"
    },
    success: {
      main: "#2d6a4f"
    },
    error: {
      main: "#b02a2a"
    }
  },
  shape: {
    borderRadius: 16
  },
  typography: {
    fontFamily: "var(--font-body)",
    h1: {
      fontFamily: "var(--font-display)",
      fontWeight: 700
    },
    h2: {
      fontFamily: "var(--font-display)",
      fontWeight: 700
    },
    h3: {
      fontFamily: "var(--font-display)",
      fontWeight: 700
    },
    h4: {
      fontFamily: "var(--font-display)",
      fontWeight: 600
    },
    h5: {
      fontFamily: "var(--font-display)",
      fontWeight: 600
    },
    button: {
      textTransform: "none",
      fontWeight: 600
    }
  }
});
