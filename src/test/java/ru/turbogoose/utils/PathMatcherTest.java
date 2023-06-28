package ru.turbogoose.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.turbogoose.exceptions.MismatchException;
import ru.turbogoose.exceptions.PathMatcherException;
import ru.turbogoose.utils.path.PathMatcher;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PathMatcherTest {
    @ParameterizedTest
    @ValueSource(strings = {
            "", "orders/{id}", "/orders/{id", "/orders/{order_id}", "{/orders/id}"
    })
    public void whenPassIncorrectPatternThenThrow(String incorrectPathTemplate) {
        assertThrows(PathMatcherException.class, () -> new PathMatcher(incorrectPathTemplate));
    }

    @Test
    public void whenCallExtractWithoutMatchCallOnMatchedStringThenReturnMapWithVariables() {
        String template = "/orders/{orderId}/items/{itemId}";
        PathMatcher pathMatcher = new PathMatcher(template);
        String testPath = "/orders/11/items/a1b2c3";
        Map<String, String> vars = pathMatcher.extractVariables(testPath);
        assertThat(vars.get("orderId"), is("11"));
        assertThat(vars.get("itemId"), is("a1b2c3"));
    }

    @Test
    public void whenCallExtractWithoutMatchOnMismatchedStringThenThrow() {
        String template = "/orders/{orderId}/items/{itemId}";
        PathMatcher pathMatcher = new PathMatcher(template);
        String testPath = "/orders/11";
        assertThrows(MismatchException.class, () -> pathMatcher.extractVariables(testPath));
    }

    @Test
    public void whenCallMatchAndExtractOnMatchedStringThenReturnMapWithVariables() {
        String template = "/orders/{orderId}/items/{itemId}";
        PathMatcher pathMatcher = new PathMatcher(template);
        String testPath = "/orders/11/items/a1b2c3";
        assertTrue(pathMatcher.matches(testPath));
        Map<String, String> vars = pathMatcher.extractVariables(testPath);
        assertThat(vars.get("orderId"), is("11"));
        assertThat(vars.get("itemId"), is("a1b2c3"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/orders", "/orders/items"})
    public void whenPassPatternWithoutParametersThenMatchExactSameStringAndExtractEmptyMap(String template) {
        PathMatcher matcher = new PathMatcher(template);

        assertThat(matcher.matches(template), is(true));
        assertThat(matcher.extractVariables(template).size(), is(0));

        assertThat(matcher.matches(template + "/123"), is(false));
        assertThat(matcher.matches("/hello" + template), is(false));
    }

    @Test
    public void whenPassNullPatternThenMatchNullAndExtractEmptyMap() {
        PathMatcher matcher = new PathMatcher(null);

        assertThat(matcher.matches(null), is(true));
        assertThat(matcher.extractVariables(null).size(), is(0));
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"/", "/orders", "orders/1/items/", "/orders/1/items/4/barcodes"})
    public void whenMatchingUnsuccessfulThenThrow(String mismatchedPath) {
        String template = "/orders/{orderId}/items/{itemId}";
        PathMatcher pathMatcher = new PathMatcher(template);
        assertThat(pathMatcher.matches(mismatchedPath), is(false));
    }
}