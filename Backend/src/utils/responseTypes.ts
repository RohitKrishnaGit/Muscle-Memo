export type ApiResponse<T> = {
    error: boolean;
    code: number;
    data?: T;
    message?: string;
};

export type Success<T> = ApiResponse<T> & { error: false };
export type Failure = ApiResponse<unknown> & { error: true; message: string };

export const success = async <T>(
    data: T | Promise<T>,
    message?: string,
    code: number = 200
): Promise<Success<T>> => {
    return {
        error: false,
        code,
        data: await Promise.resolve(data),
        message,
    };
};

export const failure = (
    message: string,
    code: number = 400
): Promise<Failure> => {
    return Promise.resolve({
        error: true,
        code,
        message,
    });
};

export function isSuccess<T>(x: ApiResponse<T>): x is Success<T> {
    return !x.error;
}

export function isFailure(x: ApiResponse<any>): x is Failure {
    return x.error;
}
