package com.odessyserver;

import com.odessyserver.util.ServerConfiguration;

import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) {
        final ServerSocket SOCKET = ServerConfiguration.createServerSocket();
        final Server SERVER  = new Server(SOCKET);
        SERVER.listenForConnections();
    }
}
