import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { AllowedStatistics } from "../entities/AllowedStatistics";
import { failure, success } from "../utils/responseTypes";
import { getNthColumnName } from "../utils/dynamicColumnName";

export class AllowedStatisticsController {
    private allowedStatisticsRepository = AppDataSource.getRepository(AllowedStatistics);

    async getAllowedStatistics(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const exerciseRefId = parseInt(request.params.exerciseRefId);

        const allowedStatistics = await this.allowedStatisticsRepository.findOneBy({
            user: { id: userId },
        });

        if (!allowedStatistics) {
            return failure("unregistered user");
        }

        const colName = await getNthColumnName(exerciseRefId,this.allowedStatisticsRepository)
        return success(allowedStatistics[colName as keyof AllowedStatistics]);
    }

    async updateAllowedStatistics(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const exerciseRefId = parseInt(request.params.exerciseRefId);
        const { allowedValue } = request.body;

        const allowedStatistics = await this.allowedStatisticsRepository.findOneBy({
            user: { id: userId },
        });

        if (!allowedStatistics) {
            return failure("unregistered user");
        }

        const colName = await getNthColumnName(exerciseRefId,this.allowedStatisticsRepository);

        await this.allowedStatisticsRepository.save({
            ...allowedStatistics,
            [colName]: allowedValue
        });

        return success("successful allowed PR update");
    }
}