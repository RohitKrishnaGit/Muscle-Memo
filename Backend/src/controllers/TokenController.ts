import { AppDataSource } from "../data-source";
import { NextFunction, Request, Response } from "express";
import { User } from "../entities/User";
import {
    generatePasswordHash,
    generateRandomToken,
    validatePassword,
} from "../utils/password";
import { generateTokens, verifyRefreshToken } from "../utils/token";
import jwt from "jsonwebtoken";
import { UserToken } from "../entities/UserToken";

export class TokenController {
    private userTokenRepository = AppDataSource.getRepository(UserToken);

    async newToken(request: Request, response: Response, next: NextFunction) {
        verifyRefreshToken(request.body.refreshToken)
            .then(({ tokenDetails }) => {
                if (typeof tokenDetails === "string") {
                    tokenDetails = { id: tokenDetails };
                }
                const payload = { id: tokenDetails.id };
                const accessToken = jwt.sign(
                    payload,
                    process.env.ACCESS_TOKEN_PRIVATE_KEY,
                    { expiresIn: 300 }
                );

                response.status(200).json({
                    error: false,
                    accessToken,
                    message: "Access token created successfully",
                });
            })
            .catch((err) => response.status(400).json(err));
    }

    async remove(request: Request, response: Response, next: NextFunction) {
        const userToken = await this.userTokenRepository.findOneBy({
            token: request.body.refreshToken,
        });
        if (!userToken)
            return response
                .status(200)
                .json({ error: false, message: "Logged Out Sucessfully" });

        await this.userTokenRepository.delete(userToken);

        return response
            .status(200)
            .json({ error: false, message: "Logged Out Sucessfully" });
    }
}
