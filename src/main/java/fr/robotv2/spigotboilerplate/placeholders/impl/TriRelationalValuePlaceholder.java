package fr.robotv2.spigotboilerplate.placeholders.impl;

@FunctionalInterface
public interface TriRelationalValuePlaceholder<A, B, C> {
    String apply(String text, A fst, B snd, C thd);
}
