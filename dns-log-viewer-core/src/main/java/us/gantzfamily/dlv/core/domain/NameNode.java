package us.gantzfamily.dlv.core.domain;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import us.gantzfamily.dlv.common.domain.Query;
import us.gantzfamily.dlv.common.lang.DateUtils;

public class NameNode {

	private final NameNode parent;
	private final String name;

	private Date firstSeen;
	private Date lastSeen;
	private int count;
	
	private final Map<String, NameNode> children = new HashMap<>();

	public NameNode(final NameNode parent, final String name) {
		this.parent = parent;
		this.name = name;
		this.firstSeen = null;
		this.lastSeen = null;
		this.count = 0;
	}

	public NameNode getParent() {
		return parent;
	}
	
	public String getName() {
		return name;
	}

	public Optional<Date> getFirstSeen() {
		return Optional.ofNullable(firstSeen);
	}
	
	public Optional<Date> getLastSeen() {
		return Optional.ofNullable(lastSeen);
	}

	public int getCount() {
		return count;
	}
	
	private NameNode newNode(final String name) {
		return new NameNode(this, name);
	}
	
	private NameNode addNodes(final List<String> nodeNames, final Query query) {
		
		if( nodeNames.isEmpty() ) {
			update(query);
			return this;
		}
		
		final String nextNodeName = nodeNames.get(0);
		
		final NameNode nextNode = children.computeIfAbsent(nextNodeName, this::newNode);
		
		return nextNode.addNodes(nodeNames.subList(1, nodeNames.size()), query);
	}
	
	private void update(final Query query) {
		
		final Date timestamp = query.getTimestamp();
		
		this.firstSeen = DateUtils.min(this.firstSeen, timestamp);
		this.lastSeen = DateUtils.max(this.lastSeen, timestamp);
		this.count += 1;
	}
	
	public static NameNode addNodes(final NameNode rootNode, final Query query) {

		final String[] nodeNames = query.getReverseParts();
	
		final List<String> nodeNamesList = Arrays.asList(nodeNames);
		
		return rootNode.addNodes(nodeNamesList, query);
	}
}
