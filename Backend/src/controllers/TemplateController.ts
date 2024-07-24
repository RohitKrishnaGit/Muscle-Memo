import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { Template } from "../entities/Template";
import { User } from "../entities/User";
import { failure, success } from "../utils/responseTypes";

export class TemplateController {
    private templateRepository = AppDataSource.getRepository(Template);

    async all(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const data = (
            await this.templateRepository.find({
                where: { user: { id: userId } },

                relations: {
                    exercises: { exerciseRef: true, customExerciseRef: true },
                },
            })
        ).map((workout) => ({
            ...workout,
            exercises: workout.exercises.map((exercise) => ({
                ...exercise,
                exerciseRef: {
                    ...(exercise.exerciseRef ?? exercise.customExerciseRef),
                    isCustom: !!exercise.customExerciseRef,
                },
                exerciseSet: JSON.parse(exercise.exerciseSet),
            })),
        }));
        return success(data);
    }

    async one(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const id = parseInt(request.params.id);

        const template = await this.templateRepository.findOne({
            where: {
                user: { id: userId },
                id,
            },
            relations: {
                exercises: { exerciseRef: true, customExerciseRef: true },
            },
        });

        if (!template) {
            return failure("this template does not exist");
        }
        return success({
            ...template,
            exercises: template.exercises.map((exercise) => ({
                ...exercise,
                exerciseSet: JSON.parse(exercise.exerciseSet),
            })),
        });
    }

    async create(request: Request, response: Response, next: NextFunction) {
        const { name, userId } = request.body;

        const template = Object.assign(new Template(), {
            name,
            user: { id: userId } as User,
        });

        const newTemplate = await this.templateRepository.save(template);

        return success({ templateId: newTemplate.id });
    }

    async remove(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const id = parseInt(request.params.id);

        let templateToRemove = await this.templateRepository.findOneBy({
            id,
            user: { id: userId },
        });

        if (!templateToRemove) {
            return failure("this template does not exist");
        }

        await this.templateRepository.remove(templateToRemove);

        return success("template has been removed");
    }
}
