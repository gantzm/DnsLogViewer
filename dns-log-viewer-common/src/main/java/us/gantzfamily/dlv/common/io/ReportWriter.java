package us.gantzfamily.dlv.common.io;

import java.io.Closeable;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ReportWriter implements Closeable {

    private final PrintWriter printWriter;

    public ReportWriter(final OutputStream os) {
        this.printWriter = new PrintWriter(os);
    }

    public void println(final String msg) {
        printWriter.println(msg);
    }

    public void println(final String msg, Object... args) {
        println(String.format(msg, args));
    }

    @Override
    public void close() {
        printWriter.flush();
    }
}
