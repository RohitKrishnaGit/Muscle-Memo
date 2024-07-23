import jwt from "jsonwebtoken";
import { AppDataSource } from "../data-source";
import { User } from "../entities/User";
import { UserToken } from "../entities/UserToken";
import { getEnv } from "../environment";

const userTokenRepository = AppDataSource.getRepository(UserToken);

export const generateTokens = async (user: User) => {
    try {
        const payload = { id: user.id, role: user.role };
        const accessToken = jwt.sign(
            payload,
            getEnv().ACCESS_TOKEN_PRIVATE_KEY,
            { expiresIn: getEnv().ACCESS_TOKEN_TIMEOUT }
        );
        const refreshToken = jwt.sign(
            payload,
            getEnv().REFRESH_TOKEN_PRIVATE_KEY
        );

        const newToken = Object.assign(new UserToken(), {
            user: user,
            token: refreshToken,
        });
        await userTokenRepository.save(newToken);

        return Promise.resolve({ accessToken, refreshToken });
    } catch (err) {
        return Promise.reject(err);
    }
};

export const verifyRefreshToken = async (refreshToken: string) => {
    const privateKey: string = getEnv().REFRESH_TOKEN_PRIVATE_KEY;

    const token = await userTokenRepository.findOneBy({ token: refreshToken });
    if (!token) {
        return Promise.reject({
            error: true,
            message: "Invalid refresh token",
        });
    }

    try {
        const tokenDetails = jwt.verify(refreshToken, privateKey);
        return Promise.resolve({
            tokenDetails,
            error: false,
            message: "Valid refresh token",
        });
    } catch (err) {
        return Promise.reject({
            error: true,
            message: "Invalid refresh token",
        });
    }
};
