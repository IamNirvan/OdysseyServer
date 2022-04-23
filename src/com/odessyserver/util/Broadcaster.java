package com.odessyserver.util;

import java.io.BufferedWriter;
import java.io.IOException;

public final class Broadcaster {
    public static void broadcastStandardMessage(BufferedWriter bufferedWriter, String message)
            throws IOException {
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }
}
