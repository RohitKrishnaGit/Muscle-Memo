import { ChatController } from "../controllers/ChatController";
const socketIO = require('socket.io');

export const initChatService = (server: any) => {
    const io = socketIO(server);
    const cc = new ChatController();

    io.on('connection', (socket: any) => {
        let room = "default";
        console.log('A user connected');

        socket.on('join', function(roomId: string) {
            room = roomId;
            socket.join(room);
            console.log(`Socket ${socket.id} joined room ${room}`);
            io.to(socket.id).emit('history', 'fetch history');
        });

        socket.on('message', async (message: string, refreshToken: string) => {
            console.log(`Received message '${message}'
                from user with refresh token ${refreshToken}
                in room ${room}`);
            if( !(await cc.createHelper(refreshToken, room, message)).error) {
                socket.to(room).emit('message', `${message}`);
                console.log(`broadcasted ${message}`)
            } else {
                console.log(`invalid refreshToken`)
            }
        });

        socket.on('disconnect', () => {
            console.log('A user disconnected');
        });
    });
}
