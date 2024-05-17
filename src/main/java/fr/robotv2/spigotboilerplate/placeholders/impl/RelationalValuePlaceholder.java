package fr.robotv2.spigotboilerplate.placeholders.impl;

@FunctionalInterface
public interface RelationalValuePlaceholder<A, B> {
    String apply(String text, A fst, B snd);
}
