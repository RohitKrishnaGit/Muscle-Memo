import { number, object, string } from "zod";

export const allTemplateSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/^\d+$/),
    }),
});

export const oneTemplateSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/^\d+$/),
        id: string({
            required_error: "id is required",
        }).regex(/^\d+$/),
    }),
});

export const createTemplateSchema = object({
    body: object({
        name: string({
            required_error: "name is required",
        }),
        userId: number({
            required_error: "userId is required",
        }),
    }),
});

export const removeTemplateSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/^\d+$/),
        id: string({
            required_error: "id is required",
        }).regex(/^\d+$/),
    }),
});
