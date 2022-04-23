package com.odessyserver.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class ServerConfiguration {
    private static int getPortNumber() {
        ConsoleWriter.writeMessageToConsole("Enter the port number:");
        return new Scanner(System.in).nextInt();
    }

    /**
     * Responsible for creating a new server socket for the specified port number.
     * It will continue to prompt the user to enter a port number, if it fails to create a socket using that port number.
     * @return A server socket, if it was successfully created. Otherwise, null.
     * */
    public static ServerSocket createServerSocket() {
        ServerSocket socket = null;
        do {
            int port = getPortNumber();
            try {
                socket = new ServerSocket(port);
            } catch (IOException e) {
                ConsoleWriter.writeErrorToConsole("Failed to create a server socket.", e.toString());
            }
        } while(socket == null);
        return socket;
    }

}
