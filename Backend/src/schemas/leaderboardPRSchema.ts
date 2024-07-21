import { boolean, object, string, number } from "zod";

export const getPrSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
        exerciseRefId: string({
            required_error: "exerciseRefId is required",
        }).regex(/^\d+$/, "exerciseRefId should be numerical"),
    }),
});

export const postPrSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
        exerciseRefId: string({
            required_error: "exerciseRefId is required",
        }).regex(/^\d+$/, "exerciseRefId should be numerical"),
    }),
    body: object({
        Pr: number({
            required_error: "Pr is required",
        }),
    }),
});

export const leaderboardSchema = object({
    params: object({
        exerciseRefId: string({
            required_error: "exerciseRefId is required",
        }).regex(/^\d+$/, "exerciseRefId should be numerical"),
        count: string({
            required_error: "count is required",
        }).regex(/^\d+$/, "count should be numerical"),
    }),
});

export const leaderboardFriendsSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
        exerciseRefId: string({
            required_error: "exerciseRefId is required",
        }).regex(/^\d+$/, "exerciseRefId should be numerical"),
        count: string({
            required_error: "count is required",
        }).regex(/^\d+$/, "count should be numerical"),
    }),
});

export const getPrVisibilitySchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
        exerciseRefId: string({
            required_error: "exerciseRefId is required",
        }).regex(/^\d+$/, "exerciseRefId should be numerical"),
    }),
});

export const postPrVisibilitySchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
        exerciseRefId: string({
            required_error: "exerciseRefId is required",
        }).regex(/^\d+$/, "exerciseRefId should be numerical"),
    }),
    body: object({
        allowedValue: boolean({
            required_error: "allowedValue is required",
        })
    }),
});

export const getAllPrSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
});