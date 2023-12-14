import java.io.Serializable;

public class SerializedData implements Serializable {
    private static final long serialVersionUID = 1L; // for version control
    private int intValue;
    private String category;
    private String update;
    private int clientNum = -999;
    private String displayText;
    private char character;
    private int guesses = 6;
    private int attempts = 3;
    private String currentCategory;


    public int getLength() {
        return intValue;
    }

    public void setCategory(String Input){
        this.category = Input;
    }

    public String getCategory(){
        return this.category;
    }

    public void setUpdate(String userUpdate){
        this.update = userUpdate;
    }

    public String getUpdate(){
        return this.update;
    }

    public int getClientNum(){
        return clientNum;
    }

    public void setClientNum(int temp){
        clientNum = temp;
    }

    public void setDisplay(String text){
        displayText = text;
    }

    public String getDisplay(){
        return this.displayText;
    }

    public void setChar(char temp){
         this.character = temp;
    }

    public char getChar(){
        return this.character;
    }

    public void decrementGuess(){
        guesses -= 1;
    }

    public void setGuess(int guess){
        this.guesses = guess;
    }

    public int getGuess(){
        return this.guesses;
    }

    public void decrementAttempt(){
        this.attempts -= 1;
    }

    public void setAttempts(int num){
        this.attempts = num;
    }

    public int getAttempts(){
        return attempts;
    }

    public void setCurrentCategory(String temp){
        currentCategory = temp;
    }
    public String getCurrentCategory(){
        return currentCategory;
    }

}