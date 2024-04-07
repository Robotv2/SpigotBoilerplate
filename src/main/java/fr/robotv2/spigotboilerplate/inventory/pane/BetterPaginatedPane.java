package fr.robotv2.spigotboilerplate.inventory.pane;

import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import org.jetbrains.annotations.NotNull;

public class BetterPaginatedPane extends PaginatedPane {

    public BetterPaginatedPane(@NotNull Slot slot, int length, int height, @NotNull Pane.Priority priority) {
        super(slot, length, height, priority);
    }

    public BetterPaginatedPane(int x, int y, int length, int height, @NotNull Pane.Priority priority) {
        super(x, y, length, height, priority);
    }

    public BetterPaginatedPane(@NotNull Slot slot, int length, int height) {
        super(slot, length, height);
    }

    public BetterPaginatedPane(int x, int y, int length, int height) {
        super(x, y, length, height);
    }

    public BetterPaginatedPane(int length, int height) {
        super(length, height);
    }

    /**
     * Checks if there is a next page available.
     *
     * @return true if the current page is less than the total number of pages minus one, indicating that a subsequent page exists.
     */
    public boolean hasNextPage() {
        return !isLastPage();
    }

    /**
     * Checks if there is a previous page available.
     *
     * @return true if the current page number is not zero, indicating that a preceding page exists.
     */
    public boolean hasPreviousPage() {
        return !isFirstPage();
    }

    /**
     * Advances to the next page if one is available.
     * This method increments the page number by one if {@link #hasNextPage()} returns true.
     */
    public void nextPage() {
        if (hasNextPage()) {
            setPage(getPage() + 1);
        }
    }

    /**
     * Moves to the previous page if one is available.
     * This method decrements the page number by one if {@link #hasPreviousPage()} returns true.
     */
    public void previousPage() {
        if (hasPreviousPage()) {
            setPage(getPage() - 1);
        }
    }

    /**
     * Checks if the current page is the first page.
     *
     * @return true if the current page is the first page.
     */
    public boolean isFirstPage() {
        return getPage() == 0;
    }

    /**
     * Checks if the current page is the last page.
     *
     * @return true if the current page is the last page.
     */
    public boolean isLastPage() {
        return getPage() >= getPages() - 1;
    }
}
