import bcrypt from "bcrypt";
import { randomBytes } from "crypto";

export const generatePasswordHash = async (
    password: string
): Promise<string> => {
    const salt = await bcrypt.genSalt();
    const hash = await bcrypt.hash(password, salt);
    return hash;
};

export const validatePassword = async (
    password: string,
    hash: string
): Promise<boolean> => {
    const result = await bcrypt.compare(password, hash);
    return result;
};

export const isPasswordHash = (hash: string): boolean => {
    if (!hash || hash.length !== 60) return false;
    try {
        bcrypt.getRounds(hash);
        return true;
    } catch {
        return false;
    }
};

export const generateRandomToken = () =>
    randomBytes(48).toString("base64").replace(/[+/]/g, ".");
