package fr.robotv2.spigotboilerplate.placeholders;

@FunctionalInterface
public interface RelationalValuePlaceholder<A, B> {
    String apply(String text, A fst, B snd);
}
