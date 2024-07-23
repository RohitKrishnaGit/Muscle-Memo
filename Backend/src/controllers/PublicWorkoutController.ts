import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { PublicWorkout } from "../entities/PublicWorkout";
import { User } from "../entities/User";
import { failure, success } from "../utils/responseTypes";
import { geoFire } from "../utils/location";
import { ArrayContains } from "typeorm";

export class PublicWorkoutController {
    private publicWorkoutRepository =
        AppDataSource.getRepository(PublicWorkout);
    private userRepository = AppDataSource.getRepository(User);

    async all(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);

        return success(
            this.publicWorkoutRepository.findBy({
                creator: { id: userId },
            })
        );
    }

    async one(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const id = parseInt(request.params.id);

        const publicWorkout = await this.publicWorkoutRepository.findOneBy({
            creator: { id: userId },
            id,
        });

        if (!publicWorkout) {
            return failure("this public workout does not exist");
        }
        return success(publicWorkout);
    }

    async create(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const { name, date, latitude, longitude, description, gender, experience } = request.body;

        const publicWorkout = Object.assign(new PublicWorkout(), {
            name,
            date,
            latitude,
            longitude,
            description,
            gender,
            experience,
            creator: { id: userId } as User,
        });

        const newPublicWorkout = await this.publicWorkoutRepository.save(
            publicWorkout
        );

        geoFire.set(newPublicWorkout.id.toString(), [+latitude, +longitude]).then(function () {
            console.log("Provided key has been added to GeoFire");
        }, function (error) {
            console.log("Error: " + error);
        });

        return success({ publicWorkoutId: newPublicWorkout.id });
    }

    async filter(request: Request, response: Response, next: NextFunction) {
        const { userId, gender, experience, friendsOnly, latitude, longitude } = request.body;

        const nearby: string[] = []

        const user = await this.userRepository.findOneBy({
            id: userId,
        })

        const geoQuery = geoFire.query({
            center: [+latitude, +longitude],
            radius: 9.5
        });

        geoQuery.on("key_entered", function (key: any, location: any, distance: any) {
            nearby.push(key)
        });
        
        const sleep = (ms:number) => {
            return new Promise ((resolve) => {
                setTimeout(resolve, ms)
            })
        }

        await sleep(50)

        let publicWorkouts = await this.publicWorkoutRepository.findBy({
            gender,
            experience,
        })

        if (friendsOnly) {
            publicWorkouts = publicWorkouts.filter((pw) => user?.friends.includes(pw.creator));
        }

        return success(
            publicWorkouts.filter((publicWorkout) => nearby.includes(publicWorkout.id.toString()))
        );
    }

    async remove(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const id = parseInt(request.params.id);

        let publicWorkoutToRemove =
            await this.publicWorkoutRepository.findOneBy({
                id,
                creator: { id: userId },
            });

        if (!publicWorkoutToRemove) {
            return failure("this public workout does not exist");
        }

        await this.publicWorkoutRepository.remove(publicWorkoutToRemove);

        return success("public workout has been removed");
    }
}
