import { NextFunction, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { Chat } from "../entities/Chat";
import { User } from "../entities/User";
import { UserToken } from "../entities/UserToken";
import { failure, success } from "../utils/responseTypes";

export class ChatController {
    private chatRepository = AppDataSource.getRepository(Chat);
    private tokenRepository = AppDataSource.getRepository(UserToken);

    async allHelper(roomId: string) {
        return success(
            this.chatRepository.find({
                where: { roomId },
                relations: {
                    sender: true,
                },
            })
        );
    }

    async all(request: Request, response: Response, next: NextFunction) {
        const roomId = request.params.roomId;

        return this.allHelper(roomId);
    }

    /* Shouldn't need to get single chat messages, keeping until remove confirmed */

    // async one(request: Request, response: Response, next: NextFunction) {
    //     const userId = parseInt(request.params.userId);
    //     const roomId = request.params.roomId;
    //     const id = parseInt(request.params.id);

    //     const chat =
    //         await this.chatRepository.findOneBy({
    //             id,
    //             roomId,
    //             sender: { id: userId },
    //         });

    //     if (!chat) {
    //         return failure("this chat does not exist");
    //     }
    //     return success(chat);
    // }

    async createHelper(user: User, roomId: string, message: string) {
        const sender = user;

        if (!sender) return failure("unregistered sender");

        const chat = Object.assign(new Chat(), {
            roomId,
            message,
            sender,
        });

        return success(this.chatRepository.save(chat));
    }

    async remove(request: Request, response: Response, next: NextFunction) {
        const roomId = request.params.roomId;

        let chatsToRemove = await this.chatRepository.findBy({
            roomId,
        });

        if (!chatsToRemove) {
            return failure("no chats with the roomId exist");
        }

        await this.chatRepository.remove(chatsToRemove);

        return success("chats have been removed");
    }

    async tokenToUserHelper(token: string) {
        const tokenObj = await this.tokenRepository.findOne({
            where: { token },
            relations: {
                user: true,
            },
        });

        if (!tokenObj) return null;
        return tokenObj.user;
    }
}
