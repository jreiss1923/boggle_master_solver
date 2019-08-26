import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class BoggleBoardWithTrie extends BoggleBoard{

    Trie dictionary = new Trie();

    //now adds list of words to trie
    ArrayList<String> scanDictWords(){
        ArrayList<String> dictWords = new ArrayList<>();
        try {
            File f = new File("C:\\Users\\Joshua Reiss\\IdeaProjects\\Boggle\\src\\dictionary.txt");
            Scanner sc = new Scanner(f);

            while(sc.hasNextLine()){
                String s = sc.nextLine();
                dictWords.add(s);
                dictionary.insert(s);
            }
        }
        catch (IOException e){
            System.out.println("Error: dictionary.txt file not found");
        }

        return dictWords;
    }

    //now uses trie as reference instead of an arraylist
    ArrayList<String> getAllCorrectWords(){
        ArrayList<String> allWords = new ArrayList<>();
        ArrayList<String> dictWords = this.scanDictWords();
        ArrayList<ArrayList<Dice>> correctDiceWords = new ArrayList<>();

        allWords.addAll(getAllWords(3));
        allWords.addAll(getAllWords(4));
        allWords.addAll(getAllWords(5));
        allWords.addAll(getAllWords(6));
        allWords.addAll(getAllWords(7));
        allWords.addAll(getAllWords(8));

        ArrayList<String> correctWords = new ArrayList<>();

        for(int i = 0; i < allWords.size(); i++){
            if(dictionary.search(allWords.get(i)) && allWords.get(i).length() >= 4){
                correctWords.add(allWords.get(i));
                correctDiceWords.add(this.diceWordPosTracker.get(i));
            }
        }
        this.diceWordPosTracker = correctDiceWords;
        correctWords = this.remove4LetterPlurals(correctWords, dictWords);
        this.remove4LetterDicePlurals(dictWords);
        correctWords = this.removeAllDuplicates(correctWords);
        this.diceWordPosTracker = this.removeAllDiceDuplicates();

        return correctWords;
    }

    //prints board location and coordinates of word
    void findWord(String word) {
        Scanner input = new Scanner(System.in);
        while(!word.equals("n")){
            ArrayList<int[]> wordPos = this.getDicePosOfWord(word);
            for(int[] letterPos : wordPos){
                System.out.println("(" + letterPos[0] + ", " + letterPos[1] + ")");
            }
            this.printWordLocation(word);
            System.out.println("\nWould you like to find another word?");
            word = input.nextLine();
        }
    }

    //prints word location from board
    void printWordLocation(String word){
        ArrayList<int[]> wordPos = this.getDicePosOfWord(word);

        for(int i = 0; i < 5; i++){
            String s = "";
            for(int j = 0; j < 5; j++){
                boolean hasLetterPos = false;
                for(int[] letterPos : wordPos){
                    if(i == letterPos[1] && j == letterPos[0]){
                        s += this.boardState[i][j].currentLetter + " ";
                        hasLetterPos = true;
                    }
                }
                if(hasLetterPos == false){
                    s += "_ ";
                }
            }
            System.out.println(s);
        }
    }

}

class Test{

    public static void main(String[] args){

        Scanner input = new Scanner(System.in);

        BoggleBoardWithTrie b = new BoggleBoardWithTrie();
        b.generateBoard();
        b.printBoard();
        ArrayList<String> words = b.getAllCorrectWords();

        b.printAllWords();

        System.out.println("There are " + b.diceWordPosTracker.size() + " words on the board that score for a total of " + b.calculateScore(words) + " points.");

        System.out.println("\nWould you like to find a word?");
        b.findWord(input.nextLine());

    }


}
