namespace NodeJS {
    interface ProcessEnv {
        NODE_ENV: string;
        PORT: string;
        ACCESS_TOKEN_PRIVATE_KEY: string;
        REFRESH_TOKEN_PRIVATE_KEY: string;
        ACCESS_TOKEN_TIMEOUT?: string;
        FIREBASE_PROJECT_ID: string;
        FIREBASE_CLIENT_EMAIL: string;
        FIREBASE_PRIVATE_KEY: string;
        G_CLIENT_ID: string;
        G_CLIENT_SECRET: string;
        G_REDIRECT_URI: string;
        G_REFRESH_TOKEN: string;
    }
}
