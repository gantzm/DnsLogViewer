package us.gantzfamily.dlv.common.io;

import java.util.function.Consumer;

public abstract class FormattedReportWriter<T> implements Consumer<T> {

    protected final DlvConsole con;

    private boolean headerWritten = false;

    public FormattedReportWriter(final DlvConsole con) {
        this.con = con;
    }

    public abstract void writeHeader();

    public abstract void writeDetail(T detail);

    @Override
    public void accept(final T detail) {

        if (!headerWritten) {
            writeHeader();
            headerWritten = true;
        }
        
        writeDetail(detail);
    }

}