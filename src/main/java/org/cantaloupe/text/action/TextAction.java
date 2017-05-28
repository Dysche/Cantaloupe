package org.cantaloupe.text.action;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

public abstract class TextAction<R> {
    protected final R result;

    protected TextAction(R result) {
        this.result = checkNotNull(result, "result");
    }

    public final R getResult() {
        return this.result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TextAction<?> that = (TextAction<?>) o;
        return this.result.equals(that.result);
    }

    @Override
    public int hashCode() {
        return this.result.hashCode();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(this.result).toString();
    }
}