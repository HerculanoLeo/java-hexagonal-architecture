package com.herculanoleo.starter.shared.utils;

import org.apache.commons.lang3.StringUtils;

import java.net.URI;

public final class TakeLastPathSegment {

    private TakeLastPathSegment() {
    }

    public static String fromURI(URI uri) {
        return fromString(uri.getPath());
    }

    public static String fromString(String path) {
        return StringUtils.substringAfterLast(path, "/");
    }

}
