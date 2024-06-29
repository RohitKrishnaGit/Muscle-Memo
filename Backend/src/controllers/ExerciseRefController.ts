import { AppDataSource } from "../data-source";
import { NextFunction, Request, Response } from "express";
import { ExerciseRef } from "../entities/ExerciseRef";

export class ExerciseRefController {
    private exerciseRefRepository = AppDataSource.getRepository(ExerciseRef);

    async all(request: Request, response: Response, next: NextFunction) {
        return this.exerciseRefRepository.find();
    }

    async one(request: Request, response: Response, next: NextFunction) {
        const id = parseInt(request.params.id);

        const exerciseRef = await this.exerciseRefRepository.findOneBy({
            id,
        });

        if (!exerciseRef) {
            return "this exerciseRef does not exist";
        }
        return exerciseRef;
    }

    /* Shouldn't need these, temporarily keeping until cleanup can be confirmed */
    // async save(request: Request, response: Response, next: NextFunction) {
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
