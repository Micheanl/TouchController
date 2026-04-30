package top.fifthlight.fabazel.tokenhelper.backend.macos;

import pt.davidafsilva.apple.OSXKeychain;
import pt.davidafsilva.apple.OSXKeychainException;
import top.fifthlight.fabazel.tokenhelper.TokenBackend;

public class MacOsKeychainBackend implements TokenBackend {
    private final OSXKeychain keychain = OSXKeychain.getInstance();

    public MacOsKeychainBackend() throws OSXKeychainException {
    }

    @Override
    public void saveToken(String tokenId, String tokenSecret) throws Exception {
        keychain.addGenericPassword("modrinth_token_id", tokenId, tokenSecret);
    }

    @Override
    public String getToken(String tokenId) throws Exception {
        return keychain.findGenericPassword("modrinth_token_id", tokenId).orElse(null);
    }
}
