import { object, string } from "zod";

export const allUserSchema = object({});

export const oneUserSchema = object({
    params: object({
        id: string({
            required_error: "id is required",
        }).regex(/^\d+$/),
    }),
});

export const loginUserSchema = object({
    body: object({
        email: string({
            required_error: "email is required",
        }),
        password: string({
            required_error: "password is required",
        }),
    }),
});

export const logoutUserSchema = object({});

export const createUserSchema = object({
    body: object({
        username: string({
            required_error: "username is required",
        }),
        fullName: string({
            required_error: "fullName is required",
        }),
        email: string({
            required_error: "email is required",
        }).email(),
        password: string({
            required_error: "password is required",
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
