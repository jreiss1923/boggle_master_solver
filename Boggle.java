import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.LinkedHashSet;

//contains the board and word finding functions
class BoggleBoard {

    Dice[][] boardState;
    //variable to help make dice placement random
    ArrayList<Integer> diceNums = new ArrayList<>();
    long startTime;

    //keeps track of possible numbers of dice
    void instantiateDiceNums(){
        for(int i = 0; i < 25; i++){
            diceNums.add(i);
        }
    }

    //instantiates the boardstate and starting time
    BoggleBoard(){
        this.boardState = new Dice[5][5];
        this.startTime = System.currentTimeMillis();
    }

    //generates a board from the possible dice
    void generateBoard(){
        this.instantiateDiceNums();
        Random rand = new Random();

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                int diceNumLocation = rand.nextInt(diceNums.size());
                int diceNum = diceNums.get(diceNumLocation);
                int randSide = rand.nextInt(6);
                boardState[i][j] = new Dice(diceNum, randSide, j, i);
                diceNums.remove(diceNumLocation);
            }
        }
    }

    //prints the current boardstate
    void printBoard(){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                System.out.print(boardState[i][j].currentLetter + " ");
            }
            System.out.println();
        }
    }

    //prints the dice at each position in the board
    void printDice(){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                System.out.print("{");
                for(char letter : boardState[i][j].diceLetters){
                    System.out.print(letter + " ");
                }
                System.out.print("} ");
            }
            System.out.println();
        }
    }

    //calculates the boggle score for a given list of words
    int calculateScore(ArrayList<String> words){
        int totalScore = 0;
        for(String s : words){
            if(s.length() == 4){
                totalScore += 1;
            }
            else if(s.length() == 5){
                totalScore += 2;
            }
            else if(s.length() == 6){
                totalScore += 3;
            }
            else if(s.length() == 7){
                totalScore += 5;
            }
            else if(s.length() >= 8){
                totalScore += 11;
            }
        }
        return totalScore;
    }

    //removes all duplicates from the list of words
    //idk how this works
    ArrayList<String> removeAllDuplicates(ArrayList<String> words){
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(words);

        return new ArrayList<>(hashSet);
    }

    //removes all 4 letter plurals from the list of words
    //against boggle master rules
    ArrayList<String> remove4LetterPlurals(ArrayList<String> words, ArrayList<String> dictWords){
        ArrayList<String> wordsCopy = new ArrayList<>();
        wordsCopy.addAll(words);

        for(String s : wordsCopy){
            if(s.length() == 4){
                if(s.charAt(3) == 's' && !(s.charAt(2) == 's') && dictWords.contains(s.substring(0, 3))){
                    words.remove(s);
                }
            }
        }

        return words;
    }

    //returns the elapsed time
    //unnecessary now that trie has been implemented
    long elapsedTime(){
        return (System.currentTimeMillis() - this.startTime)/1000;
    }

    //gets all correct words
    ArrayList<String> getAllCorrectWords(){
        ArrayList<String> allWords = new ArrayList<>();
        ArrayList<String> dictWords = this.scanDictWords();

        allWords.addAll(getAllWords(4));
        allWords.addAll(getAllWords(5));
        allWords.addAll(getAllWords(6));
        allWords.addAll(getAllWords(7));
        allWords.addAll(getAllWords(8));
        allWords.addAll(getAllWords(9));

        ArrayList<String> correctWords = new ArrayList<>();

        for(int i = 0; i < allWords.size(); i++){
            if(this.elapsedTime() >=180){
                return this.remove4LetterPlurals(correctWords, dictWords);
            }
            else {
                if (dictWords.contains(allWords.get(i))) {
                    System.out.println(allWords.get(i) + " is in the dictionary");
                    correctWords.add(allWords.get(i));
                } else {
                    System.out.println(allWords.get(i) + " is not in the dictionary");
                }
            }
        }

        this.remove4LetterPlurals(correctWords, dictWords);
        this.removeAllDuplicates(correctWords);

        return correctWords;

    }

    //adds all words from dictionary.txt to arraylist
    ArrayList<String> scanDictWords(){
        ArrayList<String> dictWords = new ArrayList<>();
        try {
            File f = new File("C:\\Users\\Joshua Reiss\\IdeaProjects\\Boggle\\src\\dictionary.txt");
            Scanner sc = new Scanner(f);

            while(sc.hasNextLine()){
                dictWords.add(sc.nextLine());
            }
        }
        catch (IOException e){
            System.out.println("Could not find dictionary.txt file in your folder");
        }

        return dictWords;
    }

    //gets all words from board given length of word
    ArrayList<String> getAllWords(int lengthOfWord){
        ArrayList<String> allWords = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                allWords.addAll(this.getWordCombos(this.boardState[i][j], lengthOfWord - 1));
            }
        }
        return allWords;
    }

    //gets all words from board given length of word and a starting position
    ArrayList<String> getWordCombos(Dice startingDice, int lengthOfWord){
        ArrayList<String> allCombos = new ArrayList<>();

        ArrayList<Dice> adjacentDice = this.getAdjacentDice(startingDice);

        ArrayList<String> adjacentWords = new ArrayList<>();
        ArrayList<ArrayList<Dice>> encounteredDice = new ArrayList<>();

        for(Dice d : adjacentDice){
            adjacentWords.add("" + startingDice.currentLetter + d.currentLetter);
            ArrayList<Dice> temp = new ArrayList<>();
            temp.add(d);
            temp.add(startingDice);
            encounteredDice.add(temp);
        }

        for(int i = 0; i < adjacentWords.size(); i++){
            allCombos.addAll(this.getWords(adjacentWords.get(i), adjacentDice.get(i), encounteredDice.get(i), lengthOfWord - 1));
        }

        return allCombos;
    }

    //recurses through a pattern, adding all possible patterns to the startingWord
    ArrayList<String> getWords(String startingWord, Dice startingDice, ArrayList<Dice> encounteredDice, int lengthOfWord){
        if(lengthOfWord == 0){
            ArrayList<String> temp = new ArrayList<>();
            temp.add(startingWord);
            return temp;
        }
        else {
            ArrayList<Dice> adjacentDice = this.getAdjacentDice(startingDice);
            ArrayList<String> combinedWords = new ArrayList<>();
            ArrayList<String> allCombos = new ArrayList<>();


            for (Dice d : encounteredDice) {
                if (adjacentDice.contains(d)) {
                    adjacentDice.remove(d);
                }
            }

            for (int i = 0; i < adjacentDice.size(); i++) {
                combinedWords.add(startingWord + adjacentDice.get(i).currentLetter);
                ArrayList<Dice> tempDiceEncountered = new ArrayList<>();
                tempDiceEncountered.addAll(encounteredDice);
                tempDiceEncountered.add(adjacentDice.get(i));

                allCombos.addAll(this.getWords(combinedWords.get(i), adjacentDice.get(i), tempDiceEncountered, lengthOfWord - 1));
            }

            return allCombos;
        }




    }

    //gets all dice adjacent to one another
    ArrayList<Dice> getAdjacentDice(Dice startingDice){
        ArrayList<Dice> adjacentDice = new ArrayList<>();
        if(startingDice.posy - 1 >= 0){
            adjacentDice.add(boardState[startingDice.posy - 1][startingDice.posx]);
        }
        if(startingDice.posy + 1 <= 4){
            adjacentDice.add(boardState[startingDice.posy + 1][startingDice.posx]);
        }
        if(startingDice.posx - 1 >= 0){
            adjacentDice.add(boardState[startingDice.posy][startingDice.posx - 1]);
            if(startingDice.posy - 1 >= 0){
                adjacentDice.add(boardState[startingDice.posy - 1][startingDice.posx - 1]);
            }
            if(startingDice.posy + 1 <= 4){
                adjacentDice.add(boardState[startingDice.posy + 1][startingDice.posx - 1]);
            }
        }
        if(startingDice.posx + 1 <= 4){
            adjacentDice.add(boardState[startingDice.posy][startingDice.posx + 1]);
            if(startingDice.posy - 1 >= 0){
                adjacentDice.add(boardState[startingDice.posy - 1][startingDice.posx + 1]);
            }
            if(startingDice.posy + 1 <= 4){
                adjacentDice.add(boardState[startingDice.posy + 1][startingDice.posx + 1]);
            }
        }
        return adjacentDice;
    }





}

//dice class
class Dice{

    int diceNum;
    char[] diceLetters;
    char currentLetter;
    int posx;
    int posy;

    //dice constructor to create a die given a number and a random side
    Dice(int diceNum, int randSide, int posx, int posy){
        this.diceNum = diceNum + 1;
        this.diceLetters = this.getLetters(this.diceNum);
        this.currentLetter = diceLetters[randSide];
        this.posx = posx;
        this.posy = posy;
    }

    //function that decides which die to choose given a die number
    char[] getLetters(int diceNum){
        if(diceNum == 1){
            char[] diceList1 = {'a', 'i', 's', 'r', 'f', 'a'};
            return diceList1;
        }
        else if(diceNum == 2){
            char[] diceList2 = {'e', 'n', 's', 's', 's', 'u'};
            return diceList2;
        }
        else if(diceNum == 3){
            char[] diceList3 = {'a', 'e', 'e', 'e', 'e', 'm'};
            return diceList3;
        }
        else if(diceNum == 4){
            char[] diceList4 = {'d', 'r', 'n', 'l', 'd', 'o'};
            return diceList4;
        }
        else if(diceNum == 5){
            char[] diceList5 = {'n', 'u', 'o', 'o', 't', 'w'};
            return diceList5;
        }
        else if(diceNum == 6){
            char[] diceList6 = {'f', 'r', 'p', 'i', 's', 'y'};
            return diceList6;
        }
        else if(diceNum == 7){
            char[] diceList7 = {'d', 'r', 'h', 'l', 'h', 'o'};
            return diceList7;
        }
        else if(diceNum == 8){
            char[] diceList8 = {'d', 'r', 'n', 'l', 'h', 'h'};
            return diceList8;
        }
        else if(diceNum == 9){
            char[] diceList9 = {'a', 'e', 'e', 'u', 'g', 'm'};
            return diceList9;
        }
        else if(diceNum == 10){
            char[] diceList10 = {'c', 'e', 'l', 't', 'p', 'i'};
            return diceList10;
        }
        else if(diceNum == 11){
            char[] diceList11 = {'a', 'a', 'r', 's', 'f', 'a'};
            return diceList11;
        }
        else if(diceNum == 12){
            char[] diceList12 = {'e', 't', 't', 't', 'o', 'm'};
            return diceList12;
        }
        else if(diceNum == 13){
            char[] diceList13 = {'a', 'e', 'n', 'n', 'n', 'd'};
            return diceList13;
        }
        else if(diceNum == 14){
            char[] diceList14 = {'a', 'f', 'r', 's', 'y', 'i'};
            return diceList14;
        }
        else if(diceNum == 15){
            char[] diceList15 = {'g', 'o', 'v', 'r', 'w', 'r'};
            return diceList15;
        }
        else if(diceNum == 16){
            char[] diceList16 = {'h', 'i', 'r', 'y', 'p', 'r'};
            return diceList16;
        }
        else if(diceNum == 17){
            char[] diceList17 = {'o', 'o', 't', 'u', 'o', 't'};
            return diceList17;
        }
        else if(diceNum == 18){
            char[] diceList18 = {'c', 'c', 's', 't', 'n', 'w'};
            return diceList18;
        }
        else if(diceNum == 19){
            char[] diceList19 = {'d', 'h', 'h', 't', 'o', 'n'};
            return diceList19;
        }
        else if(diceNum == 20){
            char[] diceList20 = {'b', 'k', 'q', 'z', 'x', 'j'};
            return diceList20;
        }
        else if(diceNum == 21){
            char[] diceList21 = {'a', 'e', 'm', 'n', 'n', 'g'};
            return diceList21;
        }
        else if(diceNum == 22){
            char[] diceList22 = {'c', 'e', 's', 'p', 's', 'i'};
            return diceList22;
        }
        else if(diceNum == 23){
            char[] diceList23 = {'a', 'e', 'e', 'e', 'e', 'a'};
            return diceList23;
        }
        else if(diceNum == 24){
            char[] diceList24 = {'e', 'i', 't', 't', 'i', 'i'};
            return diceList24;
        }
        else{
            char[] diceList25 = {'c', 'e', 'l', 't', 'i', 'i'};
            return diceList25;
        }

    }

}

class test{

    public static void main(String[] args){

        long start = System.currentTimeMillis();
        BoggleBoard b = new BoggleBoard();
        b.generateBoard();
        b.printBoard();
        b.printDice();
        ArrayList<String> words = b.removeAllDuplicates(b.getAllCorrectWords());
        for(String s : words){
            System.out.println(s);
        }
        long finish = System.currentTimeMillis();
        long elapsed = (finish - start)/1000;
        long minutes = elapsed/60;
        long seconds = elapsed%60;
        System.out.println(minutes + ":" + seconds + " has passed");
        System.out.println(b.calculateScore(words));


    }


}