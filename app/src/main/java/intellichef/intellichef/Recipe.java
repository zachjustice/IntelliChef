package intellichef.intellichef;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sally on 2/12/17.
 */

public class Recipe {
    private String description;
    private String name;
    private String[] instructions;
    private int recipePK;
    private double rating;
    private String photoUrl;
    private int prepTime;

    public Recipe() {
        this("", "", new String[0], -1, -1, "", -1);
    }
    public Recipe(String description, String name, String[] instruction, int recipePK, double rating, String photoUrl, int prepTime) {
        this.description = description;
        this.name = name;
        this.instructions = instruction;
        this.recipePK = recipePK;
        this.rating = rating;
        this.photoUrl = photoUrl;
        this.prepTime = prepTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInstructions(String[] instructions) {
        this.instructions = instructions;
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

    public String[] getInstructions() {
        return instructions;
    }

    public int getRecipePK() {
        return recipePK;
    }

    public double getRating() {
        return rating;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void fillParams(JSONObject recipe) {
        try {
            this.description = recipe.getString("description");
            this.recipePK = recipe.getInt("recipe");
            this.name = recipe.getString("name");
            this.rating = recipe.getDouble("rating");
            this.photoUrl = recipe.getString("url");
            if (recipe.has("preparation_time")) {
                this.prepTime = recipe.getInt("preparation_time");
            }
            this.instructions = recipe.getString("instructions").split("\\n");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
