import { number, object, string, array } from "zod";

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
        exerciseRefId: number().optional(),
        customExerciseRefId: number().optional(),
        templateId: number().optional(),
        exerciseSet: array(object({
            reps: number().optional(),
            weight: number().optional(),
            duration: number().optional()
          }))
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
