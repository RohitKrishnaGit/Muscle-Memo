import { object, string } from "zod";

export const newTokenSchema = object({
    params: object({
        refreshToken: string({
            required_error: "refreshToken is required",
        }),
    }),
});
