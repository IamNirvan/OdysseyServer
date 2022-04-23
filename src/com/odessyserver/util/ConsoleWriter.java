package com.odessyserver.util;

import java.text.MessageFormat;

public class ConsoleWriter {

    public static void writeErrorToConsole(String message, String cause) {
        System.out.println(MessageFormat.format("\n-- WARNING --\n\t- {0}\n\t- {1}",
                message, cause));
    }

    public static void writeMessageToConsole(String message) {
        System.out.println(MessageFormat.format("\n>> {0}", message));
    }
}
