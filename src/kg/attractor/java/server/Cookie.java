package kg.attractor.java.server;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Cookie<V> {
    private final String name;
    private final V value;
    private Integer maxAge;
    private boolean httpOnly;
    private String path;

    public Cookie(String name, V value) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(value);
        this.name = name.strip();
        this.value = value;
    }

    public static <V> Cookie make(String name, V value) {
        return new Cookie<>(name, value);
    }

    private V getValue() {
        return value;
    }

    private Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAgeInSeconds) {
        this.maxAge = maxAgeInSeconds;
    }

    private String getName() {
        return name;
    }

    private boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public void setPath(String path) {
        this.path = path;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        Charset utf8 = StandardCharsets.UTF_8;

        String encodedName = URLEncoder.encode(getName().strip(), utf8);

        String stringValue = getValue().toString();
        String encodedValue = URLEncoder.encode(stringValue, utf8);

        sb.append(String.format("%s=%s", encodedName, encodedValue));

        if (getMaxAge() != null) {
            sb.append(String.format("; Max-Age=%s", getMaxAge()));
        }

        if (isHttpOnly()) {
            sb.append("; HttpOnly");
        }

        return sb.toString();
    }
}