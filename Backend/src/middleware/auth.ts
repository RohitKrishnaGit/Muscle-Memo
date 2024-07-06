import { NextFunction, Request, Response } from "express";
import jwt from "jsonwebtoken";

// Middleware to make sure that the request is from an authenticated user
export const authenticateWithToken = (
    req: Request,
    res: Response,
    next: NextFunction
) => {
    const token = req.header("x-access-token");
    if (!token) return res.status(401).json("Access Denied: No token provided");

    try {
        const tokenDetails = jwt.verify(
            token,
            process.env.ACCESS_TOKEN_PRIVATE_KEY
        );
        req.user = (
            typeof tokenDetails === "string"
                ? { id: tokenDetails }
                : tokenDetails
        ) as jwt.JwtPayload;
        next();
    } catch (err) {
        console.log(err);
        res.status(401).json("Access Denied: Invalid token");
    }
};

export const validateUserParam = (paramName: string) => {
    return (req: Request, res: Response, next: NextFunction) => {
        if (!req.user)
            return res.status(401).json("Access Denied: User Unknown");

        try {
            if (parseInt(req.params[paramName]) !== req.user["id"]) {
                return res.status(401).json("Access Denied: No Permission");
            }

            next();
        } catch (err) {
            console.log(err);
            return res.status(401).json("Access Denied: Invalid token");
        }
    };
};

export const validateUserBody = (bodyParam: string) => {
    return (req: Request, res: Response, next: NextFunction) => {
        if (!req.user)
            return res.status(401).json("Access Denied: User Unknown");

        try {
            if (parseInt(req.body[bodyParam]) !== req.user["id"]) {
                return res.status(401).json("Access Denied: No Permission");
            }

            next();
        } catch (err) {
            console.log(err);
            return res.status(401).json("Access Denied: Invalid token");
        }
    };
};
