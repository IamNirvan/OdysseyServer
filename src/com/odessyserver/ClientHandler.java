package com.odessyserver;

import com.odessyserver.util.Broadcaster;
import com.odessyserver.util.ConsoleWriter;

import java.io.*;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;

public final class ClientHandler implements Runnable {
    private final Socket SOCKET;
    private BufferedWriter buffWriter;
    private BufferedReader buffReader;
    private String clientUserName;

    public ClientHandler(Socket socket) {
        this.SOCKET = socket;
        setUpHandler();
    }

    /**
     * Creates instances of BufferedWriter and BufferedReader and bounds them to the client's
     * output and input stream respectively. The username is acquired using the BufferedReader instance.
     * */
    private void setUpHandler() {
        try {
            this.buffWriter = new BufferedWriter(new OutputStreamWriter(this.SOCKET.getOutputStream()));
            this.buffReader = new BufferedReader(new InputStreamReader(this.SOCKET.getInputStream()));

            // The first line sent by the client is the username. The statement below will receive that username.
            this.clientUserName = buffReader.readLine();
            broadcastMessage(MessageFormat.format("{0} entered the chat", this.clientUserName));
            showActiveUsers();
        } catch (IOException e) {
            e.printStackTrace();
            expelResources();
        }
    }

    /**
     * The code that runs on a separate thread. It is responsible for listening for messages from
     * the client and broadcasting them to other clients.
     * */
    @Override
    public void run() {
        String messageFromClient = null;
        try {
            while(this.SOCKET.isConnected()) {
                if(!this.SOCKET.isClosed()) {
                    messageFromClient = this.buffReader.readLine();
                    broadcastMessage(messageFromClient);
                }
            }
        } catch (IOException e) {
            ConsoleWriter.writeErrorToConsole("An error occurred when listening for client messages",
                    e.toString());
            expelResources();
        }
    }

    private void showActiveUsers() {
        ArrayList<ClientHandler> clientHandlers = Server.getClientHandlers();
        if(clientHandlers.size() != 0) {
            String[] userNames = new String[clientHandlers.size()];
            int count = 0;
            try {
                for(ClientHandler clientHandler : clientHandlers) {
                    userNames[count++] = clientHandler.clientUserName;
                }
                Broadcaster.broadcastStandardMessage(this.buffWriter, MessageFormat.format(
                        "Active users include: {0}", Arrays.toString(userNames)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes the message to the consoles of other ClientHandlers' clients,
     * by using an arraylist of ClientHandlers and using their buffered writer.
     * */
    public void broadcastMessage(String message) {
        for (ClientHandler clientHandler: Server.getClientHandlers()) {
            if(!clientHandler.clientUserName.equals(this.clientUserName)) {
                try {
                    Broadcaster.broadcastStandardMessage(clientHandler.buffWriter, message);
                } catch (IOException e) {
                    ConsoleWriter.writeErrorToConsole("An error occurred when broadcasting a message",
                            e.toString());
                    expelResources();
                }
            }
        }
    }

    public void removeUser() {
        broadcastMessage(MessageFormat.format("{0} has left the chat.", this.clientUserName));
        Server.removeClientHandler(this);
    }

    public void expelResources() {
        removeUser();
        try {
            if(this.buffWriter != null) {
                this.buffWriter.close();
            }
            if(this.buffReader != null) {
                this.buffReader.close();
            }
            if(this.SOCKET != null) {
                this.SOCKET.close();
            }
        } catch(IOException e) {
            ConsoleWriter.writeErrorToConsole("An error occurred when expelling resources", e.toString());
        }
    }
}