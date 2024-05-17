package fr.robotv2.spigotboilerplate.placeholders.impl;

@FunctionalInterface
public interface ValuePlaceholder<A> {
    String apply(String text, A value);
}
