import bodyParser from "body-parser";
import dotenv from "dotenv";
import express, { Request, Response } from "express";
import { AppDataSource } from "./data-source";
import { Routes } from "./routes";

dotenv.config();
AppDataSource.initialize()
    .then(async () => {
        // create express app
        const app = express();
        const port = process.env.PORT || 3000;
        app.use(bodyParser.json());

        // register express routes from defined application routes
        Routes.forEach((route) => {
            (app as any)[route.method](
                route.route,
                route.middleware,
                (req: Request, res: Response, next: Function) => {
                    try {
                        const result = new (route.controller as any)()[
                            route.action
                        ](req, res, next);
                        if (result instanceof Promise) {
                            result.then((result) =>
                                result !== null && result !== undefined
                                    ? res.send(result)
                                    : undefined
                            );
                        } else if (result !== null && result !== undefined) {
                            res.json(result);
                        }
                    } catch (err) {
                        console.log(err);
                        res.status(500).json({
                            error: true,
                            message: "Internal Server Error",
                        });
                    }
                }
            );
        });

        // setup express app here
        // ...

        // start express server
        app.listen(port);

        console.log("Express server has started on port 3000.");
    })
    .catch((error) => console.log(error));
