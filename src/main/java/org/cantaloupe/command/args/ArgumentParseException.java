package org.cantaloupe.command.args;

import org.cantaloupe.command.CommandException;
import org.cantaloupe.text.Text;

import com.google.common.base.Strings;

public class ArgumentParseException extends CommandException {
	private static final long serialVersionUID = -8555316116315990226L;

	private final String source;
	private final int position;

	public ArgumentParseException(Text message, String source, int position) {
		super(message, true);
		this.source = source;
		this.position = position;
	}

	public ArgumentParseException(Text message, Throwable cause, String source, int position) {
		super(message, cause, true);
		this.source = source;
		this.position = position;
	}

	@Override
	public Text getText() {
		Text superText = super.getText();
		
		if (this.source == null || this.source.isEmpty()) {
			return super.getText();
		} else if (superText == null) {
			return Text.of(getAnnotatedPosition());
		} else {
			return Text.of(superText.getContent(), "\n", getAnnotatedPosition());
		}
	}

	public String getAnnotatedPosition() {
		String source = this.source;
		int position = this.position;
		
		if (source.length() > 80) {
			if (position >= 37) {
				int startPos = position - 37;
				int endPos = Math.min(source.length(), position + 37);
				
				if (endPos < source.length()) {
					source = "..." + source.substring(startPos, endPos) + "...";
				} else {
					source = "..." + source.substring(startPos, endPos);
				}
				
				position -= 40;
			} else {
				source = source.substring(0, 77) + "...";
			}
		}
		
		return source + "\n" + Strings.repeat(" ", position) + "^";
	}

	public int getPosition() {
		return this.position;
	}

	public String getSourceString() {
		return this.source;
	}
}