import { object, string } from "zod";

export const allUserSchema = object({});

export const oneUserSchema = object({
    params: object({
        id: string({
            required_error: "id is required",
        }).regex(/(^\d+$)|(^me$)/, "id should be either numerical or me"),
    }),
});

export const loginUserSchema = object({
    body: object({
        email: string({
            required_error: "email is required",
        }).email(),
        password: string({
            required_error: "password is required",
        }),
    }),
});

export const updateUserSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
    body: object({
        username: string().optional(),
        gender: string().optional(),
        experience: string().optional(),
    }),
});

export const logoutAllUserSchema = object({});

export const createUserSchema = object({
    body: object({
        username: string({
            required_error: "username is required",
        }),
        email: string({
            required_error: "email is required",
        }).email(),
        password: string({
            required_error: "password is required",
        }),
        gender: string({
            required_error: "gender is required",
        }),
        experience: string({
            required_error: "experience is required",
        }),
    }),
});

export const removeUserSchema = object({
    params: object({
        id: string({
            required_error: "id is required",
        }).regex(/(^\d+$)|(^me$)/, "id should be either numerical or me"),
    }),
});
