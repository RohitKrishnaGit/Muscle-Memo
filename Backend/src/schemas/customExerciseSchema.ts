import { boolean, object, string } from "zod";

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
        durationVSReps: boolean({
            required_error: "durationVSReps is required",
        }),
        weight: boolean({
            required_error: "weight is required",
        }),
        distance: boolean({
            required_error: "distance is required",
        }),
        description: string().optional(),
        imagePath: string().optional(),
    }),
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
});

export const updateCustomExerciseRefSchema = object({
    body: object({
        name: string({
            required_error: "name is required",
        }),
        durationVSReps: boolean({
            required_error: "durationVSReps is required",
        }),
        weight: boolean({
            required_error: "weight is required",
        }),
        distance: boolean({
            required_error: "distance is required",
        }),
        description: string().optional(),
        imagePath: string().optional(),
    }),
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
        id: string({
            required_error: "id is required",
        }).regex(/^\d+$/, "id should be numerical"),
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
