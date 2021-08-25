package tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TileSet {
    private int numLetters;
    private ArrayList<Tile> letters;
    private Map<Character, Integer> numLettersMap;
    private static final Map<Character, Integer> letterValueMap = new HashMap<>();
    private static TileSet instance;

    private TileSet() {
        initialize();
    }

    public void initialize(){

        System.out.println("Testing");
        numLettersMap = new HashMap<>();
        //letterValueMap = new HashMap<>();
        letters = new ArrayList<>();
        numLetters = 0;

        for (char tempLetter = 'a'; tempLetter <= 'z'; tempLetter++) {
            switch (tempLetter) {
                case 'a':
                case 'i':
                    addChar(tempLetter,9,1);
                    break;
                case 'b':
                case 'c':
                case 'm':
                case 'p':
                    addChar(tempLetter,2,3);
                    break;
                case 'd':
                    addChar(tempLetter,4,2);
                    break;
                case 'e':
                    addChar(tempLetter,12,1);
                    break;
                case 'f':
                case 'h':
                case 'v':
                case 'w':
                case 'y':
                    addChar(tempLetter,2,4);
                    break;
                case 'g':
                    addChar(tempLetter,3,2);
                    break;
                case 'j':
                case 'x':
                    addChar(tempLetter,1,8);
                    break;
                case 'k':
                    addChar(tempLetter,1,5);
                    break;
                case 'l':
                case 's':
                case 'u':
                    addChar(tempLetter,4,1);
                    break;
                case 'n':
                case 'r':
                case 't':
                    addChar(tempLetter,6,1);
                    break;
                case 'o':
                    addChar(tempLetter,8,1);
                    break;
                case 'q':
                case 'z':
                    addChar(tempLetter,1,10);
                    break;
                default:
                    break;
            }

        }
        // Add the two blank tiles
        addChar('_',2,0);
        letters.add(new Tile('_'));
        letters.add(new Tile('_'));
        numLetters = 2;

        // Adding all the tiles
        char tempLetter = 'a';
        while (tempLetter <= 'z') {
            int count = numLettersMap.get(tempLetter);
            while (count-- > 0) {
                letters.add(new Tile(tempLetter));
                numLetters++;
            }
            tempLetter++;
        }


        Collections.shuffle(letters, new Random(System.nanoTime()));

    }

    public static int getLetterValue(char letter) {
        int val = 0;
        char c = Character.toLowerCase(letter);
        if (letterValueMap.containsKey(c)) {
            val = letterValueMap.get(c);
        }
        return val;
    }

    public Tile getRandomLetter() {
        if (numLetters <= 0)
            return null;

        numLetters--;
        return letters.remove(0);
    }

    public void addChar(char letter, int numLetter, int letterValue){
        numLettersMap.put(letter,numLetter);
        letterValueMap.put(letter, letterValue);
    }

    public static TileSet getInstance() {
        if (instance == null) {
            instance = new TileSet();
        }
        return instance;
    }

    public boolean isEmpty(List<Tile> letterSet) {
        if (letterSet.isEmpty()) {
            return true;
        }
        return false;
    }

    public List<Tile> getWholeSet() {
        if (numLetters < 7)
            return null;
        List<Tile> newLetters = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            newLetters.add(letters.remove(0));
            numLetters--;
        }
        return newLetters;
    }


    public List<Tile> getBagOfLetters() {
        return letters;
    }

    public int getNumLettersInBag() {
        return letters.size();
    }

    public List<Tile> removeRandomLetters() {
        return letters;
    }
}
