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

    async getFriends(request: Request, response: Response, next: NextFunction) {
        const id = parseInt(request.params.id);

        const user = await this.userRepository.findOne({
            where: { id },
            relations: {
                friends: true,
            }
        });

        if (!user) {
            return "unregistered user";
        }
        return user.friends;
    }

    async getIncomingFriendRequests(request: Request, response: Response, next: NextFunction) {
        const id = parseInt(request.params.id);

        const user = await this.userRepository.findOne({
            where: { id },
            relations: {
                incomingFriendRequests: true,
            }
        });

        if (!user) {
            return "unregistered user";
        }
        return user.incomingFriendRequests;
    }

    async getOutgoingFriendRequests(request: Request, response: Response, next: NextFunction) {
        const id = parseInt(request.params.id);

        const user = await this.userRepository.findOne({
            where: { id },
            relations: {
                outgoingFriendRequests: true,
            }
        });

        if (!user) {
            return "unregistered user";
        }
        return user.outgoingFriendRequests;
    }

    async sendFriendRequest(request: Request, response: Response, next: NextFunction) {
        const id = parseInt(request.params.id);
        const { friendId } = request.body;

        const user = await this.userRepository.findOne({
            where: { id },
            relations: {
                outgoingFriendRequests: true,
            }
        });

        const friend = await this.userRepository.findOneBy({
            id: friendId,
        });

        if (!user) {
            return "unregistered user";
        }

        if (!friend) {
            return "target user does not exist";
        }

        if (friend.id === user.id) {
            return "cannot send friend request to self"
        }

        user.outgoingFriendRequests = [...user.outgoingFriendRequests ?? [], friend]

        return this.userRepository.save(user)
    }

    async acceptFriendRequest(request: Request, response: Response, next: NextFunction) {
        const id = parseInt(request.params.id);
        const { friendId } = request.body;

        const user = await this.userRepository.findOne({
            where: { id },
            relations: {
                friends: true
            }
        });

        const friend = await this.userRepository.findOne({
            where: { id: friendId },
            relations: {
                outgoingFriendRequests: true,
                friends: true
            }
        });

        if (!user) {
            return "unregistered user";
        }
        if (!friend) {
            return "target user does not exist";
        }

        const len = friend.outgoingFriendRequests.length
        friend.outgoingFriendRequests = friend.outgoingFriendRequests?.filter((friendReq) => {
            return friendReq.id !== user.id
        })

        if (friend.outgoingFriendRequests.length === len) {
            return "this friend request does not exist"
        }

        user.friends = [...user.friends ?? [], friend]
        friend.friends = [...friend.friends ?? [], user]
        
        await this.userRepository.save(user)
        await this.userRepository.save(friend)

        return "friend request accept successs"
    }

    async removeFriend(request: Request, response: Response, next: NextFunction) {
        const id = parseInt(request.params.id);
        const { friendId } = request.body;

        const user = await this.userRepository.findOne({
            where: { id },
            relations: {
                friends: true
            }
        });

        const friend = await this.userRepository.findOne({
            where: { id: friendId },
            relations: {
                friends: true
            }
        });

        if (!user) {
            return "unregistered user";
        }
        if (!friend) {
            return "target user does not exist";
        }

        const len1 = user.friends.length
        user.friends = user.friends?.filter((friend) => {
            return friend.id !== user.id
        })

        const len2 = friend.friends.length
        friend.friends = friend.friends?.filter((friend) => {
            return friend.id !== user.id
        })

        if (
            user.friends.length === len1 ||
            friend.friends.length === len2
        ) {
            return "this friend does not exist"
        }
        
        await this.userRepository.save(user)
        await this.userRepository.save(friend)

        return "friend removed"
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
            response.status(401);
            return "Invalid email or password";
        }

        const verifiedPassword = await validatePassword(
            password,
            user.password
        );

        if (!verifiedPassword) {
            response.status(401);
            return "Invalid email or password";
        }

        // const { accessToken, refreshToken } = await ;

        return generateTokens(user);
    }

    async logout(request: Request, response: Response, next: NextFunction) {
        const userToken = await this.userTokenRepository.findOneBy({
            token: request.body.refreshToken,
        });
        if (!userToken) return "Logged Out Sucessfully";

        await this.userTokenRepository.delete(userToken);

        return "Logged Out Sucessfully";
    }

    async create(request: Request, response: Response, next: NextFunction) {
        const { username, fullName, email, password } = request.body;

        const userExists = !!(await this.userRepository.findOneBy([
            { username },
            { email },
        ]));

        if (userExists) {
            response.status(400);
            return "User with given username or email already exist";
        }

        const user = Object.assign(new User(), {
            username,
            fullName,
            email,
            password: await generatePasswordHash(password),
        });

        await this.userRepository.save(user);

        return "User created";
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
