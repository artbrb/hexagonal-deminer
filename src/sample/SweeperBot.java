package sample;

import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.ArrayList;

import static sample.Main.*;


public class SweeperBot {

    ArrayList<HexGroup> groups;

    public SweeperBot(Hexagon[][] filed, Text[][] flagField) {

            //логический алгоритм
            for (int row = 0; row < SIZE_OF_FIELD; row++) {
                for (int column = 0; column < SIZE_OF_FIELD; column++) {

                    if (!field[row][column].notOpen() && field[row][column].getBombCount() > 0) {
                        ArrayList<Hexagon> list = new ArrayList<>();

                        if (row % 2 == 0) {

                            for (Pair<Integer, Integer> move : movesAroundForEvenRow) {
                                if (checkOutOfField(row, column, move)) {
                                    if (field[row + move.getKey()][column + move.getValue()].notOpen() &&
                                            !field[row + move.getKey()][column + move.getValue()].getFlagStatus()) {
                                        list.add(filed[row + move.getKey()][column + move.getValue()]);
                                    }
                                }
                            }

                        } else {

                            for (Pair<Integer, Integer> move : movesAroundForOddRow) {
                                if (checkOutOfField(row, column, move)) {
                                    if (field[row + move.getKey()][column + move.getValue()].notOpen() &&
                                            !field[row + move.getKey()][column + move.getValue()].getFlagStatus()) {
                                        list.add(filed[row + move.getKey()][column + move.getValue()]);
                                    }
                                }
                            }
                        }

                        HexGroup group = new HexGroup(list, field[row][column].getBombCount());
                    }
                }
            }

            boolean repeat;
            do {
                repeat = false;
                for (int i = 0; i < groups.size() - 1; i++) {
                    HexGroup groupF = groups.get(i);
                    for (int j = i + 1; j < groups.size(); j++) {
                        HexGroup groupS = groups.get(j);

                        if (groupF.comparison(groupS)) {
                            groups.remove(j--);
                            break;
                        }

                        HexGroup parent;
                        HexGroup child;
                        if (groupF.HexagonList.size() > groupS.HexagonList.size()) {
                            parent = groupF;
                            child = groupS;
                        } else {
                            child = groupF;
                            parent = groupS;
                        }
                        if (parent.HexagonList.contains(child.HexagonList)) {
                            parent.subtraction(child);
                            repeat = true;
                        } else if (groupF.intersections(groupS) > 0) {
                            if (groupF.countBombOfGroup > groupS.countBombOfGroup) {
                                parent = groupF;
                                child = groupS;
                            } else {
                                child = groupF;
                                parent = groupS;
                            }

                            HexGroup intersection = parent.getIntersection(child);

                            if (intersection.countBombOfGroup == child.countBombOfGroup) {
                                groups.add(intersection);
                                parent.subtraction(intersection);
                                child.subtraction(intersection);
                                repeat = true;
                            }
                        }

                    }
                }
            } while (repeat);


        }

}
