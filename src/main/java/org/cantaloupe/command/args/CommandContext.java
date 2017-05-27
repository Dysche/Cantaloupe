package org.cantaloupe.command.args;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class CommandContext {
	private final Multimap<String, Object> parsedArgs;

	public CommandContext() {
		this.parsedArgs = ArrayListMultimap.create();
	}

	@SuppressWarnings("unchecked")
	public <T> Collection<T> getAll(String key) {
		return Collections.unmodifiableCollection((Collection<T>) this.parsedArgs.get(key));
	}

	@SuppressWarnings("unchecked")
	public <T> Optional<T> getOne(String key) {
		Collection<Object> values = this.parsedArgs.get(key);

		if (values.size() != 1) {
			return Optional.empty();
		}

		return Optional.ofNullable((T) values.iterator().next());
	}

	public void putArg(String key, Object value) {
		this.parsedArgs.put(key, value);
	}

	public boolean hasAny(String key) {
		return this.parsedArgs.containsKey(key);
	}
}