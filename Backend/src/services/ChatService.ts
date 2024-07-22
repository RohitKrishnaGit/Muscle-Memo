const socketIO = require('socket.io');

export const initChatService = (server: any) => {
    const io = socketIO(server);

    io.on('connection', (socket: any) => {
        let room = "default";
        console.log('A user connected');

        socket.on('join', function(roomId: string) {
            room = roomId;
            socket.join(room);
            console.log(`Socket ${socket.id} joined room ${room}`);
            io.emit('message', `${room}`);
        });

        socket.on('message', (message: string) => {
            console.log(`Received message '${message}' from socket ${socket.id} in room ${room}`);
            socket.to(room).emit('message', `${message}+me`);
        });

        socket.on('disconnect', () => {
            console.log('A user disconnected');
        });
    });
}
