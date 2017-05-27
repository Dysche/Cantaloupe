package org.cantaloupe.inject;

public class Scope {
	private String namespace = null;
	private String name = null;

	private Scope(String namespace, String name) {
		this.namespace = namespace;
		this.name = name;
	}

	public static Scope of(String namespace, String name) {
		return new Scope(namespace, name);
	}

	public String getNamespace() {
		return this.namespace;
	}

	public String getName() {
		return this.name;
	}
}