import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { UserPRs } from "../entities/UserPRs";
import { failure, success } from "../utils/responseTypes";
import { User } from "../entities/User";
import { getNthColumnName } from "../utils/dynamicColumnName";

export class UserPRsController {
    private userPRsRepository = AppDataSource.getRepository(UserPRs);

    async getAllUserPRs(request: Request, response: Response, next: NextFunction) {
        const id = parseInt(request.params.id);

        const userPRs = await this.userPRsRepository.findOneBy({
            id
        });

        if (!userPRs) {
            return "unregistered user";
        }

        return userPRs
    }

    async getUserPR(request: Request, response: Response, next: NextFunction) {
        const id = parseInt(request.params.id);
        const exerciseRefId = parseInt(request.params.exerciseRefId);

        const userPRs = await this.userPRsRepository.findOneBy({
            id
        });

        if (!userPRs) {
            return "unregistered user";
        }

        const colName = await getNthColumnName(exerciseRefId, this.userPRsRepository)
        return userPRs[colName as keyof UserPRs];
    }

    async updateUserPR(request: Request, response: Response, next: NextFunction) {
        const id = parseInt(request.params.id);
        const exerciseRefId = parseInt(request.params.exerciseRefId);
        const { PR } = request.body;

        const userPRs = await this.userPRsRepository.findOneBy({
            id
        });

        if (!userPRs) {
            return "unregistered user";
        }

        const colName = await getNthColumnName(exerciseRefId, this.userPRsRepository)
        userPRs[colName as keyof UserPRs] = PR
        return "successful PR update";
    }

    async getTopN(request: Request, response: Response, next: NextFunction) {
        const exerciseRefId = parseInt(request.params.exerciseRefId);
        const count = parseInt(request.params.count);

        const userPRs = await this.userPRsRepository.find({
            take: count,
            order: {
                [getNthColumnName(exerciseRefId, this.userPRsRepository).toString()]: "ASC"
            }
        })

        if (!userPRs) {
            return "no user PRs exist";
        }

        return userPRs;
    }
}