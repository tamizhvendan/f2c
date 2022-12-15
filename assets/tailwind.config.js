/** @type {import('tailwindcss').Config} */

const colors = require('tailwindcss/colors');
const defaultTheme = require("tailwindcss/defaultTheme");

module.exports = {
  content: [
    "../src/f2c/web/**/*.clj",
    "./node_modules/flowbite/**/*.js"
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
    },
    extend: {},
  },
  plugins: [
    require('flowbite/plugin')
  ],
}
