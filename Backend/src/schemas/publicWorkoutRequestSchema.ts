import { number, object, string } from "zod";

export const incomingPublicWorkoutRequestsSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
});

export const outgoingPublicWorkoutRequestsSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
});

export const sendPublicWorkoutRequestSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
    body: object({
        publicWorkoutId: number({
            required_error: "publicWorkoutId is required",
        }),
    }),
});

export const acceptPublicWorkoutRequestSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
        id: string({
            required_error: "id is required",
        }),
    }),
});

export const rejectPublicWorkoutRequestSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
        id: string({
            required_error: "id is required",
        }),
    }),
});