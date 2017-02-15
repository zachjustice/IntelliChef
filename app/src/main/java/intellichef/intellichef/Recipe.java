package intellichef.intellichef;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sally on 2/12/17.
 */

public class Recipe {
    private String description;
    private String name;
    private String instruction;
    private int recipePK;

    public Recipe() {
        this("", "", "" , -1);
    }
    public Recipe(String description, String name, String instruction, int recipePK) {
        this.description = description;
        this.name = name;
        this.instruction = instruction;
        this.recipePK = recipePK;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public void setRecipePK(int recipePK) {
        this.recipePK = recipePK;
    }
    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getInstruction() {
        return instruction;
    }

    public int getRecipePK() {
        return recipePK;
    }

    public void fillParams(JSONObject recipe) {
        try {
            this.description = recipe.getString("description");
            this.recipePK = recipe.getInt("recipe");
            this.name = recipe.getString("name");
            this.instruction = recipe.getString("instructions");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
