import { number, object, string } from "zod";

export const allUserSchema = object({});

export const oneUserSchema = object({
    params: object({
        id: string({
            required_error: "id is required",
        }).regex(/(^\d+$)|(^me$)/, "id should be either numerical or me"),
    }),
});

export const getFriendsSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
});

export const incomingFriendReqSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
});

export const outgoingFriendReqSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
});

export const sendFriendReqSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
    body: object({
        friendId: number({
            required_error: "friendId is required",
        }),
    }),
});

export const acceptFriendReqSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
    body: object({
        friendId: number({
            required_error: "friendId is required",
        }),
    }),
});

export const rejectFriendReqSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
    body: object({
        friendId: number({
            required_error: "friendId is required",
        }),
    }),
});

export const removeFriendSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
    body: object({
        friendId: number({
            required_error: "friendId is required",
        }),
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
        profilePicture: string().optional(),
    }),
});

export const reportUserSchema = object({
    body: object({
        reportedUserId: number({
            required_error: "reportedUserId is required",
        }),
        reason: string({
            required_error: "reason is required",
        }),
    }),
});

export const updateUserFirebaseTokenSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
    body: object({
        firebaseTokens: string({
            required_error: "token is required",
        }),
    }),
});

export const logoutSchema = object({
    params: object({
        userId: string({
            required_error: "userId is required",
        }).regex(/(^\d+$)|(^me$)/, "userId should be either numerical or me"),
    }),
    body: object({
        firebaseToken: string({
            required_error: "firebsae token is required",
        }),
        refreshToken: string({
            required_error: "refresh token is required",
        }),
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

export const requestPasswordResetSchema = object({
    body: object({
        email: string({ required_error: "email is required" }).email(),
    }),
});

export const confirmPasswordResetSchema = object({
    params: object({
        code: string({
            required_error: "code is required",
        }),
    }),
});

export const resetPasswordSchema = object({
    params: object({
        code: string({
            required_error: "code is required",
        }),
    }),
    body: object({
        password: string({ required_error: "password is required" }),
    }),
});
