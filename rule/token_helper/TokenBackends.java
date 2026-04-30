package top.fifthlight.fabazel.tokenhelper;

import java.util.ServiceLoader;

public final class TokenBackends {
    private static final TokenBackend INSTANCE = ServiceLoader.load(TokenBackend.class)
            .findFirst()
            .orElseThrow();

    private TokenBackends() {
    }

    public static TokenBackend getDefault() {
        return INSTANCE;
    }
}
