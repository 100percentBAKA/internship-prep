import { resolve } from "path"

export default {
    entry: "./index.js",
    output: {
        filename: "bundle.js",
        path: resolve(process.cwd(), "dist"),
    },
    mode: "development",
};