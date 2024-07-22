import { object, string } from "zod";

export const allChatsSchema = object({
    params: object({
        roomId: string({
            required_error: "roomId is required",
        }),
    }),
});

export const removeChatsSchema = object({
    params: object({
        roomId: string({
            required_error: "roomId is required",
        }),
    }),
});