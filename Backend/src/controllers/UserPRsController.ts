import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { UserPrs } from "../entities/UserPrs";
import { failure, success } from "../utils/responseTypes";
import { getNthColumnName } from "../utils/dynamicColumnName";
import { AllowedStatistics } from "../entities/AllowedStatistics";
import { User } from "../entities/User";

export class UserPrsController {
    private userPrsRepository = AppDataSource.getRepository(UserPrs);
    private userRepository = AppDataSource.getRepository(User);

    async getAllUserPrs(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);

        const userPrs = await this.userPrsRepository.findOneBy({
            user: { id: userId },
        });

        if (!userPrs) {
            return failure("unregistered user");
        }

        return success(userPrs);
    }

    async getUserPr(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const exerciseRefId = parseInt(request.params.exerciseRefId);

        const userPrs = await this.userPrsRepository.findOneBy({
            user: { id: userId },
        });

        if (!userPrs) {
            return failure("unregistered user");
        }

        const colName = await getNthColumnName(exerciseRefId, this.userPrsRepository)
        return success(userPrs[colName as keyof UserPrs]);
    }

    async updateUserPr(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const exerciseRefId = parseInt(request.params.exerciseRefId);
        const { Pr } = request.body;

        const userPrs = await this.userPrsRepository.findOneBy({
            user: { id: userId },
        });

        if (!userPrs) {
            return failure("unregistered user");
        }
        
        const colName = await getNthColumnName(exerciseRefId, this.userPrsRepository);

        await this.userPrsRepository.save({
            ...userPrs,
            [colName]: Pr
        });

        return success("successful PR update");
    }

    async getTopN(request: Request, response: Response, next: NextFunction) {
        const exerciseRefId = parseInt(request.params.exerciseRefId);
        const count = parseInt(request.params.count);
        const colName = await getNthColumnName(exerciseRefId, this.userPrsRepository);

        const userPrs = await this.userPrsRepository.createQueryBuilder('userPrs')
            .select([
                'userPrs.userId',
                `userPrs.${colName} AS pr`,
                'user.username AS username' 
            ])
            .innerJoin(
                AllowedStatistics,
                'allowedStatistics',
                `userPrs.userId = allowedStatistics.userId AND allowedStatistics.${colName} = true`
            )
            .orderBy('userPrs.' + colName, 'DESC')
            .take(count)
            .innerJoin(
                User,
                'user',
                `userPrs.userId = user.id`
            )
            .getRawMany();

        if (userPrs.length === 0) {
            return failure("No user PRs exist");
        }

        return success(userPrs);
    }

    async getTopNFriends(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const exerciseRefId = parseInt(request.params.exerciseRefId);
        const count = parseInt(request.params.count);

        const colName = await getNthColumnName(exerciseRefId, this.userPrsRepository);

        const user = await this.userRepository.findOne({
            where: { id: userId },
            relations: {
                friends: true,
            },
        });

        if (!user) {
            return failure("unregistered user");
        }

        const userIds = [...user.friends, user].map((entry) => entry.id);

        const userPrs = await this.userPrsRepository.createQueryBuilder('userPrs')
            .select([
                'userPrs.userId',
                `userPrs.${colName} AS pr`,
                'user.username AS username' 
            ])
            .where('userPrs.userId IN (:...userIdList)', {userIdList: userIds})
            .innerJoin(
                AllowedStatistics,
                'allowedStatistics',
                `userPrs.userId = allowedStatistics.userId AND allowedStatistics.${colName} = true`
            )
            .orderBy('userPrs.' + colName, 'DESC')
            .take(count)
            .innerJoin(
                User,
                'user',
                `userPrs.userId = user.id`
            )
            .getRawMany();

        if (userPrs.length === 0) {
            return failure("No user PRs exist");
        }

        return success(userPrs);
    }
}