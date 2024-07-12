import { NextFunction, Request, Response } from "express";
import { AnyZodObject, ZodError } from "zod";

export const validateSchema =
    (schema: AnyZodObject) =>
    (req: Request, res: Response, next: NextFunction) => {
        try {
            schema.parse({
                params: req.params,
                query: req.query,
                body: req.body,
            });

            next();
        } catch (error) {
            if (error instanceof ZodError) {
                return res.status(400).json({
                    error: true,
                    code: 400,
                    message: error.errors
                        .reduce((prev, curr) => {
                            return [...prev, curr.message];
                        }, [] as string[])
                        .join(","),
                });
            }
            next(error);
        }
    };
