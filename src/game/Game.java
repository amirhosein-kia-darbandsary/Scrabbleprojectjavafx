package game;

import tile.Dictionary;
import tile.TileSet;
import vector.Location;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Game {
    private int boardSize = 15;

    private Board mainBoard;
    private TileSet tileSet = TileSet.getInstance();
    private Dictionary dictionary;
    private static Game gameInstance;
    private List<String> errorList;

    private Game() {
        this.mainBoard = new Board();
        this.dictionary = Dictionary.getDictionaryInstance();
        this.errorList = new ArrayList<>();

    }

    public TileSet getTileSet() {
        return tileSet;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public static Game getInstance() {
        if (gameInstance == null) {
            gameInstance = new Game();
        }
        return gameInstance;
    }

    public Location getTopLetter(List<Location> coordinates) {

        Location largest = coordinates.get(0);

        if (isVertical(coordinates)) {
            for (int i = 0; i < coordinates.size(); i++) {
                if (coordinates.get(i).getY() > largest.getY()) {
                    largest = coordinates.get(i);
                }
            }
        }
        return largest;
    }

    public Location getBottomLetter(List<Location> coords) {

        Location smallest = coords.get(0);

        if (isVertical(coords)) {
            for (int i = 0; i < coords.size(); i++) {
                if (coords.get(i).getY() < smallest.getY()) {
                    smallest = coords.get(i);
                }
            }
        }
        return smallest;
    }

    public Location getLeftLetter(List<Location> coords) {

        Location smallest = coords.get(0); // x-value

        if (isHorizontal(coords)) {
            for (int i = 0; i < coords.size(); i++) {
                if (coords.get(i).getX() < smallest.getX()) {
                    smallest = coords.get(i);
                }
            }
        }
        return smallest;
    }

    public Location getRightLetter(List<Location> coords) {

        Location largest = coords.get(0);

        if (isHorizontal(coords)) {
            for (int i = 0; i < coords.size(); i++) {
                if (coords.get(i).getX() > largest.getX()) {
                    largest = coords.get(i);
                }
            }
        }
        return largest;
    }

    public boolean isAdjacentToAWord(Board previousGameBoard, List<Location> coordinates) {

        boolean isAdjacent = false;

        for (int i = 0; i < coordinates.size(); i++) {
            if (isHorizontal(coordinates)) {
                Location leftest = getLeftLetter(coordinates);
                Location rightest = getRightLetter(coordinates);
                if (bordersTop(previousGameBoard, coordinates.get(i))
                        || bordersBottom(previousGameBoard, coordinates.get(i))) {
                    if (bordersLeft(previousGameBoard, leftest)
                            || bordersRight(previousGameBoard, rightest)) {
                        isAdjacent = true;
                    }
                }
            } else if (isVertical(coordinates)) {
                Location topmost = getTopLetter(coordinates);
                Location bottomost = getBottomLetter(coordinates);
                if (bordersLeft(previousGameBoard, coordinates.get(i))
                        || bordersRight(previousGameBoard, coordinates.get(i))) {
                    if (bordersTop(previousGameBoard, topmost)
                            || bordersBottom(previousGameBoard, bottomost)) {
                        isAdjacent = true;
                    }
                }
            }
        }
        return isAdjacent;
    }

    public boolean playedFromRack(Player p, List<Location> coordinates, Board currentGameBoard) {
        List<Character> temp = getWord(currentGameBoard, coordinates);
        boolean flag = true;

        for (int i = 0; i < temp.size(); i++) {
            for (int j = 0; j < p.getPlayerSet().size(); j++) {
                if (temp.get(i) == p.getPlayerSet().get(j).getLetter()) {
                    break;
                }
                if (j == p.getPlayerSet().size()) {
                    flag = false;
                }
            }
        }
        return flag;
    }

    public void replenishPlayerTiles(List<Character> charsEntered, Player p) {
        for (int i = 0; i < charsEntered.size(); i++) {
            char c = Character.toLowerCase(charsEntered.get(i));
            p.deleteTile(c);
        }
        while (p.getPlayerSetSize() < 7) {
            p.getPlayerSet().add(tileSet.getRandomLetter());
        }
    }

    public String checkWords(List<Location> locationLists, char[][] board) {
        StringBuilder sb = new StringBuilder();
        int xDiff = locationLists.get(1).getX() - locationLists.get(0).getX() + 1;
        int yDiff = locationLists.get(1).getY() - locationLists.get(0).getY() + 1;

        if (locationLists.get(0).getY() == locationLists.get(1).getY()  || locationLists.size() == 1) {
            for (int i = 0; i < xDiff; i++) {
                sb.append(Character.toString(board[locationLists.get(0).getX() + i][locationLists.get(0).getY()]));
            }
        }
        else {
            for (int i = 0; i < yDiff; i++) {
                sb.append(Character.toString(board[locationLists.get(0).getX()][locationLists.get(0).getY() + i]));
            }
        }
        return sb.toString();
    }

    public boolean makeFinalMove(char[][] currentBoard, Player p) {

        char[][] previousBoard = mainBoard.getBoard();
        char[][] gameBoard = currentBoard;

        if (Arrays.deepEquals(previousBoard, gameBoard)) {
            return false;
        }

        boolean makemove = true;

        List<Location> newLetters = getNewLetterLocation(previousBoard, gameBoard);

        if (newLetters.size() < 2 && mainBoard.isEmpty()) {
            errorList.add("Word is too short.");
            makemove = false;
        }

        List<Character> charsEntered = getEnteredLetters(newLetters, gameBoard);
        List<String> newWords = allMyNewWords(newLetters, gameBoard);
        int points = pointsForAll(newWords);

        int numberOfWrongLetters = isBlankLetter(charsEntered, p);
        int numOfBlankLetters = containsBlankLetters(charsEntered);


        if (!isHorizontal(newLetters) && !isVertical(newLetters)) {
            errorList.add("Word is not horizontal or vertical.");
            makemove = false;
        }

        if (numberOfWrongLetters > numOfBlankLetters) {
            errorList.add("Word is not played from rack.");
            makemove = false;
        }


        if (!allWords(newWords)) {
            errorList.add("Words are not valid.");
            makemove = false;
        }

        if (makemove) {
            mainBoard.setBoard(gameBoard);
            p.addPoints(points);
            while (numOfBlankLetters > 0) {
                p.deleteTile('_');
                numOfBlankLetters--;
            }
            replenishPlayerTiles(charsEntered, p);
        }
        return makemove;
    }

    public List<String> getErrors() {
        return errorList;
    }

    public int containsBlankLetters(List<Character> charsEntered) {
        int count = 0;
        for (int i = 0; i < charsEntered.size(); i++) {
            if (Character.toLowerCase(charsEntered.get(i)) == '_') {
                count++;
            }
        }
        return count;
    }

    public int isBlankLetter(List<Character> charsEntered, Player p) {
        int count = 0;
        for (int i = 0; i < charsEntered.size(); i++) {
            for (int j = 0; j < p.getPlayerSetSize(); j++) {
                if (Character.toLowerCase(charsEntered.get(i)) == p.getPlayerSet().get(j).getLetter()) {
                    count++;
                    break;
                }
            }
        }
        return charsEntered.size() - count;
    }

    public List<String> allMyNewWords(List<Location> locArray, char[][] gameBoard) {

        List<String> retString = new ArrayList<>();
        List<Location> firstlast = new ArrayList<>();

        if (isHorizontal(locArray) || locArray.size() == 1) {
            firstlast.add(getLeft(locArray, gameBoard));
            firstlast.add(getRight(locArray, gameBoard));
        }
        else {
            firstlast.add(getTop(locArray, gameBoard));
            firstlast.add(getBottom(locArray, gameBoard));
        }

        retString.add(checkWords(firstlast, gameBoard));

        return retString;
    }

    public void getOtherHorizWords(List<Location> loc, char[][] board, List<String> retString) {
        for (int i = 0; i < loc.size(); i++) {
            ArrayList<Location> horiz = new ArrayList<>();
            horiz.add(loc.get(i));
            ArrayList<Location> checkHoriz = new ArrayList<>();
            checkHoriz.add(getTop(loc, board));
            checkHoriz.add(getBottom(loc, board));
            retString.add(checkWords(checkHoriz, board));
        }
    }

    public void getOtherVertWords(List<Location> loc, char[][] board, List<String> retString) {
        for (int i = 0; i < loc.size(); i++) {
            ArrayList<Location> vert = new ArrayList<>();
            vert.add(loc.get(i));
            ArrayList<Location> checkVert = new ArrayList<>();
            checkVert.add(getRight(loc, board));
            checkVert.add(getLeft(loc, board));
            retString.add(checkWords(checkVert, board));
        }
    }

    public List<Location> getNewLetterLocation(char[][] previousBoard, char[][] gameBoard) {

        List<Location> newLocations = new ArrayList<>();
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                if (previousBoard[x][y] != gameBoard[x][y]) {
                    newLocations.add(new Location(x, y));
                }
            }
        }
        return newLocations;
    }

    public boolean checkValidityOfWords(List<String> newWords) {
        for (int i = 0; i < newWords.size(); i++) {
            if (dictionary.contains(newWords.get(i).toLowerCase()) || newWords.size() == 1 || newWords.get(i) == " " ||
                    newWords.get(i) == "") {
                return true;
            }
        }
        return false;
    }

    public boolean isVertical(List<Location> newLetters) {
        for (int i = 0; i < newLetters.size() - 1; i++) {
            if (newLetters.get(i).getX() != newLetters.get(i + 1).getX()) {
                return false;
            }
        }
        return true;
    }

    public List<Character> getWord(Board currentGameBoard, List<Location> arrLoc) {
        List<Character> word = new ArrayList<>();

        for (int i = 0; i < arrLoc.size(); i++) {
            word.add(currentGameBoard.getChar(arrLoc.get(i).getX(), arrLoc.get(i).getY()));
        }
        return word;
    }



    public boolean allWords(List<String> words) {
        for (int i = 0; i < words.size(); i++) {
            String w = words.get(i).toLowerCase();
            System.out.println("Checking: " + w);
            if (isWord(w)) {
                System.out.println("we entered to true sections!!");
                return true;
            }
        }
        System.out.println("we entered to false sections!!");
        return false;
    }

    public boolean isWord(String word) {
        if (dictionary.contains(word))
        {
            System.out.println("we are in isWord section!!");
            return true;
        }
        System.out.println("we are in isWord(False) section!!");
        return false;
    }

    public boolean bordersLeft(Board previousGameBoard, Location coordinates) {
        char c = previousGameBoard.getChar(coordinates.getX() - 1, coordinates.getY());
        if (!Character.isAlphabetic(c)) {
            return false;
        }
        return true;
    }

    public int pointsForAll(List<String> newWords) {
        int total = 0;
        for (int i = 0; i < newWords.size(); i++) {
            total += calculatePointsForWord(newWords.get(i));
        }
        return total;
    }

    public boolean bordersRight(Board previousGameBoard, Location coordinates) {
        char c = previousGameBoard.getChar(coordinates.getX() + 1, coordinates.getY());
        if (!Character.isAlphabetic(c))  {
            return false;
        }
        return true;
    }

    public void initialBoard(char [][] b) {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                b[i][j] = ' ';
            }
        }
    }

    public int calculatePointsForWord(String str) {
        int totalPoints = 0;
        for (int i = 0; i < str.length(); i++) {
            totalPoints += TileSet.getLetterValue(str.charAt(i));
        }

        return totalPoints;
    }


    public boolean isHorizontal(List<Location> newLetters) {
        for (int i = 0; i < newLetters.size() - 1; i++) {
            if (newLetters.get(i).getY() != newLetters.get(i + 1).getY()) {
                return false;
            }
        }
        return true;
    }

    public Location getLeft(List<Location> locationList, char[][] board) {
        int i = 1;
        Location temp = new Location(locationList.get(0).getX(), locationList.get(0).getY());
        while ((locationList.get(0).getX() - i > 0) && (board[locationList.get(0).getX() - i][locationList.get(0).getY()]) != ' ') {
            temp = new Location(locationList.get(0).getX() - i, locationList.get(0).getY());
            i++;
        }
        return temp;
    }

    public Location getRight(List<Location> locationList, char[][] board) {
        int i = 0;
        Location temp = new Location(locationList.get(0).getX(), locationList.get(0).getY());

        while ((locationList.get(0).getX() + i < 15) && (board[locationList.get(0).getX() + i][locationList.get(0).getY()]) != ' ') {
            temp = new Location(locationList.get(0).getX() + i, locationList.get(0).getY());
            i++;
        }
        return temp;
    }

    public Location getTop(List<Location> locationList, char[][] board) {
        int i = 1;
        Location temp = new Location(locationList.get(0).getX(), locationList.get(0).getY());
        while ((locationList.get(0).getY() - i > 0) && board[locationList.get(0).getX()][locationList.get(0).getY() - i] != ' ') {
            temp = new Location(locationList.get(0).getX(), locationList.get(0).getY() - i);
            i++;
        }
        return temp;
    }

    public Location getBottom(List<Location> locationList, char[][] board) {
        int i = 0;
        Location temp = new Location(locationList.get(0).getX(), locationList.get(0).getY());
        while ((locationList.get(0).getY() + i < 15) && board[locationList.get(0).getX()][locationList.get(0).getY() + i] != ' ') {
            temp = new Location(locationList.get(0).getX(), locationList.get(0).getY() + i);
            i++;
        }
        return temp;
    }

    public List<Character> getEnteredLetters(List<Location> newLetters, char[][] board) {
        List<Character> chars = new ArrayList<>();

        for (int i = 0; i < newLetters.size(); i++) {
            chars.add(board[newLetters.get(i).getX()][newLetters.get(i).getY()]);
        }
        return chars;
    }

    public List<String> getWordsString(char [][] currentBoard) {

        char[][] previousBoard = mainBoard.getBoard();
        char[][] gameBoard = currentBoard;

        List<Location> newLetters = getNewLetterLocation(previousBoard, gameBoard);
        return allMyNewWords(newLetters, gameBoard);
    }

    public boolean bordersBottom(Board previousGameBoard, Location coordinates) {
        char c = previousGameBoard.getChar(coordinates.getX(), coordinates.getY() - 1);
        if (!Character.isAlphabetic(c)) {
            return false;
        }
        return true;
    }

    public boolean bordersTop(Board previousGameBoard, Location coordinates) {
        char c = previousGameBoard.getChar(coordinates.getX(), coordinates.getY() + 1);
        if (!Character.isAlphabetic(c)) {
            return false;
        }
        return true;
    }
}