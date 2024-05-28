package santanu.core;

import com.google.inject.Singleton;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

@Singleton
public class IdGenerator {
    public String generate(String url) {
        return DigestUtils.sha1Hex(url).substring(0, 8);
    }

    public String generateWithBuffer(String url) {
        return DigestUtils.sha1Hex(url.concat(getBuffer())).substring(0, 8);
    }

    private static String getBuffer() {
        return String.valueOf(LocalDateTime.now().get(ChronoField.MICRO_OF_SECOND));
    }
}
