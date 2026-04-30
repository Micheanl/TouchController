package top.fifthlight.fabazel.tokenhelper.backend.linux;

import de.swiesend.secretservice.simple.SimpleCollection;
import top.fifthlight.fabazel.tokenhelper.TokenBackend;

import java.io.IOException;
import java.util.Map;

public class LinuxSecretServiceBackend implements TokenBackend {
    @Override
    public void saveToken(String tokenId, String tokenSecret) {
        try (var collection = new SimpleCollection()) {
            collection.createItem(tokenId, tokenSecret, Map.of("modrinth_token_id", tokenId));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getToken(String tokenId) throws IOException {
        try (var collection = new SimpleCollection()) {
            var items = collection.getItems(Map.of("modrinth_token_id", tokenId));
            if (items.isEmpty()) {
                return null;
            }
            return new String(collection.getSecret(items.getFirst()));
        }
    }
}
