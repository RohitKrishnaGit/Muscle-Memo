import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { User } from "../entities/User";
import { Workout } from "../entities/Workout";
import { failure, success } from "../utils/responseTypes";

export class WorkoutController {
    private workoutRepository = AppDataSource.getRepository(Workout);

    async all(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);

        return success(
            this.workoutRepository.findBy({
                user: { id: userId },
            })
        );
    }

    async one(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const id = parseInt(request.params.id);

        const workout = await this.workoutRepository.findOneBy({
            user: { id: userId },
            id,
        });

        if (!workout) {
            return failure("this workout does not exist");
        }
        return success(workout);
    }

    async create(request: Request, response: Response, next: NextFunction) {
        const { name, userId } = request.body;

        const workout = Object.assign(new Workout(), {
            name,
            user: { id: userId } as User,
        });

        return success(this.workoutRepository.save(workout));
    }

    async remove(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const id = parseInt(request.params.id);

        let workoutToRemove = await this.workoutRepository.findOneBy({
            id,
            user: { id: userId },
        });

        if (!workoutToRemove) {
            return failure("this workout does not exist");
        }

        await this.workoutRepository.remove(workoutToRemove);

        return success("workout has been removed");
    }
}