import { NextFunction, Request, Response } from "express";
import jwt from "jsonwebtoken";
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
                tokenDetails = { id: tokenDetails };
            }
            const payload = { id: tokenDetails.id };
            const accessToken = jwt.sign(
                payload,
                process.env.ACCESS_TOKEN_PRIVATE_KEY,
                { expiresIn: 3000 } // TODO: Change to 300
            );
            return success(accessToken);
        } catch (err) {
            return failure((err as any).message);
        }
    }
}
