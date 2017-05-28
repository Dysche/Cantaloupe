package org.cantaloupe.command.args.parsing;

import com.google.common.base.Objects;

public final class SingleArg {
    private final String value;
    private final int    startIndex;
    private final int    endIndex;

    public SingleArg(String value, int startIndex, int endIndex) {
        this.value = value;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public String getValue() {
        return this.value;
    }

    public int getStartIndex() {
        return this.startIndex;
    }

    public int getEndIndex() {
        return this.endIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SingleArg)) {
            return false;
        }

        SingleArg singleArg = (SingleArg) o;
        return this.startIndex == singleArg.startIndex && this.endIndex == singleArg.endIndex && Objects.equal(this.value, singleArg.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value, this.startIndex, this.endIndex);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("value", this.value).add("startIdx", this.startIndex).add("endIdx", this.endIndex).toString();
    }
}