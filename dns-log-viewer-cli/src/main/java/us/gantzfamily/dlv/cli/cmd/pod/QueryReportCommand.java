package us.gantzfamily.dlv.cli.cmd.pod;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.gantzgulch.tools.common.lang.GGCloseables;
import com.google.inject.Inject;
import com.opencsv.CSVWriter;

import us.gantzfamily.dlv.cli.cmd.AbstractCommand;
import us.gantzfamily.dlv.common.domain.Query;
import us.gantzfamily.dlv.core.DnsLogReader;

public class QueryReportCommand extends AbstractCommand<QueryReportParameters> {

	private final DnsLogReader podLogReader;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS Z");

	private Set<String> whitelist = new HashSet<>();

	private List<Query> queries = new ArrayList<>();

	private Map<String, Set<String>> groupMap = new HashMap<>();

	@Inject
	public QueryReportCommand(//
			final DnsLogReader podLogReader) {

		super("queryReport", new QueryReportParameters());

		this.podLogReader = podLogReader;

		this.sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@Override
	public int run() {

		readWhitelist();

		readLogs();

		try (final ReportWriter reportWriter = new ReportWriter(params.getOutputPath())) {

			queries //
					.stream() //
					.sorted() //
					.forEach(this::process);
		}

		final List<String> keys = groupMap//
				.keySet()//
				.stream()//
				.sorted()//
				.collect(Collectors.toList());

		for (final String key : keys) {

			System.out.println("");
			System.out.println("Client: " + key);

			final Set<String> s = groupMap.get(key);

			final List<String> queryList = s//
					.stream()//
					.filter(this::notWhitelisted) //
					.sorted()//
					.collect(Collectors.toList());

			for (final String q : queryList) {

				System.out.println("    " + q);
			}

		}

		return 0;
	}

	private boolean notWhitelisted(final String domain) {
		
		return ! isWhitelisted(domain);
		
	}
	
	private boolean isWhitelisted(final String domain) {
		
		if( StringUtils.isBlank(domain) ) {
			return false;
		}
		
		if( whitelist.contains(domain) ) {
			return true;
		}
		
		final String remaining = StringUtils.substringBeforeLast(domain, ".");
		
		if( domain.equals(remaining) ) {
			return false;
		}
		
		return isWhitelisted(remaining);
		
	}
	
	private void readWhitelist() {

		final Path whitelistPath = params.getWhitelistPath().orElse(null);

		if (whitelistPath == null) {
			return;
		}

		try {
			Files.readAllLines(whitelistPath) //
					.stream() //
					.filter(StringUtils::isNotBlank) //
					.forEach(whitelist::add);
		} catch (final IOException e) {
			LOG.fatal(e, "Unable to read whitelist: %s", whitelistPath);
			throw new RuntimeException(e);
		}
	}

	private void process(final Query query) {

		System.out.println("Query: " + query);

		final String clientIp = query.getClientIpAddress();
		final String domainRev = query.getReverseDomain();

		final Set<String> s = groupMap.computeIfAbsent(clientIp, k -> {
			return new HashSet<>();
		});

		s.add(domainRev);

	}

	private void readLogs() {

		for (final Path path : params.getPaths()) {
			podLogReader.read(path, queries::add);
		}

	}

	private class ReportWriter implements Closeable {

		private final Writer writer;
		private final CSVWriter csvWriter;

		private boolean headerWritten = false;

		public ReportWriter(final Path outputPath) {

			try {

				writer = Files.newBufferedWriter(outputPath);

				csvWriter = new CSVWriter(writer);

			} catch (final IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeHeader() {

			final String[] header = { "Artifact", "First Seen", "Last Seen", "Request Count", "Unique SNs",
					"SN Sample" };

			csvWriter.writeNext(header);

			headerWritten = true;
		}

		public void writeDetail(final Query query) {

//                return;
//            }
//
//            if (!headerWritten) {
//                writeHeader();
//            }
//
//            final String[] detail = { dmcArtifact, format(dmcData.getFirstSeen()), format(dmcData.getLastSeen()), Integer.toString(dmcData.getUsageCount()),
//                    Integer.toString(dmcData.getSerialNumberCount()), dmcData.getSerialNumberSample() };
//
//            csvWriter.writeNext(detail);

		}

		@Override
		public void close() {
			GGCloseables.closeQuietly(csvWriter);
		}

		private String format(final Date date) {
			return date != null ? sdf.format(date) : "";
		}

	}

}
