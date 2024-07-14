import { object, string } from "zod";

export const newTokenSchema = object({
    body: object({
        refreshToken: string({
            required_error: "refreshToken is required",
        }),
    }),
});
