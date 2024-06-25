import { AppDataSource } from "../data-source";
import { NextFunction, Request, Response } from "express";
import { Workout } from "../entity/Workout";
import { User } from "../entity/User";

export class WorkoutController {
    private workoutRepository = AppDataSource.getRepository(Workout);

    async all(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId)

        return this.workoutRepository.findBy({
            user: { id: userId }
        });
    }

    async one(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId)
        const id = parseInt(request.params.id);

        const workout = await this.workoutRepository.findOneBy({
            user: { id: userId },
            id,
        });

        if (!workout) {
            return "this workout does not exist";
        }
        return workout;
    }

    async save(request: Request, response: Response, next: NextFunction) {
        const { name, userId } = request.body;

        const workout = Object.assign(new Workout(), {
            name,
            user: { id: userId } as User,
        });

        return this.workoutRepository.save(workout);
    }

    async remove(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId)
        const id = parseInt(request.params.id);

        let workoutToRemove = await this.workoutRepository.findOneBy({
            id,
            user: { id: userId },
        });

        if (!workoutToRemove) {
            return "this workout does not exist";
        }

        await this.workoutRepository.remove(workoutToRemove);

        return "workout has been removed";
    }
}
