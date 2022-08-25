package us.gantzfamily.dlv.common.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.gantzgulch.logging.core.GGLogger;

public class Query implements Comparable<Query> {

	// 12-Aug-2022 17:44:30.989 client @0x7f4a5c041708 10.99.1.162#52677
	// (clients2.google.com): query: clients2.google.com IN A + (10.99.1.2)

	public static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS");

	public static final String REGEX_DATE_TIME = "(\\d\\d-\\w\\w\\w-\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d)";
	public static final String REGEX_CLNT_PTR = "@0x\\w{12}";
	public static final String REGEX_IP = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";
	public static final String REGEX_PORT = "\\d{1,10}";
	public static final String REGEX_QUERY = "\\(([^\\)]+)\\)";

	public static final String REGEX = "^" + REGEX_DATE_TIME + " client " + REGEX_CLNT_PTR + " " + REGEX_IP + "#"
			+ REGEX_PORT + " " + REGEX_QUERY + ".*$";

	public static final Pattern QUERY_PATTERN = Pattern.compile(REGEX);

	private static final GGLogger LOG = GGLogger.getLogger(Query.class);

	private final Date ts;

	private final String clientIpAddress;
	
	private final String queryDomain;
	
	private final String[] reverseParts;
	
	private final String reverseDomain;

	public Query(//
			final Date ts, //
			final String clientIpAddress, //
			final String queryDomain) {

		this.ts = ts;
		this.clientIpAddress = clientIpAddress;
		this.queryDomain = queryDomain;
		this.reverseParts = reverseParts(this.queryDomain);
		this.reverseDomain = StringUtils.join(this.reverseParts, ".");
	}

	public Date getTimestamp() {
		return ts;
	}

	public String getClientIpAddress() {
		return clientIpAddress;
	}

	public String getQueryDomain() {
		return queryDomain;
	}

	public String[] getReverseParts() {
		return reverseParts;
	}
	
	public String getReverseDomain() {
		return reverseDomain;
	}
	
	@Override
	public int compareTo(final Query o) {
		return ts.compareTo(o.ts);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private String[] reverseParts(String queryDomain) {
		
		if( StringUtils.isBlank(queryDomain)) {
			return new String[0];
		}
		
		final String[] parts = StringUtils.split(queryDomain, ".");
		
		final String[] reverseParts = new String[parts.length];
		
		
		for(int i=0, j=parts.length - 1; i<parts.length; i++, j--) {
			reverseParts[j] = parts[i];
		}
		
		return reverseParts;
	}

	public static Optional<Query> parse(final String queryText) {

		// 12-Aug-2022 17:44:30.989 client @0x7f4a5c041708 10.99.1.162#52677
		// (clients2.google.com): query: clients2.google.com IN A + (10.99.1.2)

		try {
			final Matcher m = QUERY_PATTERN.matcher(queryText);

			if (!m.matches()) {
				return Optional.empty();
			}

			final String dateTime = m.group(1);
			final String clientIpAddress = m.group(2);
			final String query = m.group(3);

			final Date ts = sdf.parse(dateTime);

			return Optional.of(new Query(ts, clientIpAddress, query));

		} catch (final RuntimeException | ParseException e) {

			LOG.warn(e, "Unexpected exception: %s", e.getMessage());

			return Optional.empty();
		}
	}

}
