import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { User } from "../entities/User";
import { failure, success } from "../utils/responseTypes";
import { PublicWorkout } from "../entities/PublicWorkout";
import { PublicWorkoutRequest } from "../entities/PublicWorkoutRequest";

export class PublicWorkoutRequestController {
    private userRepository = AppDataSource.getRepository(User);
    private publicWorkoutRepository = AppDataSource.getRepository(PublicWorkout);
    private publicWorkoutRequestRepository = AppDataSource.getRepository(PublicWorkoutRequest)

    async getIncomingPublicWorkoutRequests(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const userId = parseInt(request.params.userId);

        return success(this.publicWorkoutRequestRepository.find({
            where: {
                publicWorkout: { creator: { id: userId } }
            },
            relations: {
                publicWorkout: true,
                sender: true,
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
            where: {
                sender: { id: userId }
            },
            relations: {
                publicWorkout: true,
                sender: true,
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

        const publicWorkout = await this.publicWorkoutRepository.findOne({
            where: {
                id: publicWorkoutId,
            },
            relations: {
                creator: true,
                users: true,
            }
        });

        const existingPublicWorkoutRequest = await this.publicWorkoutRequestRepository.findOne({
            where: {
                publicWorkout: { id: publicWorkoutId },
                sender: { id: userId }
            },
            relations: {
                publicWorkout: true
            }
        })

        if (!publicWorkout) {
            return failure("public workout does not exist");
        }

        if (user.id == publicWorkout.creator.id) {
            return failure("can't join own public workout")
        }

        if (!!existingPublicWorkoutRequest) {
            return failure("already requested to join this workout")
        }

        const publicWorkoutRequest = Object.assign(new PublicWorkoutRequest(), {
            sender: user,
            publicWorkout,
        });

        await this.publicWorkoutRequestRepository.save(publicWorkoutRequest);

        return success("Public workout request created");
    }

    async acceptPublicWorkoutRequest(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const id = parseInt(request.params.id);

        const publicWorkoutRequest = await this.publicWorkoutRequestRepository.findOneBy({
            id
        });

        if (!publicWorkoutRequest) {
            return failure("invalid public workout request");
        }

        const publicWorkout = await this.publicWorkoutRepository.findOneBy({
            id: publicWorkoutRequest.publicWorkout.id
        })

        if (!publicWorkout) {
            return failure("invalid public workout")
        }

        publicWorkout.users = [...(publicWorkout.users ?? []), publicWorkoutRequest.sender];

        await this.publicWorkoutRequestRepository.remove(publicWorkoutRequest)
        await this.publicWorkoutRepository.save(publicWorkout);

        return success("public workout request accept successs");
    }

    async rejectPublicWorkoutRequest(
        request: Request,
        response: Response,
        next: NextFunction
    ) {
        const id = parseInt(request.params.id);

        const publicWorkoutRequest = await this.publicWorkoutRequestRepository.findOneBy({
            id
        });

        if (!publicWorkoutRequest) {
            return failure("invalid public workout request");
        }

        await this.publicWorkoutRequestRepository.remove(publicWorkoutRequest)

        return success("public workout request removed");
    }
}
