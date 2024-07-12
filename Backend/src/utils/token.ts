import jwt from "jsonwebtoken";
import { AppDataSource } from "../data-source";
import { User } from "../entities/User";
import { UserToken } from "../entities/UserToken";

const userTokenRepository = AppDataSource.getRepository(UserToken);

export const generateTokens = async (user: User) => {
    try {
        const payload = { id: user.id };
        const accessToken = jwt.sign(
            payload,
            process.env.ACCESS_TOKEN_PRIVATE_KEY,
            { expiresIn: 10 } // TODO: Change to 300
        );
        const refreshToken = jwt.sign(
            payload,
            process.env.REFRESH_TOKEN_PRIVATE_KEY
        );

        const userToken = await userTokenRepository.findOne({
            where: {
                user: { id: user.id },
            },
            relations: {
                user: true,
            },
        });
        if (userToken) await userTokenRepository.remove(userToken);

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
    const privateKey: string = process.env.REFRESH_TOKEN_PRIVATE_KEY;

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