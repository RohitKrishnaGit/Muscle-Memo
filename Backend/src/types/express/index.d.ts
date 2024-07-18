import { JwtPayload } from "jsonwebtoken";
import { Role } from "../../entities/User";

declare global {
    namespace Express {
        interface Request {
            user?: JwtPayload & { id?: number; role?: Role };
            isMe?: boolean;
        }
    }
}
