import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { PublicWorkout } from "../entities/PublicWorkout";
import { User } from "../entities/User";
import { geoFire } from "../utils/location";
import { failure, success } from "../utils/responseTypes";

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

    async allJoined(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);

        return success(
            (
                await this.publicWorkoutRepository.find({
                    relations: { users: true, creator: true },
                })
            ).filter(
                (publicWorkout) =>
                    publicWorkout.creator.id === userId ||
                    publicWorkout.users?.find((user) => user.id === userId)
            )
        );
    }

    async one(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const id = parseInt(request.params.id);

        const publicWorkout = await this.publicWorkoutRepository.findOne({
            where: {
                creator: { id: userId },
                id,
            },
            relations: {
                users: true,
            },
        });

        if (!publicWorkout) {
            return failure("this public workout does not exist");
        }
        return success(publicWorkout);
    }

    async create(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const {
            name,
            date,
            latitude,
            longitude,
            description,
            gender,
            experience,
        } = request.body;

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

        geoFire
            .set(newPublicWorkout.id.toString(), [+latitude, +longitude])
            .then(
                function () {
                    console.log("Provided key has been added to GeoFire");
                },
                function (error) {
                    console.log("Error: " + error);
                }
            );

        return success({ publicWorkoutId: newPublicWorkout.id });
    }

    async filter(request: Request, response: Response, next: NextFunction) {
        const userId = parseInt(request.params.userId);
        const { gender, experience, friendsOnly, latitude, longitude } =
            request.body;

        const user = await this.userRepository.findOne({
            where: {
                id: userId,
            },
            relations: {
                friends: true,
            },
        });

        function pollGeofireLocation(
            center: [number, number],
            radius: number
        ): Promise<string[]> {
            return new Promise((resolve) => {
                const queryResults: any[] = [];

                const geoQuery = geoFire.query({ center, radius });

                geoQuery.on(
                    "key_entered",
                    (key: any, location: any, distance: any) => {
                        queryResults.push(key); // consider storing location
                    }
                );

                geoQuery.on("ready", () => {
                    geoQuery.cancel(); // unsubscribe all event listeners and destroy query
                    // consider sorting queryResults before resolving the promise (see below)
                    resolve(queryResults);
                });
            });
        }
        const nearby: string[] = await pollGeofireLocation(
            [+latitude, +longitude],
            9.5
        );

        let publicWorkouts = await this.publicWorkoutRepository.find({
            where: {
                gender,
                experience,
            },
            relations: {
                creator: true,
            },
        });

        if (friendsOnly) {
            publicWorkouts = publicWorkouts.filter((pw) =>
                user?.friends.find((friend) => friend.id === pw.creator.id)
            );
        }

        console.log(nearby);
        console.log(publicWorkouts);
        console.log(userId);

        return success(
            publicWorkouts.filter(
                (publicWorkout) =>
                    publicWorkout.creator.id !== userId &&
                    nearby.includes(publicWorkout.id.toString())
            )
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
