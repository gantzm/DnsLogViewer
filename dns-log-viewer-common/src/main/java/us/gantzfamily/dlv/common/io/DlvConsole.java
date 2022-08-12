package us.gantzfamily.dlv.common.io;

import java.io.PrintWriter;

public class DlvConsole {

    public static final DlvConsole INSTANCE = new DlvConsole();

    public static final int LEVEL_INFO = 100;
    public static final int LEVEL_DEBUG = 200;
    public static final int LEVEL_TRACE = 300;
    
    private final PrintWriter printWriter;

    private int currentLevel = LEVEL_INFO;
    
    private DlvConsole() {
        this.printWriter = new PrintWriter(System.out);
    }
    
    private void write(final int level, final String message) {
        if( currentLevel >= level ) {
            printWriter.println(message);
            printWriter.flush();
        }
    }
    
    public void setLevel(final int level) {
        this.currentLevel = level;
    }
    
    public void info(final String message) {
        write(LEVEL_INFO, message);
    }
    
    public void info(final String message, final Object ... args) {
        this.info(String.format(message, args));
    }

    public void debug(final String message) {
        write(LEVEL_DEBUG, message);
    }
    
    public void debug(final String message, final Object ... args) {
        this.debug(String.format(message, args));
    }
    
    public void trace(final String message) {
        write(LEVEL_TRACE, message);
    }
    
    public void trace(final String message, final Object ... args) {
        this.trace(String.format(message, args));
    }
    
}
