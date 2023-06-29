package ru.turbogoose.servlet.path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.turbogoose.exception.MismatchException;
import ru.turbogoose.exception.PathMatcherException;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TemplatePathMatcherTest {
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "orders/{id}", "/orders/{id", "/orders/{order_id}", "{/orders/id}"
    })
    public void whenPassIncorrectPatternThenThrow(String incorrectPathTemplate) {
        assertThrows(PathMatcherException.class, () -> new TemplatePathMatcher(incorrectPathTemplate));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/", "/orders", "/orders/items"})
    public void whenPassPatternWithoutParametersThenMatchExactSameStringReturningEmptyMap(String template) {
        TemplatePathMatcher matcher = new TemplatePathMatcher(template);
        assertThat(matcher.matches(template), is(true));
        assertThat(matcher.extractVariables(template), is(Collections.emptyMap()));
        assertThat(matcher.matches(template + "/123"), is(false));
        assertThat(matcher.matches("/hello" + template), is(false));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"/", "/orders", "orders/1/items/", "/orders/1/items/4/barcodes"})
    public void whenPassMismatchedStringThenThrow(String mismatchedPath) {
        String template = "/orders/{orderId}/items/{itemId}";
        TemplatePathMatcher pathMatcher = new TemplatePathMatcher(template);
        assertThat(pathMatcher.matches(mismatchedPath), is(false));
        assertThrows(MismatchException.class, () -> pathMatcher.extractVariables(mismatchedPath));
    }

    @Test
    public void whenPassMatchedStringThenReturnMapWithVariables() {
        String template = "/orders/{orderId}/items/{itemId}";
        TemplatePathMatcher pathMatcher = new TemplatePathMatcher(template);
        String testPath = "/orders/11/items/a1b2c3";
        assertThat(pathMatcher.matches(testPath), is(true));
        Map<String, String> vars = pathMatcher.extractVariables(testPath);
        assertThat(vars.get("orderId"), is("11"));
        assertThat(vars.get("itemId"), is("a1b2c3"));
    }
}