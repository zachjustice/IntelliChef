package intellichef.intellichef;

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

    public void fillParams(String jsonParams) {
        int strPad = 3;
        int intPad = 2;
        int descriptionInd = jsonParams.indexOf("description") + "description".length() + strPad;
        int recipeInd = jsonParams.indexOf("recipe");
        description = jsonParams.substring(descriptionInd, recipeInd - strPad);

        recipeInd += "recipe".length() + intPad;
        int nameInd = jsonParams.indexOf("name");
        recipePK = Integer.parseInt(jsonParams.substring(recipeInd, nameInd - intPad));

        nameInd += "name".length() + strPad;
        int instructionInd = jsonParams.indexOf("instructions");
        name = jsonParams.substring(nameInd, instructionInd - strPad);

        instructionInd += "instructions".length() + strPad;
        instruction = jsonParams.substring(instructionInd);

    }
}
