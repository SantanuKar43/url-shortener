package santanu.core;

import com.google.inject.Singleton;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

@Singleton
public class IdGenerator {

    private static final int ID_LENGTH = 8;

    public String generate(String url) {
        return DigestUtils.sha1Hex(url).substring(0, ID_LENGTH);
    }

    public String generateWithBuffer(String url) {
        return DigestUtils.sha1Hex(url.concat(getBuffer())).substring(0, ID_LENGTH);
    }

    private static String getBuffer() {
        return String.valueOf(LocalDateTime.now().get(ChronoField.MICRO_OF_SECOND));
    }

    public boolean isValid(String id) {
        return id.length() == ID_LENGTH;
    }
}
