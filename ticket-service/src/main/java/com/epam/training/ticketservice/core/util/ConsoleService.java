package com.epam.training.ticketservice.core.util;

import org.springframework.stereotype.Service;

import java.io.PrintStream;

@Service
public class ConsoleService {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    private final PrintStream out = System.out;

    public void print(String msg, String... args) {
        //this.out.print(ANSI_YELLOW);
        this.out.printf(msg, (Object[]) args);
        this.out.println();
    }

    public void printError(String msg, String... args) {
        //this.out.print(ANSI_RED);
        this.out.printf(msg, (Object[]) args);
        this.out.println();
    }
}
