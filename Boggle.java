import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

class DictionaryWordFinder{

    String app_id = "1f7905d8";
    String app_key = "7262f34749ae1cd42c90f79626942ac7";


    boolean isWordBoggle(String word){
        JSONParser parser = new JSONParser();
        String website = "https://od-api.oxforddictionaries.com:443/api/v2/entries/en-gb/" + word.toLowerCase() + "?" + "fields=definitions&strictMatch=true";
        try {
            URL url = new URL(website);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("app_id",app_id);
            urlConnection.setRequestProperty("app_key",app_key);

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            JSONObject jsonWord = (JSONObject) parser.parse(stringBuilder.toString());
            if(jsonWord.containsKey("error")){
                return false;
            }
            return true;



        }
        catch (Exception e) {
            return false;
        }
    }

}

class BoggleBoard {

    Dice[][] boardState;

    ArrayList<Integer> diceNums = new ArrayList<Integer>();

    void instantiateDiceNums(){
        for(int i = 0; i < 25; i++){
            diceNums.add(i);
        }
    }

    BoggleBoard(){
        this.boardState = new Dice[5][5];
    }

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

    void printBoard(){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                System.out.print(boardState[i][j].currentLetter + " ");
            }
            System.out.println();
        }
    }

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

    int calculateScore(ArrayList<String> words){
        int totalScore = 0;
        for(String s : words){
            if(s.length() >= 4){
                totalScore += s.length() - 3;
            }
        }
        return totalScore;
    }

    ArrayList<String> getAllCorrectWords(){

        ArrayList<String> allWords = new ArrayList<>();
        ArrayList<String> allWordsCopy = new ArrayList<>();
        DictionaryWordFinder d = new DictionaryWordFinder();

        allWords.addAll(this.getAllWords(4));

        allWordsCopy.addAll(allWords);

        for(String s : allWordsCopy){
            if(!d.isWordBoggle(s)){
                System.out.println(s + " is not in the dictionary.");
                allWords.remove(s);
            }
            else{
                System.out.println(s + " is a word.");
            }
        }

        return allWords;


    }

    ArrayList<String> getAllWords(int lengthOfWord){
        ArrayList<String> allWords = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                allWords.addAll(this.getWordCombos(this.boardState[i][j], lengthOfWord - 1));
            }
        }
        return allWords;
    }

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

class Dice{

    int diceNum;
    char[] diceLetters;
    char currentLetter;
    int posx;
    int posy;

    Dice(int diceNum, int randSide, int posx, int posy){
        this.diceNum = diceNum + 1;
        this.diceLetters = this.getLetters(this.diceNum);
        this.currentLetter = diceLetters[randSide];
        this.posx = posx;
        this.posy = posy;
    }

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
        BoggleBoard b = new BoggleBoard();
        b.generateBoard();
        b.printBoard();
        b.printDice();
        ArrayList<String> correctWords = b.getAllCorrectWords();
        for(String s : correctWords){
            System.out.println(s);
        }
        System.out.println(b.calculateScore(correctWords));



    }


}
