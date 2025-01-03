import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { User } from "../entities/User";
import { Workout } from "../entities/Workout";
import { failure, success } from "../utils/responseTypes";

export class WorkoutController {
    private workoutRepository = AppDataSource.getRepository(Workout);

    async all(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const data = (
            await this.workoutRepository.find({
                where: { user: { id: userId } },
                relations: {
                    exercises: { exerciseRef: true, customExerciseRef: true },
                },
            })
        ).map((workout) => ({
            ...workout,
            exercises: workout.exercises.map((exercise) => ({
                ...exercise,
                exerciseRef: exercise.exerciseRef ?? exercise.customExerciseRef,
                exerciseSet: JSON.parse(exercise.exerciseSet),
            })),
        }));
        return success(data);
    }

    async one(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const id = parseInt(request.params.id);

        const workout = await this.workoutRepository.findOne({
            where: {
                user: { id: userId },
                id,
            },
            relations: {
                exercises: { exerciseRef: true, customExerciseRef: true },
            },
        });

        if (!workout) {
            return failure("this workout does not exist");
        }
        return success({
            ...workout,
            exercises: workout.exercises.map((exercise) => ({
                ...exercise,
                exerciseRef: exercise.exerciseRef ?? exercise.customExerciseRef,
                exerciseSet: JSON.parse(exercise.exerciseSet),
            })),
        });
    }

    async create(request: Request, response: Response, next: NextFunction) {
        const { name, userId, date, duration } = request.body;

        const workout = Object.assign(new Workout(), {
            name,
            date,
            duration,
            user: { id: userId } as User,
        });

        const newWorkout = await this.workoutRepository.save(workout);

        return success({ workoutId: newWorkout.id });
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
