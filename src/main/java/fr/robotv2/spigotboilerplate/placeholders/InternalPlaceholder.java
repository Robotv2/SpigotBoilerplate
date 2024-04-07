package fr.robotv2.spigotboilerplate.placeholders;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InternalPlaceholder {

    private final String from;
    private final String to;

    public static InternalPlaceholder of(String from, String to) {
        return new InternalPlaceholder(from, to);
    }

    public String apply(String text) {
        return text.replace(from, to);
    }
}
