package game;

import tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int points;
    private ArrayList<Tile> playerSet;


    public Player() {

    }

    public void addPoints(int val) {
        points += val;
    }

    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public boolean hasName() {
        if (getName() != "" || getName() != null) {
            return true;
        }
        return false;
    }

    public void setPoints(int newPoints) {
        points = newPoints;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setPlayerSet(List<Tile> list) {
        playerSet = (ArrayList<Tile>) list;
    }

    public ArrayList<Tile> getPlayerSet() {
        return playerSet;
    }

    public void deleteTile(char ch) {
        for (int i = 0; i < playerSet.size(); i++) {
            if (playerSet.get(i).getLetter() == ch) {
                playerSet.remove(i);
            }
        }
    }

    public int getPlayerSetSize() {
        return playerSet.size();
    }

    public char getOneLetter(List<Tile> playerSet) {
        return (playerSet.remove(0)).getLetter();
    }
}