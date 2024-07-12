import { number, object, string } from "zod";

export const allExerciseSchema = object({
    params: object({
        workoutId: string({
            required_error: "workoutId is required",
        }).regex(/^\d+$/),
    }),
});

export const oneExerciseSchema = object({
    params: object({
        workoutId: string({
            required_error: "workoutId is required",
        }).regex(/^\d+$/),
        id: string({
            required_error: "id is required",
        }).regex(/^\d+$/),
    }),
});

export const createExerciseSchema = object({
    body: object({
        workoutId: number({
            required_error: "workoutId is required",
        }),
        exerciseRefId: number().nullable(),
        customExerciseRefId: number().nullable(),
        userId: number({
            required_error: "userId is required",
        }),
        sets: number().nullable(),
        reps: number().nullable(),
        weight: number().nullable(),
        duration: number().nullable(),
    }),
});

export const removeExerciseSchema = object({
    params: object({
        workoutId: string({
            required_error: "workoutId is required",
        }).regex(/^\d+$/),
        id: string({
            required_error: "id is required",
        }).regex(/^\d+$/),
    }),
});
