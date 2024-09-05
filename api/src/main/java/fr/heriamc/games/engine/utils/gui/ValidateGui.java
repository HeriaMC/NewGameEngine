package fr.heriamc.games.engine.utils.gui;

import fr.heriamc.games.api.GameApi;
import fr.heriamc.games.engine.utils.item.ItemBuilder;
import lombok.Setter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

@Setter
public class ValidateGui extends Gui {

    private final ItemStack borderItem, informationItem;

    private final Gui previousGui;
    private final ValidateAction action;

    public ValidateGui(String inventoryName, ItemStack borderItem, ItemStack informationItem, Gui previousGui, ValidateAction action) {
        super(inventoryName, 3);
        this.borderItem = borderItem;
        this.informationItem = informationItem;
        this.previousGui = previousGui;
        this.action = action;
    }

    @Override
    public void setup() {
        setItems(getBorders(), borderItem);

        setItem(11, confirmButton());
        setItem(13, new GuiButton(informationItem));
        setItem(15, denyButton());
    }


    public GuiButton confirmButton() {
        return new GuiButton(new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setDyeColor(DyeColor.GREEN)
                .setName("&7» &aConfirmer").build(), event -> action.validateAction());
    }

    public GuiButton denyButton() {
        return new GuiButton(new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setDyeColor(DyeColor.RED)
                .setName("&7» &cAnnuler").build(), event -> action.denyAction(previousGui, event.getWhoClicked()));
    }

    public interface ValidateAction {

        void validateAction();

        default void denyAction(Gui previousGui, HumanEntity player) {
            GameApi.getInstance().getGuiManager().open(player, previousGui);
        }
    }
}