package intellichef.intellichef;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sally on 2/12/17.
 */

public class Recipe {
    private String description;
    private String name;
    private String[] instructions;
    private String[] ingredients;
    private int recipePK;
    private double rating;
    private String photoUrl;
    private int prepTime;

    public Recipe() {
        this("", "", new String[0], new String[0], -1, -1, "", -1);
    }
    public Recipe(String description, String name, String[] ingredients, String[] instruction, int recipePK, double rating, String photoUrl, int prepTime) {
        this.description = description;
        this.name = name;
        this.instructions = instruction;
        this.ingredients = ingredients;
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
            this.recipePK = recipe.getInt("recipe_pk");
            this.name = recipe.getString("name");
//            this.rating = recipe.getDouble("rating");
            this.photoUrl = recipe.getString("image_url");
            if (recipe.has("preparation_time")) {
                this.prepTime = recipe.getInt("preparation_time");
            }
            Log.wtf("LMAO", "" + recipe.getString("instructions").length());
            Log.wtf("LMAO", "" + recipe.getString("instructions"));
            this.instructions = recipe.getString("instructions").split("\n");
            JSONArray jsonarray = recipe.getJSONArray("ingredients");
            this.ingredients = new String[jsonarray.length()];
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject ingredient = jsonarray.getJSONObject(i);
                String ing = ingredient.getString("description");
                ingredients[i] = ing;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }
}
