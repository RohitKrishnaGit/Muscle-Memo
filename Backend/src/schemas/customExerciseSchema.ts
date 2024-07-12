import { number, object, string } from "zod";

export const allCustomExerciseRefSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/^\d+$/),
    }),
});

export const oneCustomExerciseRefSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/^\d+$/),
        id: string({
            required_error: "id is required",
        }).regex(/^\d+$/),
    }),
});

export const createCustomExerciseRefSchema = object({
    body: object({
        name: string({
            required_error: "name is required",
        }),
        userId: number({
            required_error: "userId is required",
        }),
    }),
});

export const removeCustomExerciseRefSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/^\d+$/),
        id: string({
            required_error: "id is required",
        }).regex(/^\d+$/),
    }),
});
