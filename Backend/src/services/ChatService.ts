import { ChatController } from "../controllers/ChatController";
import { NotificationController } from "../controllers/NotificationController";
import { User } from "../entities/User";
const socketIO = require('socket.io');

export const initChatService = (server: any) => {
    const io = socketIO(server);
    const cc = new ChatController();
    const nc = new NotificationController();

    type UserDict = {
        [userId: number]: boolean
    }
    let userRoomMap: {[room: string]: UserDict} = {};

    io.on('connection', async (socket: any) => {
        let room = "default";
        let user: User;
        

        socket.on('join', async function(roomId: string, refreshToken: string) {
            const tempUser = await cc.tokenToUserHelper(refreshToken)
            if (!tempUser) {
                console.log(`invalid refreshToken`)
                return;
            }
            user = tempUser;

            console.log(`${user.username} connected`);
            room = roomId;
            socket.join(room);
            console.log(`Socket ${socket.id} joined room ${room}`);
            userRoomMap = {
                ...userRoomMap ?? {},
                [room]: {
                    ...userRoomMap[room] ?? {},
                    [user.id]: true
                }
            }
            io.to(socket.id).emit('history', 'fetch history');
        });

        socket.on('message', async (message: string) => {
            console.log(`Received message '${message}'
                from user ${user.id}
                in room ${room}`);
            socket.to(room).emit('message', `${message}`);
            for (let userKey in userRoomMap[room]) {
                let value = userRoomMap[room][userKey];
                
                if (!value) {
                    for (let fcmToken in JSON.parse(user.firebaseTokens)) {
                        console.log(`sent notification to ${userKey}`)
                        nc.notificationHelper(fcmToken,`From: ${user.username}`,message)
                    }
                }
            }
            console.log(`broadcasted ${message}`);
        });

        socket.on('disconnect', async () => {
            console.log(`user ${user.id} disconnected`);
            userRoomMap[room][user.id] = false;
        });
    });
}
