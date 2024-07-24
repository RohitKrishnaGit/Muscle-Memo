import { boolean, number, object, string } from "zod";

export const allPublicWorkoutSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
});

export const allJoinedPublicWorkoutSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
});

export const onePublicWorkoutSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
        id: string({
            required_error: "id is required",
        }).regex(/^\d+$/, "id should be numerical"),
    }),
});

export const createPublicWorkoutSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
    body: object({
        name: string({
            required_error: "name is required",
        }),
        date: number({
            required_error: "date is required",
        }),
        latitude: string({
            required_error: "latitude is required",
        }),
        longitude: string({
            required_error: "longitude is required",
        }),
        description: string({
            required_error: "description is required",
        }),
        gender: string({
            required_error: "gender is required",
        }).optional(),
        experience: string({
            required_error: "experience is required",
        }).optional(),
    }),
});

export const removePublicWorkoutSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
        id: string({
            required_error: "id is required",
        }).regex(/^\d+$/, "id should be numerical"),
    }),
});

export const filterPublicWorkoutSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
    body: object({
        gender: string().optional(),
        experience: string().optional(),
        friendsOnly: boolean({
            required_error: "friendsOnly is required",
        }),
        latitude: string({
            required_error: "latitude is required",
        }),
        longitude: string({
            required_error: "longitude is required",
        }),
    }),
});
