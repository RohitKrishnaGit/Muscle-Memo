import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { Exercise } from "../entities/Exercise";
import { failure, success } from "../utils/responseTypes";

export class ExerciseController {
    private exerciseRepository = AppDataSource.getRepository(Exercise);

    async all(request: Request, response: Response, next: NextFunction) {
        const workoutId = parseInt(request.params.workoutId);
        console.log(request.params.userId);
        console.log(await this.exerciseRepository.findBy({
            workout: { id: workoutId },
        }))
        return success(
            this.exerciseRepository.findBy({
                workout: { id: workoutId },
            })
        );
    }

    async one(request: Request, response: Response, next: NextFunction) {
        const workoutId = parseInt(request.params.workoutId);
        const id = parseInt(request.params.id);

        const exercise = await this.exerciseRepository.findOneBy({
            id,
            workout: { id: workoutId },
        });

        if (!exercise) {
            return failure("this exercise does not exist");
        }
        return success(exercise);
    }

    async create(request: Request, response: Response, next: NextFunction) {
        let {
            workoutId,
            exerciseRefId,
            customExerciseRefId,
            exerciseSet,
            templateId,

        } = request.body;


        console.log(request.body);
        const strExerciseSet = JSON.stringify(exerciseSet);
        console.log(strExerciseSet);
        //Check for the references

        const exercise = Object.assign(new Exercise(), {
            workout: { id: workoutId },
            exerciseRef: { id: exerciseRefId?? null },
            customExerciseRef: { id: customExerciseRefId?? null },
            exerciseSet: strExerciseSet,
            template: {id: templateId?? null }
        });
        await this.exerciseRepository.save(exercise)
        return success(true);
    }

    async remove(request: Request, response: Response, next: NextFunction) {
        const workoutId = parseInt(request.params.userId);
        const id = parseInt(request.params.id);

        let exerciseToRemove = await this.exerciseRepository.findOneBy({
            id,
            workout: { id: workoutId },
        });

        if (!exerciseToRemove) {
            return failure("this exercise does not exist");
        }

        await this.exerciseRepository.remove(exerciseToRemove);

        return success("exercise has been removed");
    }
}
