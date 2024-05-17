package fr.robotv2.spigotboilerplate.placeholders;

import fr.robotv2.spigotboilerplate.placeholders.impl.InternalPlaceholder;
import fr.robotv2.spigotboilerplate.placeholders.impl.RelationalValuePlaceholder;
import fr.robotv2.spigotboilerplate.placeholders.impl.TriRelationalValuePlaceholder;
import fr.robotv2.spigotboilerplate.placeholders.impl.ValuePlaceholder;
import fr.robotv2.spigotboilerplate.util.ColorUtil;
import org.bukkit.OfflinePlayer;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class PlaceholderSupport<E extends PlaceholderSupport<E>> {

    public abstract E apply(Function<String, String> replaceFunction);

    public E apply(String from, String to) {
        return apply(new InternalPlaceholder(from, to));
    }

    public E apply(InternalPlaceholder placeholder) {
        return apply(placeholder::apply);
    }

    public <T> E apply(ValuePlaceholder<T> placeholder, T value) {
        return apply(s -> placeholder.apply(s, value));
    }

    public <A, B> E apply(RelationalValuePlaceholder<A, B> placeholder, A fst, B snd) {
        return apply(s -> placeholder.apply(s, fst, snd));
    }

    public <A, B, C> E apply(TriRelationalValuePlaceholder<A, B, C> placeholder, A fst, B snd, C thd) {
        return apply(s -> placeholder.apply(s, fst, snd, thd));
    }

    public E applyIf(Predicate<String> predicate, String from, String to) {
        return applyIf(predicate, new InternalPlaceholder(from, to));
    }

    public E applyIf(Predicate<String> predicate, InternalPlaceholder placeholder) {
        return apply(s -> predicate.test(s) ? placeholder.apply(s) : s);
    }

    @SuppressWarnings("unchecked")
    public <T> E applyIf(ValuePlaceholder<T> placeholder, Class<T> tClass, Object value) {

        if(tClass.isAssignableFrom(value.getClass())) {
            apply(placeholder, (T) value);
        }

        return (E) this;
    }

    public E color() {
        return apply(ColorUtil::color);
    }

    public E papi(OfflinePlayer offlinePlayer) {
        return apply(s -> SafePlaceholderAPI.parsePAPI(s, offlinePlayer));
    }
}
