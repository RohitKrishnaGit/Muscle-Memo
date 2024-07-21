import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { User } from "../entities/User";
import { UserToken } from "../entities/UserToken";
import { generatePasswordHash, validatePassword } from "../utils/password";
import { failure, success } from "../utils/responseTypes";
import { generateTokens } from "../utils/token";
import { AllowedStatistics } from "../entities/AllowedStatistics";
import { UserPrs } from "../entities/UserPrs";

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
        const userId = parseInt(request.params.userId);

        const user = await this.userRepository.findOne({
            where: { id: userId },
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
        const userId = parseInt(request.params.userId);

        const user = await this.userRepository.findOne({
            where: { id: userId },
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
        const userId = parseInt(request.params.userId);

        const user = await this.userRepository.findOne({
            where: { id: userId },
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
        const userId = parseInt(request.params.userId);
        const { friendId } = request.body;

        const user = await this.userRepository.findOne({
            where: { id: userId },
            relations: {
                outgoingFriendRequests: true,
            },
        });

        const friend = await this.userRepository.findOneBy({
            id: friendId,
        });

        if (!user) {
            return failure("unregistered user");
        }

        if (!friend) {
            return failure("target user does not exist");
        }

        if (friend.id === user.id) {
            return failure("cannot send friend request to self");
        }

        if (user.outgoingFriendRequests.includes(friend)) {
            return failure("friend request already sent to this user");
        }

        user.outgoingFriendRequests = [
            ...(user.outgoingFriendRequests ?? []),
            friend,
        ];

        return success(this.userRepository.save(user));
    }

    async acceptFriendRequest(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const userId = parseInt(request.params.userId);
        const { friendId } = request.body;

        const user = await this.userRepository.findOne({
            where: { id: userId },
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
        const userId = parseInt(request.params.userId);
        const { friendId } = request.body;

        const user = await this.userRepository.findOne({
            where: { id: userId },
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

        const user = await this.userRepository.findOne({
            where: {
                email,
            },
            select: ["id", "password", "role"],
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
        const { username, email, password, gender, experience } = request.body;

        const userExists = !!(await this.userRepository.findOneBy({ email }));

        if (userExists) {
            return failure("User with given email already exist");
        }

        const user = Object.assign(new User(), {
            username,
            email,
            password: await generatePasswordHash(password),
            gender,
            experience,
            userPrs: new UserPrs(),
            allowedStatistics: new AllowedStatistics(),
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

    async update(request: Request, response: Response, next: NextFunction) {
        const { username, gender, experience } = request.body;
        const id = parseInt(request.params.userId);
        let userToUpdate = await this.userRepository.findOneBy({
            id,
        });
        if (!userToUpdate) return failure("Update failed");
        return success(
            this.userRepository.save({
                ...userToUpdate,
                username,
                gender,
                experience,
            })
        );
    }

    async findEmail(request: Request, response: Response, next: NextFunction) {
        const email = request.params.email;
        let users = await this.userRepository.find({
            where: {
                email: email,
            },
        });
        if (users.length != 0) {
            return success(true);
        }
        return success(false);
    }
}
