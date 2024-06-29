import { JwtPayload } from "jsonwebtoken";
import { User } from "../entities/User";

declare global {
    namespace Express {
        interface Request {
            user?: string | JwtPayload;
        }
    }
}
