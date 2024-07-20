import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { AllowedStatistics } from "../entities/AllowedStatistics";
import { failure, success } from "../utils/responseTypes";
import { getNthColumnName } from "../utils/dynamicColumnName";

export class AllowedStatisticsController {
    private allowedStatisticsRepository = AppDataSource.getRepository(AllowedStatistics);

    async getAllowedStatistics(request: Request, response: Response, next: NextFunction) {
        const id = parseInt(request.params.id);
        const exerciseRefId = parseInt(request.params.exerciseRefId);

        const allowedStatistics = await this.allowedStatisticsRepository.findOneBy({
            id
        });

        if (!allowedStatistics) {
            return failure("unregistered user");
        }

        const colName = await getNthColumnName(exerciseRefId,this.allowedStatisticsRepository)
        return success(allowedStatistics[colName as keyof AllowedStatistics]);
    }

    async updateAllowedStatistics(request: Request, response: Response, next: NextFunction) {
        const id = parseInt(request.params.id);
        const exerciseRefId = parseInt(request.params.exerciseRefId);
        const { allowedValue } = request.body;

        const allowedStatistics = await this.allowedStatisticsRepository.findOneBy({
            id
        });

        if (!allowedStatistics) {
            return failure("unregistered user");
        }

        const colName = await getNthColumnName(exerciseRefId,this.allowedStatisticsRepository);

        await this.allowedStatisticsRepository.save({
            ...allowedStatistics,
            [colName]: allowedValue
        });

        //(allowedStatistics as Record<keyof AllowedStatistics,any>)[colName as keyof AllowedStatistics] = allowedValue;

        return success("successful PR update");
    }
}