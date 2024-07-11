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

    /* Shouldn't need these, temporarily keeping until cleanup can be confirmed */
    // async create(request: Request, response: Response, next: NextFunction) {
    //     const { name } = request.body;

    //     const exerciseRef = Object.assign(new ExerciseRef(), {
    //         name,
    //     });

    //     return this.exerciseRefRepository.save(exerciseRef);
    // }

    // async remove(request: Request, response: Response, next: NextFunction) {
    //     const id = parseInt(request.params.id);

    //     let exerciseRefToRemove = await this.exerciseRefRepository.findOneBy({ id });

    //     if (!exerciseRefToRemove) {
    //         return "this exerciseRef does not exist";
    //     }

    //     await this.exerciseRefRepository.remove(exerciseRefToRemove);

    //     return "exerciseRef has been removed";
    // }
}
