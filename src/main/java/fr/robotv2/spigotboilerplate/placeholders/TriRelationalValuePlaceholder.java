package fr.robotv2.spigotboilerplate.placeholders;

@FunctionalInterface
public interface TriRelationalValuePlaceholder<A, B, C> {
    String apply(A fst, B snd, C thd);
}
