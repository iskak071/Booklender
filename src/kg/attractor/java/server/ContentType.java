package kg.attractor.java.server;

public enum ContentType {
    TEXT_PLAIN("text/plain; charset=utf-8"),
    TEXT_HTML("text/html; charset=utf-8"),
    TEXT_CSS("text/css"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png");

    private final String descr;

    ContentType(String descr) {
        this.descr = descr;
    }

    public static ContentType fromFileName(String fileName) {
        if (fileName.endsWith(".css")) {
            return TEXT_CSS;
        }
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return TEXT_HTML;
        }
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return IMAGE_JPEG;
        }
        if (fileName.endsWith(".png")) {
            return IMAGE_PNG;
        }
        return TEXT_PLAIN;
    }

    @Override
    public String toString() {
        return descr;
    }
}

