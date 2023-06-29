package ru.turbogoose.servlet.path;

import org.junit.jupiter.api.Test;
import ru.turbogoose.exception.MismatchException;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExactPathMatcherTest {
    @Test
    public void whenCallMatchOnNotNullThenReturnFalse() {
        PathMatcher matcher = new ExactPathMatcher();
        assertThat(matcher.matches("abc"), is(false));
    }

    @Test
    public void whenCallMatchOnNullThenReturnTrue() {
        PathMatcher matcher = new ExactPathMatcher();
        assertThat(matcher.matches(null), is(true));
    }

    @Test
    public void whenCallExtractOnNullThenReturnEmptyMap() {
        PathMatcher matcher = new ExactPathMatcher();
        assertThat(matcher.extractVariables(null), is(Collections.emptyMap()));
    }

    @Test
    public void whenCallExtractOnNotNullThenThrow() {
        PathMatcher matcher = new ExactPathMatcher();
        assertThrows(MismatchException.class, () -> matcher.extractVariables("abc"));
    }
}