import { number, object, string } from "zod";

export const allCustomExerciseRefSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
});

export const oneCustomExerciseRefSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
        id: string({
            required_error: "id is required",
        }).regex(/^\d+$/, "id should be numerical"),
    }),
});

export const createCustomExerciseRefSchema = object({
    body: object({
        name: string({
            required_error: "name is required",
        }),
        userId: number({
            required_error: "userId is required",
        }).or(
            string().regex(/^me$/, "userId should be either numerical or me")
        ),
    }),
});

export const removeCustomExerciseRefSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
        id: string({
            required_error: "id is required",
        }).regex(/^\d+$/, "id should be numerical"),
    }),
});
