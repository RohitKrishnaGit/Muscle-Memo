import { object, string } from "zod";

export const sendNotificationSchema = object({
    body: object({
        registrationToken: string({
            required_error: "registrationToken is required",
        }),
        title: string({
            required_error: "title is required",
        }),
        message: string({
            required_error: "message is required",
        }),
    }),
});
