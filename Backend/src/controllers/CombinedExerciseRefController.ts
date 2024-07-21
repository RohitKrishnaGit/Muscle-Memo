import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { CustomExerciseRef } from "../entities/CustomExerciseRef";
import { ExerciseRef } from "../entities/ExerciseRef";
import { success } from "../utils/responseTypes";

export class CombinedExerciseRefController {
    private exerciseRefRepository = AppDataSource.getRepository(ExerciseRef);
    private customExerciseRefRepository =
        AppDataSource.getRepository(CustomExerciseRef);

    async all(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);

        const exerciseRefs = (await this.exerciseRefRepository.find()).map(
            (ref) => ({ ...ref, isCustom: false })
        );
        const customExerciseRefs = (
            await this.customExerciseRefRepository.findBy({
                user: { id: userId },
            })
        ).map((ref) => ({ ...ref, isCustom: true }));

        return success(
            [...exerciseRefs, ...customExerciseRefs].sort((a, b) => {
                if (a.name < b.name) return -1;
                if (a.name > b.name) return 1;
                return 0;
            })
        );
    }
}
