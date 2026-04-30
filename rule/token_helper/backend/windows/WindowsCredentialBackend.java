package top.fifthlight.fabazel.tokenhelper.backend.windows;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fifthlight.fabazel.tokenhelper.TokenBackend;
import wincred.WinCred;

import java.io.IOException;

public class WindowsCredentialBackend implements TokenBackend {
    private static final Logger logger = LoggerFactory.getLogger(WindowsCredentialBackend.class);
    private static final WinCred winCred = new WinCred();

    @Override
    public void saveToken(String tokenId, String tokenSecret) throws IOException {
        winCred.setCredential(tokenId, "token", tokenSecret);
    }

    @Override
    public String getToken(String tokenId) {
        try {
            var cred = winCred.getCredential(tokenId);
            return cred.password();
        } catch (Exception ex) {
            logger.warn("Failed to get token from Windows Credential Manager: {}", ex.getMessage());
            return null;
        }
    }
}
