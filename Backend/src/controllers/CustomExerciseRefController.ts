import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { CustomExerciseRef } from "../entities/CustomExerciseRef";
import { User } from "../entities/User";
import { failure, success } from "../utils/responseTypes";

export class CustomExerciseRefController {
    private customExerciseRefRepository =
        AppDataSource.getRepository(CustomExerciseRef);

    async all(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);

        return success(
            this.customExerciseRefRepository.findBy({
                user: { id: userId },
            })
        );
    }

    async one(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const id = parseInt(request.params.id);

        const customExerciseRef =
            await this.customExerciseRefRepository.findOneBy({
                id,
                user: { id: userId },
            });

        if (!customExerciseRef) {
            return failure("this customExerciseRef does not exist");
        }
        return success(customExerciseRef);
    }

    async create(request: Request, response: Response, next: NextFunction) {
        const { name, userId } = request.body;

        const customExerciseRef = Object.assign(new CustomExerciseRef(), {
            name,
            user: { id: userId } as User,
        });

        return success(
            this.customExerciseRefRepository.save(customExerciseRef)
        );
    }

    async remove(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const id = parseInt(request.params.id);

        let customExerciseRefToRemove =
            await this.customExerciseRefRepository.findOneBy({
                id,
                user: { id: userId },
            });

        if (!customExerciseRefToRemove) {
            return failure("this customExerciseRef does not exist");
        }

        await this.customExerciseRefRepository.remove(
            customExerciseRefToRemove
        );

        return success("customExerciseRef has been removed");
    }
}
