import bodyParser from "body-parser";
import express, { Request, Response } from "express";
import { totp } from "otplib";
import { AppDataSource } from "./data-source";
import { getEnv, initEnv } from "./environment";
import { initialize } from "./firebaseAdmin";
import { Routes } from "./routes";
import { initEmailService } from "./services/EmailService";
import { ApiResponse } from "./utils/responseTypes";

initEnv();
totp.options = { step: 1800, window: 1 };
AppDataSource.initialize()
    .then(async () => {
        // create express app
        const app = express();
        initialize();
        initEmailService();
        const port = getEnv().PORT || 3000;
        app.use(bodyParser.json());

        // register express routes from defined application routes
        Routes.forEach((route) => {
            (app as any)[route.method](
                route.route,
                route.middleware,
                (req: Request, res: Response, next: Function) => {
                    try {
                        const result:
                            | ApiResponse<any>
                            | Promise<ApiResponse<any>> =
                            new (route.controller as any)()[route.action](
                                req,
                                res,
                                next
                            );
                        if (result instanceof Promise) {
                            result.then((result) => {
                                try {
                                    return result !== null &&
                                        result !== undefined
                                        ? res.status(result.code).json(result)
                                        : undefined;
                                } catch (err) {
                                    console.error(err);
                                    return res.status(500).json({
                                        error: true,
                                        code: 500,
                                        message: "Internal Server Error",
                                    });
                                }
                            });
                        } else if (result !== null && result !== undefined) {
                            res.status(result.code).json(result);
                        }
                    } catch (err) {
                        console.error(err);
                        res.status(500).json({
                            error: true,
                            code: 500,
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
