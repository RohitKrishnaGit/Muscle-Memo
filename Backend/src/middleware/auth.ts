import { NextFunction, Request, Response } from "express";
import jwt from "jsonwebtoken";

export const authenticateWithToken = (
    req: Request,
    res: Response,
    next: NextFunction
) => {
    const token = req.header("x-access-token");
    if (!token)
        return res
            .status(401)
            .json({ error: true, message: "Access Denied: No token provided" });

    try {
        const tokenDetails = jwt.verify(
            token,
            process.env.ACCESS_TOKEN_PRIVATE_KEY
        );
        req.user = tokenDetails;
        next();
    } catch (err) {
        console.log(err);
        res.status(401).json({
            error: true,
            message: "Access Denied: Invalid token",
        });
    }
};
