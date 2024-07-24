import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { User } from "../entities/User";
import { failure, success } from "../utils/responseTypes";

export class FriendsController {
    private userRepository = AppDataSource.getRepository(User);

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
            return failure("Target user does not exist");
        }

        if (friend.id === user.id) {
            return failure("Cannot send friend request to self");
        }

        if (user.outgoingFriendRequests.includes(friend)) {
            return failure("Friend request already sent to this user");
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
            return failure("Target user does not exist");
        }

        const len = friend.outgoingFriendRequests.length;
        friend.outgoingFriendRequests = friend.outgoingFriendRequests?.filter(
            (friendReq) => {
                return friendReq.id !== user.id;
            }
        );

        if (friend.outgoingFriendRequests.length === len) {
            return failure("This friend request does not exist");
        }

        user.friends = [...(user.friends ?? []), friend];
        friend.friends = [...(friend.friends ?? []), user];

        await this.userRepository.save(user);
        await this.userRepository.save(friend);

        return success("Friend request accept successs");
    }

    async rejectFriendRequest(
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
            return failure("Target user does not exist");
        }

        const len = friend.outgoingFriendRequests.length;
        friend.outgoingFriendRequests = friend.outgoingFriendRequests?.filter(
            (friendReq) => {
                return friendReq.id !== user.id;
            }
        );

        if (friend.outgoingFriendRequests.length === len) {
            return failure("This friend request does not exist");
        }

        await this.userRepository.save(user);
        await this.userRepository.save(friend);

        return success("Friend request accept successs");
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
            return failure("Target user does not exist");
        }

        const len1 = user.friends.length;
        user.friends = user.friends?.filter((userFriend) => {
            return userFriend.id !== friendId;
        });

        const len2 = friend.friends.length;
        friend.friends = friend.friends?.filter((friendFriend) => {
            return friendFriend.id !== userId;
        });

        if (user.friends.length === len1 || friend.friends.length === len2) {
            return failure("This friend does not exist");
        }

        await this.userRepository.save(user);
        await this.userRepository.save(friend);

        return success("Friend removed");
    }
}
