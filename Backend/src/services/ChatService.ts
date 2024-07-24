import { ChatController } from "../controllers/ChatController";
import { NotificationController } from "../controllers/NotificationController";
import { UserController } from "../controllers/UserController";
import { User } from "../entities/User";
const socketIO = require("socket.io");

export const initChatService = (server: any) => {
    const io = socketIO(server);
    const chatController = new ChatController();
    const notificationController = new NotificationController();
    const userController = new UserController();

    type UserDict = {
        [userId: number]: boolean;
    };

    let userRoomMap: { [room: string]: UserDict } = {};

    const getDefaultRoom = (roomId: string): UserDict => {
        if (/^workout-\d+$/.test(roomId)) {
            return {};
        }
        const users = roomId.split("-");
        return users.reduce(
            (prev, cur) => ({ ...prev, [parseInt(cur)]: false }),
            {} as UserDict
        );
    };

    io.on("connection", async (socket: any) => {
        let room = "default";
        let user: User;

        console.log("user connected");

        socket.on(
            "join",
            async function (roomId: string, refreshToken: string) {
                const tempUser = await chatController.tokenToUserHelper(
                    refreshToken
                );
                if (!tempUser) {
                    console.log(`invalid refreshToken`);
                    return;
                }
                user = tempUser;

                console.log(`${user.username} connected`);
                room = roomId;
                socket.join(room);
                console.log(`Socket ${socket.id} joined room ${room}`);
                userRoomMap = {
                    ...(userRoomMap ?? {}),
                    [room]: {
                        ...(userRoomMap[room] ?? getDefaultRoom(room)),
                        [user.id]: true,
                    },
                };
                const msg = await chatController.createHelper(
                    user,
                    room,
                    "",
                    Date.now()
                );
                if (msg.error || !msg.data) {
                    return;
                }
                io.to(room).emit(
                    "message",
                    msg.data.id,
                    "",
                    JSON.stringify(msg.data.sender),
                    Date.now()
                );
                io.to(socket.id).emit("history", "fetch history");
            }
        );

        socket.on("message", async (message: string) => {
            console.log(`Received message '${message}'
                from user ${user.id}
                in room ${room}`);
            const msg = await chatController.createHelper(
                user,
                room,
                message,
                Date.now()
            );
            if (msg.error || !msg.data) {
                return;
            }
            io.to(room).emit(
                "message",
                msg.data.id,
                message,
                JSON.stringify(msg.data.sender),
                Date.now()
            );

            for (let userKey in userRoomMap[room]) {
                let value = userRoomMap[room][userKey];

                if (!value) {
                    const userToSend = await userController.oneHelper(userKey);
                    if (userToSend) {
                        const fcmTokens: string[] = JSON.parse(
                            userToSend.firebaseTokens
                        );
                        fcmTokens.forEach((fcmToken) => {
                            console.log(
                                `sent notification to ${userToSend.username} with fcmToken=${fcmToken}`
                            );
                            notificationController.notificationHelper(
                                fcmToken,
                                `From: ${user.username}`,
                                message
                            );
                        });
                    }
                }
            }
            console.log(`broadcasted ${message}`);
            console.log(userRoomMap[room]);
        });

        socket.on("disconnect", async () => {
            if (user) {
                console.log(`user ${user.id} disconnected`);
                userRoomMap[room][user.id] = false;
            }
        });
    });
};
