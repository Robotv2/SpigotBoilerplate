package fr.robotv2.spigotboilerplate.placeholders;

@FunctionalInterface
public interface TriRelationalValuePlaceholder<A, B, C> {
    String apply(String text, A fst, B snd, C thd);
}
