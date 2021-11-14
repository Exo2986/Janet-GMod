package gg.galaxygaming.janetgmod.logging;

import gg.galaxygaming.janetgmod.SplitOutputStream;

import java.io.File;
import java.io.PrintStream;

public class Logging {
    private final PrintStream logStream, logErrStream, consoleStream, errStream; //These may not need to be stored

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Logging() {
        File file = new File("logs/"); //Should this go in SplitOutputStream in case dir was deleted
        if (!file.exists())
            file.mkdir();
        System.setOut(logStream = new PrintStream(new SplitOutputStream(consoleStream = System.out)));
        System.setErr(logErrStream = new PrintStream(new SplitOutputStream(errStream = System.err)));
    }
}