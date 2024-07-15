import { NextFunction, Request, Response } from "express";
import { get } from "lodash";

export const enforce = (
    ...rules: ((
        inputId?: number,
        tokenId?: number
    ) => {
        error: boolean;
        code: number;
        message?: string;
    })[]
) => {
    return (path: string[]) => {
        return (req: Request, res: Response, next: NextFunction) => {
            if (!req.user) {
                return res.status(401).json({
                    error: true,
                    code: 401,
                    message: "Access Denied: User Unknown",
                });
            }

            try {
                let messages: string[] = [];
                let code = 200;
                const passessAll = rules.every((rule) => {
                    const test = rule(parseInt(get(req, path)), req.user?.id);
                    if (test.error) {
                        if (test.message)
                            messages = [...messages, test.message];
                        code = test.code;
                    }
                    return !test.error;
                });
                if (!passessAll) {
                    return res.status(code).json({
                        error: true,
                        code: code,
                        message: `The following rules failed: ${messages.join(
                            ","
                        )}`,
                    });
                }

                next();
            } catch (err) {
                console.log(err);
                return res.status(401).json({
                    error: true,
                    code: 401,
                    message: "Access Denied: Invalid token",
                });
            }
        };
    };
};

export const or = (
    ...rules: ((
        inputId?: number,
        tokenId?: number
    ) => {
        error: boolean;
        code: number;
        message?: string;
    })[]
) => {
    return (inputId?: number, tokenId?: number) => {
        let messages: string[] = [];
        let code = 200;
        const passessSome = rules.some((rule) => {
            const test = rule(inputId, tokenId);
            if (test.error) {
                if (test.message) messages = [...messages, test.message];
                code = test.code;
            }
            return !test.error;
        });
        if (!passessSome) {
            return {
                error: true,
                code: code,
                message:
                    messages.length > 0
                        ? `or(${messages.join(",")})`
                        : undefined,
            };
        }
        return {
            error: false,
            code: 200,
            message:
                messages.length > 0 ? `or(${messages.join(",")})` : undefined,
        };
    };
};

export const and = (
    ...rules: ((
        inputId?: number,
        tokenId?: number
    ) => {
        error: boolean;
        code: number;
        message?: string;
    })[]
) => {
    return (inputId?: number, tokenId?: number) => {
        let messages: string[] = [];
        let code = 200;
        const passessAll = rules.every((rule) => {
            const test = rule(inputId, tokenId);
            if (test.error) {
                if (test.message) messages = [...messages, test.message];
                code = test.code;
            }
            return !test.error;
        });
        if (!passessAll) {
            return {
                error: true,
                code: code,
                message:
                    messages.length > 0
                        ? `and(${messages.join(",")})`
                        : undefined,
            };
        }
        return {
            error: false,
            code: 200,
            message:
                messages.length > 0 ? `and(${messages.join(",")})` : undefined,
        };
    };
};
