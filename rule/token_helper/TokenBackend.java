package top.fifthlight.fabazel.tokenhelper;

public interface TokenBackend {
    void saveToken(String tokenId, String tokenSecret) throws Exception;

    String getToken(String tokenId) throws Exception;
}
