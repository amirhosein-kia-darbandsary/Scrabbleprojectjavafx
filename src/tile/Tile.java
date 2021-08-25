package tile;

import tile.TileSet;

public class Tile {
    private char letter;
    private int point;


    public Tile(char letter) {
        this.letter = letter;
        this.point = TileSet.getLetterValue(letter);
    }

    public char getLetter() {
        return letter;
    }

//    public void setLetter(char newLetter) {
//        letter = newLetter;
//    }
//
//    public int getPointValue() {
//        return point;
//    }
}