import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { ExerciseRef } from "../entities/ExerciseRef";
import { failure, success } from "../utils/responseTypes";

export class ExerciseRefController {
    private exerciseRefRepository = AppDataSource.getRepository(ExerciseRef);

    async all(request: Request, response: Response, next: NextFunction) {
        return success(this.exerciseRefRepository.find());
    }

    async one(request: Request, response: Response, next: NextFunction) {
        const id = parseInt(request.params.id);

        const exerciseRef = await this.exerciseRefRepository.findOneBy({
            id,
        });

        if (!exerciseRef) {
            return failure("this exerciseRef does not exist");
        }
        return success(exerciseRef);
    }
}
