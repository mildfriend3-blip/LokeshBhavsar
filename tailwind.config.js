/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
    "./public/index.html"
  ],
  theme: {
    extend: {
      colors: {
        cream: '#FAF6EE',
        navy: '#0F2B46',
        navyDark: '#091A2B',
        orange: '#E8760C',
        green: '#1F9D55',
        red: '#BE3A2B',
        slateCustom: '#5B6B7A',
        borderNavy: '#0F2B46'
      },
      fontFamily: {
        mono: ['ui-monospace', 'SFMono-Regular', 'Menlo', 'Monaco', 'Consolas', 'monospace'],
        sans: ['system-ui', '-apple-system', 'BlinkMacSystemFont', 'Segoe UI', 'Roboto', 'sans-serif'],
      }
    },
  },
  plugins: [],
}
