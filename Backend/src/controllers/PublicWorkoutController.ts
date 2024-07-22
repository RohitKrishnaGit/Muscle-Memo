import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { User } from "../entities/User";
import { failure, success } from "../utils/responseTypes";
import { PublicWorkout } from "../entities/PublicWorkout";

export class PublicWorkoutController {
    private publicWorkoutRepository = AppDataSource.getRepository(PublicWorkout);

    async all(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);

        return success(
            this.publicWorkoutRepository.findBy({
                creator: { id: userId },
            })
        );
    }

    async one(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const id = parseInt(request.params.id);

        const publicWorkout = await this.publicWorkoutRepository.findOneBy({
            creator: { id: userId },
            id,
        });

        if (!publicWorkout) {
            return failure("this public workout does not exist");
        }
        return success(publicWorkout);
    }

    async create(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const { name, date, location, description, gender, experience } = request.body;

        const publicWorkout = Object.assign(new PublicWorkout(), {
            name,
            date,
            location,
            description,
            gender,
            experience,
            creator: { id: userId } as User,
        });

        const newPublicWorkout = await this.publicWorkoutRepository.save(publicWorkout);

        return success({ publicWorkoutId: newPublicWorkout.id });
    }

    async filterGender(request: Request, response: Response, next: NextFunction) {
        const { gender } = request.body;

        const publicWorkout = await this.publicWorkoutRepository.findBy({
            gender,
        });

        if (!publicWorkout || publicWorkout.length == 0) {
            return failure("no workouts exist with the filter");
        }
        return success(publicWorkout);
    }

    async filterExperience(request: Request, response: Response, next: NextFunction) {
        const { experience } = request.body;

        const publicWorkout = await this.publicWorkoutRepository.findBy({
            experience,
        });

        if (!publicWorkout || publicWorkout.length == 0) {
            return failure("no workouts exist with the filter");
        }
        return success(publicWorkout);
    }

    async remove(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const id = parseInt(request.params.id);

        let publicWorkoutToRemove = await this.publicWorkoutRepository.findOneBy({
            id,
            creator: { id: userId },
        });

        if (!publicWorkoutToRemove) {
            return failure("this public workout does not exist");
        }

        await this.publicWorkoutRepository.remove(publicWorkoutToRemove);

        return success("public workout has been removed");
    }
}
