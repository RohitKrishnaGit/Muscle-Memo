import { number, object, string } from "zod";

export const allWorkoutSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/^\d+$/),
    }),
});

export const oneWorkoutSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/^\d+$/),
        id: string({
            required_error: "id is required",
        }).regex(/^\d+$/),
    }),
});

export const createWorkoutSchema = object({
    body: object({
        name: string({
            required_error: "name is required",
        }),
        userId: number({
            required_error: "userId is required",
        }),
    }),
});

export const removeWorkoutSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/^\d+$/),
        id: string({
            required_error: "id is required",
        }).regex(/^\d+$/),
    }),
});
