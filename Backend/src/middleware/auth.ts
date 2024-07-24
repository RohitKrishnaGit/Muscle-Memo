import { NextFunction, Request, Response, request } from "express";
import jwt from "jsonwebtoken";
import { get } from "lodash";
import { Role } from "../entities/User";
import { getEnv } from "../environment";

// Middleware to make sure that the request is from an authenticated user
export const authenticateWithToken = (
    req: Request,
    res: Response,
    next: NextFunction
) => {
    const token = req.header("x-access-token");
    if (!token)
        return res.status(401).json({
            error: true,
            code: 401,
            message: "Access Denied: No token provided",
        });

    console.log({ token });

    try {
        const tokenDetails = jwt.verify(
            token,
            getEnv().ACCESS_TOKEN_PRIVATE_KEY
        );
        req.user = (
            typeof tokenDetails === "string"
                ? { id: tokenDetails, role: undefined }
                : tokenDetails
        ) as jwt.JwtPayload;
        next();
    } catch (err) {
        console.log(err);
        res.status(401).json({
            error: true,
            code: 401,
            message: "Access Denied: Invalid token",
        });
    }
};

export const convertMe = (path: string[]) => {
    return (req: Request, res: Response, next: NextFunction) => {
        if (!req.user)
            return res.status(401).json({
                error: true,
                code: 401,
                message: "Access Denied: User Unknown",
            });

        try {
            const [last, ...first] = path.slice().reverse();
            if (get(req, path) == "me") {
                get(req, first.reverse())[last] = req.user.id;
            }
            req.isMe = parseInt(get(req, path)) === req.user.id;

            next();
        } catch (err) {
            console.log(err);
            return res.status(401).json({
                error: true,
                code: 401,
                message: "Access Denied: Invalid token",
            });
        }
    };
};
export const applyUser = (
    path: string[],
    ...functions: ((path: string[]) => any)[]
) => {
    return functions.map((fn) => fn(path));
};

export const sameUser = (inputId?: number, token?: typeof request.user) => {
    const idMatches = inputId === token?.id;

    return {
        error: !idMatches,
        code: idMatches ? 200 : 403,
        message: idMatches ? "" : "input user id does not match with token id",
    };
};

export const isAdmin = (_?: number, token?: typeof request.user) => {
    if (!token) {
        return {
            error: true,
            code: 403,
            message: "token not valid",
        };
    }

    if (token.role !== Role.ADMIN) {
        return {
            error: true,
            code: 403,
        };
    }
    return {
        error: false,
        code: 200,
    };
};
