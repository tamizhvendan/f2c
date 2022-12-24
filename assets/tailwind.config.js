/** @type {import('tailwindcss').Config} */

const colors = require('tailwindcss/colors');
const defaultTheme = require("tailwindcss/defaultTheme");

module.exports = {
  content: [
    "../src/f2c/web/**/*.clj"
  ],
  theme: {
    fontFamily: {
      display: ["Figtree", ...defaultTheme.fontFamily.sans],
      body: ["Inter", ...defaultTheme.fontFamily.sans],
      mono: defaultTheme.fontFamily.mono,
    },
    colors: {
      transparent: 'transparent',
      current: 'currentColor',
      black: colors.black,
      white: colors.white,
      gray: colors.gray,
      primary: colors.green,
      neutral: colors.slate,
      positive: colors.green,
      urge: colors.violet,
      warning: colors.yellow,
      info: colors.blue,
      critical: colors.red,
    },
    extend: {},
  },
  plugins: [require("a17t")],
}
