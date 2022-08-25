package us.gantzfamily.dlv.cli.cmd.pod;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import us.gantzfamily.dlv.cli.cmd.AbstractParameters;

@Parameters(commandDescription = "Produce a report of request dmc artifacts.")
public class QueryReportParameters extends AbstractParameters {

	@Parameter(names = { "--whitelist" }, required = false, description = "Whitelist of domains to ignore.", order = 21)
	private String whitelistPath;

	@Parameter(names = { "--out" }, required = true, description = "Report filename.", order = 21)
	private String outFilename;

	@Parameter(description = "Files")
	private List<String> files;

	public QueryReportParameters() {
		super();
	}

	public Optional<Path> getWhitelistPath() {
		return StringUtils.isNotBlank(whitelistPath) ? Optional.of(Paths.get(whitelistPath)) : Optional.empty();
	}

	public Path getOutputPath() {
		return Paths.get(outFilename);
	}

	public List<String> getFiles() {
		return files != null ? files : new ArrayList<>();
	}

	public List<Path> getPaths() {
		return getFiles() //
				.stream() //
				.map(Paths::get) //
				.collect(Collectors.toList());
	}

}
