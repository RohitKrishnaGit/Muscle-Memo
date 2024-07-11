import { NextFunction, Request, Response } from "express";
import jwt from "jsonwebtoken";
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
                tokenDetails = { id: tokenDetails };
            }
            const payload = { id: tokenDetails.id };
            const accessToken = jwt.sign(
                payload,
                process.env.ACCESS_TOKEN_PRIVATE_KEY,
                { expiresIn: 10 } // TODO: Change to 300
            );
            return { accessToken };
        } catch (err) {
            response.status(400);
            return (err as any).message;
        }
    }
}
