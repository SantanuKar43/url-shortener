package santanu.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorTest {
    public static final String HTTPS_WWW_GOOGLE_COM = "https://www.google.com";
    private final IdGenerator idGenerator = new IdGenerator();

    @Test
    void generate() {
        String id = idGenerator.generate(HTTPS_WWW_GOOGLE_COM);
        assertEquals("ef7efc98", id);
    }

    @Test
    void generateWithBuffer() {
        String id = idGenerator.generate(HTTPS_WWW_GOOGLE_COM);
        String idWithBuffer = idGenerator.generateWithBuffer(HTTPS_WWW_GOOGLE_COM);
        assertNotEquals(id, idWithBuffer);
    }
}