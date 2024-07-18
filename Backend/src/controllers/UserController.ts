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

    async getFriends(request: Request, response: Response, next: NextFunction) {
        console.log("get friedns");

        const id = parseInt(request.params.id);

        const user = await this.userRepository.findOne({
            where: { id },
            relations: {
                friends: true,
            },
        });

        if (!user) {
            return failure("unregistered user");
        }
        return success(user.friends);
    }

    async getIncomingFriendRequests(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const id = parseInt(request.params.id);

        const user = await this.userRepository.findOne({
            where: { id },
            relations: {
                incomingFriendRequests: true,
            },
        });

        if (!user) {
            return failure("unregistered user");
        }
        return success(user.incomingFriendRequests);
    }

    async getOutgoingFriendRequests(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const id = parseInt(request.params.id);

        const user = await this.userRepository.findOne({
            where: { id },
            relations: {
                outgoingFriendRequests: true,
            },
        });

        if (!user) {
            return failure("unregistered user");
        }
        return success(user.outgoingFriendRequests);
    }

    async sendFriendRequest(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const id = parseInt(request.params.id);
        const { friendId } = request.body;

        console.log({ friendId });

        console.log(request.body);

        const user = await this.userRepository.findOne({
            where: { id },
            relations: {
                outgoingFriendRequests: true,
            },
        });

        console.log({ user });

        const friend = await this.userRepository.findOneBy({
            id: friendId,
        });

        console.log({ friend });

        if (!user) {
            return "unregistered user";
        }

        if (!friend) {
            return "target user does not exist";
        }

        if (friend.id === user.id) {
            return "cannot send friend request to self";
        }

        user.outgoingFriendRequests = [
            ...(user.outgoingFriendRequests ?? []),
            friend,
        ];

        console.log("lol");

        await this.userRepository.save(user);

        return success("friend request sent successfgully");
    }

    async acceptFriendRequest(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const id = parseInt(request.params.id);
        const { friendId } = request.body;

        const user = await this.userRepository.findOne({
            where: { id },
            relations: {
                friends: true,
            },
        });

        const friend = await this.userRepository.findOne({
            where: { id: friendId },
            relations: {
                outgoingFriendRequests: true,
                friends: true,
            },
        });

        if (!user) {
            return failure("unregistered user");
        }
        if (!friend) {
            return failure("target user does not exist");
        }

        const len = friend.outgoingFriendRequests.length;
        friend.outgoingFriendRequests = friend.outgoingFriendRequests?.filter(
            (friendReq) => {
                return friendReq.id !== user.id;
            }
        );

        if (friend.outgoingFriendRequests.length === len) {
            return failure("this friend request does not exist");
        }

        user.friends = [...(user.friends ?? []), friend];
        friend.friends = [...(friend.friends ?? []), user];

        await this.userRepository.save(user);
        await this.userRepository.save(friend);

        return success("friend request accept successs");
    }

    async removeFriend(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const id = parseInt(request.params.id);
        const { friendId } = request.body;

        const user = await this.userRepository.findOne({
            where: { id },
            relations: {
                friends: true,
            },
        });

        const friend = await this.userRepository.findOne({
            where: { id: friendId },
            relations: {
                friends: true,
            },
        });

        if (!user) {
            return failure("unregistered user");
        }
        if (!friend) {
            return failure("target user does not exist");
        }

        const len1 = user.friends.length;
        user.friends = user.friends?.filter((friend) => {
            return friend.id !== user.id;
        });

        const len2 = friend.friends.length;
        friend.friends = friend.friends?.filter((friend) => {
            return friend.id !== user.id;
        });

        if (user.friends.length === len1 || friend.friends.length === len2) {
            return failure("this friend does not exist");
        }

        await this.userRepository.save(user);
        await this.userRepository.save(friend);

        return success("friend removed");
    }

    async login(request: Request, response: Response, next: NextFunction) {
        const { email, password } = request.body;

        console.log("jdiowajdiowa");
        console.log({ email, password });

        const user = await this.userRepository.findOne({
            where: {
                email,
            },
            select: ["id", "password", "role"],
        });

        console.log({ user });

        if (!user) {
            return failure("Invalid email or password", 401);
        }

        const verifiedPassword = await validatePassword(
            password,
            user.password
        );

        console.log(verifiedPassword);

        if (!verifiedPassword) {
            return failure("Invalid email or password", 401);
        }

        return success(generateTokens(user));
    }

    async logoutAll(request: Request, response: Response, next: NextFunction) {
        const userTokens = await this.userTokenRepository.findBy({
            user: { id: request.user?.id },
        });
        await this.userTokenRepository.remove(userTokens);
        return success("Successfully logged out");
    }

    async create(request: Request, response: Response, next: NextFunction) {
        const { username, fullName, email, password } = request.body;

        const userExists = !!(await this.userRepository.findOneBy({ email }));

        if (userExists) {
            return failure("User with given email already exist");
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
