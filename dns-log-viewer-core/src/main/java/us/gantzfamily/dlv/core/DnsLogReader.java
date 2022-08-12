package us.gantzfamily.dlv.core;

import java.io.Reader;
import java.nio.file.Path;
import java.util.function.Consumer;

import org.apache.logging.log4j.core.LogEvent;

import us.gantzfamily.dlv.common.domain.Query;

public interface DnsLogReader {

    void read(final Path logPath, final Consumer<Query> logConsumer);
    
    void read(final Reader logreader, final Consumer<Query> logConsumer);
    
}
