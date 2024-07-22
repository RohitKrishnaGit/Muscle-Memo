import dotenv from "dotenv";
type Env = {
    NODE_ENV: string;
    PORT: number;
    ACCESS_TOKEN_PRIVATE_KEY: string;
    REFRESH_TOKEN_PRIVATE_KEY: string;
    ACCESS_TOKEN_TIMEOUT: number;
    FIREBASE_PROJECT_ID: string;
    FIREBASE_CLIENT_EMAIL: string;
    FIREBASE_PRIVATE_KEY: string;
    G_CLIENT_ID: string;
    G_CLIENT_SECRET: string;
    G_REDIRECT_URI: string;
    G_REFRESH_TOKEN: string;
};

let env: Env;

export const initEnv = () => {
    dotenv.config();
    env = {
        NODE_ENV: process.env.NODE_ENV,
        PORT: parseInt(process.env.PORT),
        ACCESS_TOKEN_PRIVATE_KEY: process.env.ACCESS_TOKEN_PRIVATE_KEY,
        REFRESH_TOKEN_PRIVATE_KEY: process.env.REFRESH_TOKEN_PRIVATE_KEY,
        ACCESS_TOKEN_TIMEOUT: parseInt(
            process.env.ACCESS_TOKEN_TIMEOUT ?? "30000"
        ),
        FIREBASE_PROJECT_ID: process.env.FIREBASE_PROJECT_ID,
        FIREBASE_CLIENT_EMAIL: process.env.FIREBASE_CLIENT_EMAIL,
        FIREBASE_PRIVATE_KEY: process.env.FIREBASE_PRIVATE_KEY,
        G_CLIENT_ID: process.env.G_CLIENT_ID,
        G_CLIENT_SECRET: process.env.G_CLIENT_SECRET,
        G_REDIRECT_URI: process.env.G_REDIRECT_URI,
        G_REFRESH_TOKEN: process.env.G_REFRESH_TOKEN,
    };
};

export const getEnv = () => env;
