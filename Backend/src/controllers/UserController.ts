import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { User } from "../entities/User";
import { UserToken } from "../entities/UserToken";
import { generatePasswordHash, validatePassword } from "../utils/password";
import { failure, success } from "../utils/responseTypes";
import { generateTokens } from "../utils/token";

export class UserController {
    private userRepository = AppDataSource.getRepository(User);
    private userTokenRepository = AppDataSource.getRepository(UserToken);

    async all(request: Request, response: Response, next: NextFunction) {
        return success(this.userRepository.find());
    }

    async one(request: Request, response: Response, next: NextFunction) {
        const id = parseInt(request.params.id);

        const user = await this.userRepository.findOneBy({
            id,
        });

        if (!user) {
            return failure("unregistered user");
        }
        return success(user);
    }

    async login(request: Request, response: Response, next: NextFunction) {
        const { username, password } = request.body;

        const user = await this.userRepository.findOne({
            where: {
                username,
            },
            select: ["id", "password"],
        });

        if (!user) {
            return failure("Invalid email or password", 401);
        }

        const verifiedPassword = await validatePassword(
            password,
            user.password
        );

        if (!verifiedPassword) {
            return failure("Invalid email or password", 401);
        }

        // const { accessToken, refreshToken } = await ;

        return success(generateTokens(user));
    }

    async logout(request: Request, response: Response, next: NextFunction) {
        const userToken = await this.userTokenRepository.findOneBy({
            token: request.body.refreshToken,
        });
        if (!userToken) return success("Logged Out Sucessfully");

        await this.userTokenRepository.delete(userToken);

        return success("Logged Out Sucessfully");
    }

    async create(request: Request, response: Response, next: NextFunction) {
        const { username, fullName, email, password } = request.body;

        const userExists = !!(await this.userRepository.findOneBy([
            { username },
            { email },
        ]));

        if (userExists) {
            return failure("User with given username or email already exist");
        }

        const user = Object.assign(new User(), {
            username,
            fullName,
            email,
            password: await generatePasswordHash(password),
        });

        await this.userRepository.save(user);

        return success("User created");
    }

    async remove(request: Request, response: Response, next: NextFunction) {
        const id = parseInt(request.params.id);

        let userToRemove = await this.userRepository.findOneBy({
            id,
        });

        if (!userToRemove) {
            return failure("this user not exist");
        }

        await this.userRepository.remove(userToRemove);

        return success("user has been removed");
    }
}
