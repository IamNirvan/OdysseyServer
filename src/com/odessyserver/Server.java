package com.odessyserver;

import com.odessyserver.util.ConsoleWriter;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
    private final ServerSocket SERVER_SOCKET;
    private static final ArrayList<ClientHandler> CLIENT_HANDLERS = new ArrayList<>();

    public Server(ServerSocket socket) {
        this.SERVER_SOCKET = socket;
    }

    /**
     * Starts the process of listening for incoming connections from clients. This will continue as long as the server
     * socket is not closed.
     * */
    public void listenForConnections() {
        ConsoleWriter.writeMessageToConsole("Listening for incoming connections...");
        try {
            while(!this.SERVER_SOCKET.isClosed()) {
                ClientHandler clientHandler = new ClientHandler(this.SERVER_SOCKET.accept());
                addClientHandler(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            ConsoleWriter.writeErrorToConsole("An error occurred when listening for connections.", e.toString());
            closeSocket();
        }
    }

    private void closeSocket() {
        if(!this.SERVER_SOCKET.isClosed()) {
            try {
                this.SERVER_SOCKET.close();
                ConsoleWriter.writeMessageToConsole("Server socket closed.");
            } catch (IOException e) {
                ConsoleWriter.writeErrorToConsole("An error occurred when closing the server socket", e.toString());
            }
        }
    }

    protected static void addClientHandler(ClientHandler handler) {
        CLIENT_HANDLERS.add(handler);
        ConsoleWriter.writeMessageToConsole("A client has connected.");
    }

    protected static void removeClientHandler(ClientHandler handler) {
        CLIENT_HANDLERS.remove(handler);
        ConsoleWriter.writeMessageToConsole("A client disconnected");
    }

    protected static ArrayList<ClientHandler> getClientHandlers() {
        return CLIENT_HANDLERS;
    }
}
