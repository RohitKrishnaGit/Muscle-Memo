import { NextFunction, Request, Response } from "express";
import jwt from "jsonwebtoken";
import { getEnv } from "../environment";
import { failure, success } from "../utils/responseTypes";
import { verifyRefreshToken } from "../utils/token";

export class TokenController {
    async newToken(request: Request, response: Response, next: NextFunction) {
        const { refreshToken } = request.body;

        try {
            let { tokenDetails, error, message } = await verifyRefreshToken(
                refreshToken
            );
            if (error) throw new Error(message);

            if (typeof tokenDetails === "string") {
                tokenDetails = { id: tokenDetails, role: undefined };
            }
            const payload = { id: tokenDetails.id, role: tokenDetails.role };
            const accessToken = jwt.sign(
                payload,
                getEnv().ACCESS_TOKEN_PRIVATE_KEY,
                { expiresIn: getEnv().ACCESS_TOKEN_TIMEOUT }
            );
            return success(accessToken);
        } catch (err) {
            return failure((err as any).message);
        }
    }
}
