// lib/firebaseAdmin.js
import admin from "firebase-admin";
import { getEnv } from "./environment";

export const initialize = () => {
    if (!admin.apps.length) {
        admin.initializeApp({
            credential: admin.credential.cert({
                projectId: getEnv().FIREBASE_PROJECT_ID,
                clientEmail: getEnv().FIREBASE_CLIENT_EMAIL,
                privateKey: getEnv().FIREBASE_PRIVATE_KEY.replace(/\\n/g, "\n"),
            }),
        });
    }
};

export const getMessaging = () => {
    return admin.messaging();
};
