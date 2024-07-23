import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { User } from "../entities/User";
import { failure, success } from "../utils/responseTypes";
import { PublicWorkout } from "../entities/PublicWorkout";
import { PublicWorkoutRequest } from "../entities/PublicWorkoutRequest";

export class FriendsController {
    private userRepository = AppDataSource.getRepository(User);
    private publicWorkoutRepository = AppDataSource.getRepository(PublicWorkout);
    private publicWorkoutRequestRepository = AppDataSource.getRepository(PublicWorkoutRequest)

    async getPublicWorkouts(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);

        const user = await this.userRepository.findOneBy({
            id: userId,
        });

        if (!user) {
            return failure("unregistered user");
        }
        return success(user.publicWorkouts);
    }

    async getIncomingPublicWorkoutRequests(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const userId = parseInt(request.params.userId);

        return success(this.publicWorkoutRequestRepository.find({
            where:{
                publicWorkout: {creator: {id: userId}}
            },
            relations: {
                publicWorkout: true,
            }
        }));

    }

    async getOutgoingPublicWorkoutRequests(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const userId = parseInt(request.params.userId);

        return success(this.publicWorkoutRequestRepository.find({
            where:{
                sender: {id: userId}
            },
            relations: {
                sender: true
            }
        }));
    }

    async sendPublicWorkoutRequest(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const userId = parseInt(request.params.userId);
        const { publicWorkoutId } = request.body;

        const user = await this.userRepository.findOne({
            where: { id: userId },
        });

        if (!user) {
            return failure("unregistered user");
        }

        const publicWorkout = await this.publicWorkoutRepository.findOneBy({
            id: publicWorkoutId,
        });

        if (!publicWorkout) {
            return failure("public workout does not exist");
        }

        const publicWorkoutRequest = Object.assign(new PublicWorkoutRequest(), {
            sender: user,
            publicWorkout,
        });

        await this.publicWorkoutRequestRepository.save(publicWorkoutRequest);

        return success("Public workout request created");
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

    async acceptPublicWorkoutRequest(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const userId = parseInt(request.params.userId);
        const { senderId } = request.body;

        const user = await this.userRepository.findOne({
            where: { id: userId },
            relations: {
                publicWorkouts: true,
            },
        });

        const friend = await this.userRepository.findOne({
            where: { id: friendId },
            relations: {
                outgoingPublicWorkoutRequests: true,
                publicWorkouts: true,
            },
        });

        if (!user) {
            return failure("unregistered user");
        }
        if (!friend) {
            return failure("target user does not exist");
        }

        const len = friend.outgoingPublicWorkoutRequests.length;
        friend.outgoingPublicWorkoutRequests = friend.outgoingPublicWorkoutRequests?.filter(
            (friendReq) => {
                return friendReq.id !== user.id;
            }
        );

        if (friend.outgoingPublicWorkoutRequests.length === len) {
            return failure("this public workout join request does not exist");
        }

        friend.publicWorkouts = [...(friend.publicWorkouts ?? []), user];

        await this.userRepository.save(user);
        await this.userRepository.save(friend);

        return success("friend request accept successs");
    }

    async rejectPublicWorkoutRequest(
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

        await this.userRepository.save(user);
        await this.userRepository.save(friend);

        return success("friend request accept successs");
    }

    async removePublicWorkout(
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
        user.friends = user.friends?.filter((userFriend) => {
            return userFriend.id !== friendId;
        });

        const len2 = friend.friends.length;
        friend.friends = friend.friends?.filter((friendFriend) => {
            return friendFriend.id !== userId;
        });

        if (user.friends.length === len1 || friend.friends.length === len2) {
            return failure("this friend does not exist");
        }

        await this.userRepository.save(user);
        await this.userRepository.save(friend);

        return success("friend removed");
    }
}
