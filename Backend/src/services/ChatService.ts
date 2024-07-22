const socketIO = require('socket.io');

export const initChatService = (server: any) => {
    const io = socketIO(server);

    io.on('connection', (socket: any) => {
        console.log('A user connected');

        socket.on('join', function(room: number) {
            socket.join(room);
        });

        socket.on('message', (message: string) => {
            console.log(message)
            io.emit('message', message);
        });

        socket.on('disconnect', () => {
            console.log('A user disconnected');
        });
    });
}