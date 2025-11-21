package com.example.todo.todo;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringListConverterTest {
    private final StringListConverter conv = new StringListConverter();

    @Test
    void convertsListToJsonAndBack() throws Exception {
        List<String> tags = List.of("home", "shopping");
        String json = conv.convertToDatabaseColumn(tags);
        assertNotNull(json);
        assertTrue(json.contains("home"));

        List<String> parsed = conv.convertToEntityAttribute(json);
        assertNotNull(parsed);
        assertEquals(2, parsed.size());
        assertTrue(parsed.contains("shopping"));
    }

    @Test
    void handlesNullsGracefully() {
        assertNull(conv.convertToDatabaseColumn(null));
        assertNull(conv.convertToEntityAttribute(null));
    }
}
