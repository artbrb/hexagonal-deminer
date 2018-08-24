package sample;

import java.awt.*;

public class Hexagon {
    private boolean isOpen;
    private boolean isMine;
    private boolean isFlag;
    private boolean bangMine;
    private int numbersOfBombsNear;

    void open() {
        isOpen = true;
        bangMine = isMine;
        int countOpenedCells = 0;
        if (!isMine) countOpenedCells++;
    }

    boolean isNotOpen() {
        return !isOpen;
    }

    boolean mine() {
        return isMine = true;
    }

    boolean isMined() {
        return isMine;
    }

    void invertFlag() {
        isFlag = !isFlag;
    }

     void setCountBomb(int count) {
        numbersOfBombsNear = count;
    }
    int getCountBomb() {
        return numbersOfBombsNear;
    }

    void pain() {

    }
}
