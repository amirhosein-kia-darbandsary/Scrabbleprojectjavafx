package game;

import vector.SizeVector;

public class Board {
    private int boardSize = 15;
    private char[][] gameBoard = new char[boardSize][boardSize];
    private SizeVector sizeVector = new SizeVector();


    public Board() {
        initializeBoard();
    }

    public Board(int height, int width) {
        sizeVector.setSize(width,height);

        initializeBoard();
    }

    public void setBoard(char[][] newBoard) {
        gameBoard = newBoard;
    }

    public char[][] getBoard() {
        return gameBoard;
    }

    public boolean isEmpty() {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public void initializeBoard() {
        gameBoard = new char[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                gameBoard[i][j] = ' ';
            }
        }
    }

    public void setChar(char ch, int i, int j) {
        gameBoard[i][j] = ch;
    }

//    public int getHeight() {
//        return this.sizeVector.getHeight();
//    }
//
//    public int getWidth() {
//        return this.sizeVector.getWidth();
//    }

    public char getChar(int x, int y) {
        return gameBoard[x][y];
    }

//    public int setWidth(int newWidth) {
//        this.sizeVector.setWidth(newWidth);
//        return newWidth;
//    }
//
//    public int setHeight(int newHeight) {
//        this.sizeVector.setHeight(newHeight);
//        return newHeight;
//    }

}
