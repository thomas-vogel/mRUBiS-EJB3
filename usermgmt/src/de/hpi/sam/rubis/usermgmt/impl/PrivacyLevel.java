package de.hpi.sam.rubis.usermgmt.impl;

/**
 * Privacy levels.
 * 
 * @author thomas vogel
 *
 */
public enum PrivacyLevel {

	LOW("LOW"), HIGH("HIGH");

	private final String level;

	private PrivacyLevel(final String level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return this.level;
	}

}
