import java.util.ArrayList;
import java.util.Random;

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
                boardState[i][j] = new Dice(diceNum, randSide);
                diceNums.remove(diceNumLocation);
            }
        }
    }





}

class Dice{

    int diceNum;
    char[] diceLetters;
    char currentLetter;

    Dice(int diceNum, int randSide){
        this.diceNum = diceNum + 1;
        this.diceLetters = this.getLetters(this.diceNum);
        this.currentLetter = diceLetters[randSide];
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
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                System.out.println(b.boardState[i][j].currentLetter + ", " + b.boardState[i][j].diceNum);
            }
        }
    }
}
