package fr.heriamc.games.engine.utils.gui;

import fr.heriamc.games.engine.utils.Pagination;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.inventory.InventoryClickEvent;

@Getter
@Setter
public abstract class PageableGui<E> extends Gui {

    private final int maxItems;

    private final Pagination<E> pagination;
    private Pagination<E>.Page page;

    public PageableGui(String inventoryName, boolean updatable, int rows, int maxItems) {
        super(inventoryName, rows, updatable);
        this.maxItems = maxItems;
        this.pagination = new Pagination<>(maxItems);
        this.page = pagination.getFirst();
    }

    public PageableGui(String inventoryName, int rows, int maxItems) {
        this(inventoryName, false, rows, maxItems);
    }

    public abstract GuiButton nextPageButton();

    public abstract GuiButton previousPageButton();

    public void nextPage(InventoryClickEvent event) {
        if (pagination.hasNext(page))
            updatePage(pagination.getNext(page));
    }

    public void nextPage() {
        nextPage(null);
    }

    public void previousPage(InventoryClickEvent event) {
        if (pagination.hasPrevious(page))
            updatePage(pagination.getPrevious(page));
    }

    public void previousPage() {
        previousPage(null);
    }

    public void updatePage(Pagination<E>.Page page) {
        this.page = page;
        refresh();
    }

}