import { AppDataSource } from "../data-source";
import { NextFunction, Request, Response } from "express";
import { Exercise } from "../entity/Exercise";

export class ExerciseController {
    private exerciseRepository = AppDataSource.getRepository(Exercise);

    async all(request: Request, response: Response, next: NextFunction) {
        const workoutId = parseInt(request.params.userId);

        return this.exerciseRepository.findBy({
            workout: { id: workoutId }
        });
    }

    async one(request: Request, response: Response, next: NextFunction) {
        const workoutId = parseInt(request.params.userId);
        const id = parseInt(request.params.id);

        const exercise = await this.exerciseRepository.findOneBy({
            id,
            workout: { id: workoutId }
        });

        if (!exercise) {
            return "this exercise does not exist";
        }
        return exercise;
    }

    async save(request: Request, response: Response, next: NextFunction) {
        const { workoutId, exerciseRefId, customExerciseRefId, sets, reps, weight, duration } = request.body;

        const exercise = Object.assign(new Exercise(), {
            workout: { id: workoutId },
            exerciseRef: { id: exerciseRefId },
            customExerciseRef: { id: customExerciseRefId },
            sets,
            reps,
            weight,
            duration,
        });

        return this.exerciseRepository.save(exercise);
    }

    async remove(request: Request, response: Response, next: NextFunction) {
        const workoutId = parseInt(request.params.userId);
        const id = parseInt(request.params.id);

        let exerciseToRemove = await this.exerciseRepository.findOneBy({
            id,
            workout: { id: workoutId }
        });

        if (!exerciseToRemove) {
            return "this exercise does not exist";
        }

        await this.exerciseRepository.remove(exerciseToRemove);

        return "exercise has been removed";
    }
}
