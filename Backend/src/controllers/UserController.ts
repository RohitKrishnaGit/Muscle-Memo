import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { User } from "../entities/User";
import { UserToken } from "../entities/UserToken";
import { generatePasswordHash, validatePassword } from "../utils/password";
import { generateTokens } from "../utils/token";

export class UserController {
    private userRepository = AppDataSource.getRepository(User);
    private userTokenRepository = AppDataSource.getRepository(UserToken);

    async all(request: Request, response: Response, next: NextFunction) {
        return this.userRepository.find();
    }

    async one(request: Request, response: Response, next: NextFunction) {
        const id = parseInt(request.params.id);

        const user = await this.userRepository.findOneBy({
            id,
        });

        if (!user) {
            return "unregistered user";
        }
        return user;
    }

    async login(request: Request, response: Response, next: NextFunction) {
        const { username, password } = request.body;

        const user = await this.userRepository.findOneBy({
            username,
        });

        if (!user) {
            response.status(401);
            return { error: true, message: "Invalid email or password" };
        }

        const verifiedPassword = await validatePassword(
            password,
            user.password
        );

        if (!verifiedPassword) {
            response.status(401);
            return { error: true, message: "Invalid email or password" };
        }

        const { accessToken, refreshToken } = await generateTokens(user);

        return {
            error: false,
            accessToken,
            refreshToken,
            message: "Logged in sucessfully",
        };
    }

    async logout(request: Request, response: Response, next: NextFunction) {
        const userToken = await this.userTokenRepository.findOneBy({
            token: request.body.refreshToken,
        });
        if (!userToken)
            return { error: false, message: "Logged Out Sucessfully" };

        await this.userTokenRepository.delete(userToken);

        return { error: false, message: "Logged Out Sucessfully" };
    }

    async create(request: Request, response: Response, next: NextFunction) {
        const { username, fullName, email, password } = request.body;

        const userExists = !!(await this.userRepository.findOneBy([
            { username },
            { email },
        ]));

        if (userExists) {
            return response.status(400).json({
                error: true,
                message: "User with given username or email already exist",
            });
        }

        const user = Object.assign(new User(), {
            username,
            fullName,
            email,
            password: await generatePasswordHash(password),
        });

        return this.userRepository.save(user);
    }

    async remove(request: Request, response: Response, next: NextFunction) {
        const id = parseInt(request.params.id);

        let userToRemove = await this.userRepository.findOneBy({
            id,
        });

        if (!userToRemove) {
            return "this user not exist";
        }

        await this.userRepository.remove(userToRemove);

        return "user has been removed";
    }
}
