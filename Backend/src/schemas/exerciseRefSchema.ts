import { object, string } from "zod";

export const allExerciseRefSchema = object({});

export const oneExerciseRefSchema = object({
    params: object({
        id: string({
            required_error: "id is required",
        }).regex(/^\d+$/),
    }),
});
