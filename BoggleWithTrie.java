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

        allWords.addAll(getAllWords(4));
        allWords.addAll(getAllWords(5));
        allWords.addAll(getAllWords(6));
        allWords.addAll(getAllWords(7));
        allWords.addAll(getAllWords(8));

        ArrayList<String> correctWords = new ArrayList<>();

        for(int i = 0; i < allWords.size(); i++){
            if(dictionary.search(allWords.get(i))){
                correctWords.add(allWords.get(i));
                correctDiceWords.add(this.diceWordPosTracker.get(i));
            }
        }
        this.diceWordPosTracker = correctDiceWords;
        System.out.println(correctDiceWords.size() + " current dice size");
        System.out.println(correctWords.size() + " correct size");

        correctWords = this.remove4LetterPlurals(correctWords, dictWords);
        this.remove4LetterDicePlurals(dictWords);
        System.out.println(correctDiceWords.size() + " current dice size");
        System.out.println(correctWords.size() + " correct size");
        correctWords = this.removeAllDuplicates(correctWords);
        this.diceWordPosTracker = this.removeAllDiceDuplicates();
        System.out.println(correctDiceWords.size() + " current dice size");
        System.out.println(correctWords.size() + " correct size");

        return correctWords;
    }

}

class Test{

    public static void main(String[] args){
        BoggleBoardWithTrie b = new BoggleBoardWithTrie();
        b.generateBoard();
        b.printBoard();
        ArrayList<String> words = b.getAllCorrectWords();

        for(String word : words){
            System.out.println(word);
        }
        for(ArrayList<Dice> dList : b.diceWordPosTracker){
            String s = "";
            for(Dice d : dList){
                s += d.currentLetter;
            }
            System.out.println(s);
        }
        System.out.println(b.calculateScore(words));
    }


}
