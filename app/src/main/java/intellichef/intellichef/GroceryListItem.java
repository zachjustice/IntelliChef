package intellichef.intellichef;

import java.util.List;

/**
 * Created by jatin1 on 2/16/17.
 */
public class GroceryListItem {

    private String mIngredientName;
    private List<String> ingredientDescriptions;

    private boolean isSelected;

    public GroceryListItem(String name, List<String> ingredientDescriptions) {
        this.mIngredientName = name;
        this.ingredientDescriptions = ingredientDescriptions;
    }

    public void toggleSelected() {
        isSelected = !isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getIngredientName() {
        return mIngredientName;
    }

    public List<String> getIngredientDescriptions() {
        return ingredientDescriptions;
    }
}
