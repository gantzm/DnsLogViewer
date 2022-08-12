package us.gantzfamily.dlv.core.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import com.gantzgulch.logging.core.GGLogger;
import com.gantzgulch.tools.common.lang.GGPredicates;
import com.google.inject.Inject;

import us.gantzfamily.dlv.common.domain.Query;
import us.gantzfamily.dlv.common.io.DlvConsole;
import us.gantzfamily.dlv.core.DnsLogReader;

public class DnsLogReaderImpl implements DnsLogReader {

    private static final GGLogger LOG = GGLogger.getLogger(DnsLogReaderImpl.class);

    private final DlvConsole con = DlvConsole.INSTANCE;
    
    @Inject
    public DnsLogReaderImpl() {
    }

    @Override
    public void read(final Path logPath, final Consumer<Query> logConsumer) {

        try (final Reader reader = Files.newBufferedReader(logPath)) {
            
            con.info("read: processing: %s", logPath);
            
            read(reader, logConsumer);
        } catch (final IOException e) {
            LOG.warn(e, "Error reading file: %s", logPath);
        }
    }

    @Override
    public void read(final Reader logReader, final Consumer<Query> logConsumer) {

        try (final BufferedReader reader = new BufferedReader(logReader)) {

            reader //
                    .lines() //
                    .map(this::parse) //
                    .filter(GGPredicates.notNull()) //
                    .forEach(logConsumer);

        } catch (final IOException e) {
            LOG.warn(e, "Error reading log file.");
            throw new RuntimeException(e);
        }

    }

    private Query parse(final String dnsQuery) {

        try {
        	
        	return Query.parse(dnsQuery).orElse(null);
        	
        } catch (final RuntimeException e) {
            LOG.warn(e, "Error parsing record: %s", dnsQuery);
        }

        return null;
    }
}
