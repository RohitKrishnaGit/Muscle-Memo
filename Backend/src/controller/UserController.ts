import { AppDataSource } from "../data-source";
import { NextFunction, Request, Response } from "express";
import { User } from "../entity/User";

export class UserController {
    private userRepository = AppDataSource.getRepository(User);

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
        const username = request.params.user;
        const password = request.params.password;
        console.log(`made it here ${username}, ${password}`);
        const user = await this.userRepository.findOneBy({
            username,
            password,
        });

        if (!user) {
            return false;
        }
        return true;
    }

    async save(request: Request, response: Response, next: NextFunction) {
        const { username, fullName, email, password } = request.body;

        const user = Object.assign(new User(), {
            username,
            fullName,
            email,
            password,
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
