import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class BoggleBoardWithTrie extends BoggleBoard{

    Trie dictionary = new Trie();

    ArrayList<String> scanDictWords(){
        ArrayList<String> dictWords = new ArrayList<>();
        try {
            File f = new File("C:\\Users\\Joshua Reiss\\IdeaProjects\\Boggle\\src\\dictionary.txt");
            Scanner sc = new Scanner(f);

            while(sc.hasNextLine()){
                dictWords.add(sc.nextLine());
                dictionary.insert(sc.nextLine());
            }
        }
        catch (IOException e){
            System.out.println("oopsy poopsy we made a fucky wucky uwu");
        }

        return dictWords;
    }

    ArrayList<String> getAllCorrectWords(){
        ArrayList<String> allWords = new ArrayList<>();
        ArrayList<String> dictWords = this.scanDictWords();

        allWords.addAll(getAllWords(4));
        allWords.addAll(getAllWords(5));
        allWords.addAll(getAllWords(6));
        allWords.addAll(getAllWords(7));

        ArrayList<String> correctWords = new ArrayList<>();

        for(int i = 0; i < allWords.size(); i++){
            if(dictionary.search(allWords.get(i))){
                correctWords.add(allWords.get(i));
            }
        }

        correctWords = this.remove4LetterPlurals(correctWords, dictWords);
        correctWords = this.removeAllDuplicates(correctWords);

        return correctWords;
    }

}

class Test{

    public static void main(String[] args){
        BoggleBoardWithTrie b = new BoggleBoardWithTrie();
        b.generateBoard();
        b.printBoard();
        long startTime = System.currentTimeMillis();
        ArrayList<String> words = b.getAllCorrectWords();
        long endTime = System.currentTimeMillis();

        System.out.println("Elapsed time: " + (endTime - startTime)/1000 + " seconds");
        for(String word : words){
            System.out.println(word);
        }
        System.out.println(b.calculateScore(words));
    }


}
