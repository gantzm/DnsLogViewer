package us.gantzfamily.dlv.cli.cmd.pod;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.gantzgulch.tools.common.lang.GGCloseables;
import com.google.inject.Inject;
import com.opencsv.CSVWriter;

import us.gantzfamily.dlv.cli.cmd.AbstractCommand;
import us.gantzfamily.dlv.common.domain.Query;
import us.gantzfamily.dlv.core.DnsLogReader;

public class QueryReportCommand extends AbstractCommand<QueryReportParameters> {

	private final DnsLogReader podLogReader;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS Z");

	private List<Query> queries = new ArrayList<>();

	@Inject
	public QueryReportCommand(//
			final DnsLogReader podLogReader) {

		super("queryReport", new QueryReportParameters());

		this.podLogReader = podLogReader;

		this.sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@Override
	public int run() {

		readLogs();

		try (final ReportWriter reportWriter = new ReportWriter(params.getOutputPath())) {

//        	queries //
//        	 .stream() //
//        	 .sorted() //
//        	 .forEach(reportWriter::writeDetail);

			queries //
					.stream() //
					.sorted() //
					.forEach(q -> {
						System.out.println("Query: " + q);
					});
		}

		return 0;
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

		public void writeDetail(final Query dmcArtifact) {

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
