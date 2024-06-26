import { AppDataSource } from "../data-source";
import { NextFunction, Request, Response } from "express";
import { Template } from "../entity/Template";
import { User } from "../entity/User";

export class TemplateController {
    private templateRepository = AppDataSource.getRepository(Template);

    async all(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId)

        return this.templateRepository.findBy({
            user: { id: userId }
        });
    }

    async one(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId)
        const id = parseInt(request.params.id);

        const template = await this.templateRepository.findOneBy({
            user: { id: userId },
            id,
        });

        if (!template) {
            return "this template does not exist";
        }
        return template;
    }

    async save(request: Request, response: Response, next: NextFunction) {
        const { name, userId } = request.body;

        const template = Object.assign(new Template(), {
            name,
            user: { id: userId } as User,
        });

        return this.templateRepository.save(template);
    }

    async remove(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId)
        const id = parseInt(request.params.id);

        let templateToRemove = await this.templateRepository.findOneBy({
            id,
            user: { id: userId },
        });

        if (!templateToRemove) {
            return "this template does not exist";
        }

        await this.templateRepository.remove(templateToRemove);

        return "template has been removed";
    }
}
