package com.lodh8.starter.shared.utils;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TakeLastPathSegmentTest {

    @Test
    void fromString_withStandardPath_shouldReturnLastSegment() {
        String path = "/users/12345";
        assertEquals("12345", TakeLastPathSegment.fromString(path));
    }

    @Test
    void fromString_withTrailingSlash_shouldReturnEmptyString() {
        String path = "/users/12345/";
        // The split operation results in an empty string for the last element
        assertEquals("", TakeLastPathSegment.fromString(path));
    }

    @Test
    void fromString_withSingleSegment_shouldReturnSegment() {
        String path = "/users";
        assertEquals("users", TakeLastPathSegment.fromString(path));
    }

    @Test
    void fromString_withRootPath_shouldReturnEmptyString() {
        String path = "/";
        assertEquals("", TakeLastPathSegment.fromString(path));
    }

    @Test
    void fromString_withEmptyPath_shouldReturnEmptyString() {
        String path = "";
        assertEquals("", TakeLastPathSegment.fromString(path));
    }

    @Test
    void fromString_withNoLeadingSlash_shouldReturnLastSegment() {
        String path = "users/12345";
        assertEquals("12345", TakeLastPathSegment.fromString(path));
    }

    @Test
    void fromURI_withStandardURI_shouldReturnLastSegment() throws URISyntaxException {
        URI uri = new URI("http://localhost:8080/groups/abc-def");
        assertEquals("abc-def", TakeLastPathSegment.fromURI(uri));
    }

    @Test
    void fromURI_withTrailingSlash_shouldReturnEmptyString() throws URISyntaxException {
        URI uri = new URI("http://localhost:8080/groups/abc-def/");
        assertEquals("", TakeLastPathSegment.fromURI(uri));
    }

    @Test
    void fromURI_withRootPath_shouldReturnEmptyString() throws URISyntaxException {
        URI uri = new URI("http://localhost:8080/");
        assertEquals("", TakeLastPathSegment.fromURI(uri));
    }
}
