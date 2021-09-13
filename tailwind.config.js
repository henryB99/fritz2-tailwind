// tailwind.config.js
const defaultTheme = require('tailwindcss/defaultTheme')

module.exports = {
    mode: undefined, //'jit',
    purge: {
        enabled: false,
        content: [
            './**/*.html',
            './**/*.{js,jsx,ts,tsx,vue,kt,z}',
        ]
    },
    darkMode: false, // or 'media' or 'class'
    theme: {
        extend: {
            fontFamily: {
                sans: ['Inter var', ...defaultTheme.fontFamily.sans],
            },
        },
    },
    variants: {},
    plugins: [
        require('@tailwindcss/forms')
    ],
}
