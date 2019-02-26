package sample;

import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.*;

import static sample.Main.*;


class Bot {
    private static final Double requiredAccuracy = 0.001;
    private static Random random = new Random();
    private List<HexGroup> groups = new ArrayList<>();

    Bot(){

        firstMove();
        mainAlgorithm(generatingGroups(field));
        probabilityAlgorithm();
        randomMove();
    }

    public List<HexGroup> generatingGroups(Hexagon[][] filed) {

//        формирование групп(ячеек прилегающих к открытой ячейке с ненулевым количеством бомб вокруг)
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
                        groups.add(group);
                    }
                }
            }

            boolean repeat;
            do {
                repeat = false;
                for (int i = 0; i < groups.size() - 1; i++) {
                    HexGroup groupFirst = groups.get(i);
                    for (int j = i + 1; j < groups.size(); j++) {
                        HexGroup groupSecond = groups.get(j);
//                        если группы совпадают, то удаляем вторую
                        if (groupFirst.comparisonOfTwoGroups(groupSecond)) {
                            groups.remove(j--);
                            break;
                        }

                        HexGroup parent;
                        HexGroup child;
                        if (groupFirst.HexagonList.size() > groupSecond.HexagonList.size()) {
                            parent = groupFirst;
                            child = groupSecond;
                        } else {
                            parent = groupSecond;
                            child = groupFirst;
                        }
//                    если группа с большим количеством ячеек, содержит в себе другую, то из большей вычитаем меньшую
                        if (parent.HexagonList.containsAll(child.HexagonList)) {
                            parent.subtractingOfSmallGroup(child);
                            repeat = true;
//                      если группы пересекаются, то создаём новую из пересекающихся ячеек
                        } else if (groupFirst.intersections(groupSecond) > 0) {
                            if (groupFirst.countBombOfGroup > groupSecond.countBombOfGroup) {
                                parent = groupFirst;
                                child = groupSecond;
                            } else {
                                parent = groupSecond;
                                child = groupFirst;
                            }

                            HexGroup intersection = parent.getIntersection(child);

                            if (intersection.countBombOfGroup == child.countBombOfGroup
                                    && intersection.HexagonList.size() > 0) {
                                groups.add(intersection);
                                parent.subtractingOfSmallGroup(intersection);
                                child.subtractingOfSmallGroup(intersection);
                                repeat = true;
                            }
                        }

                    }
                }
            } while (repeat);
            return groups;
        }

    //В группы, в которых одна ячейка и одна бомба ставится флаг
    //В группы, в которых нет бомб, открываем ячейки
    public void mainAlgorithm(List<HexGroup> groups) {
        for (HexGroup group : groups) {
            if (group.countBombOfGroup == 0) {
                for (Hexagon hexagon : group.HexagonList) {
                    openHexagons(hexagon.rowCoordinate, hexagon.columnСoordinate);
                }

            } else {
                if (group.countBombOfGroup == group.HexagonList.size()) {
                    for (Hexagon hexagon : group.HexagonList) {
                        invertFlag(hexagon.rowCoordinate, hexagon.columnСoordinate);
                    }

                }
            }
        }

    }




    public boolean probabilityAlgorithm() {
        Map<Hexagon, Double> hexagonsWithProb = new HashMap<>();
        // цикл устанавливает единое значение вероятности в каждой ячейке, учитывая влияние соседних групп
        for (HexGroup group : groups) {
            for (Hexagon hexagon : group.HexagonList) {
                Double value = hexagonsWithProb.get(hexagon);
                // если ячейка еще не в мапе, добавляем
                if (!hexagonsWithProb.containsKey(hexagon)) {
                    hexagonsWithProb.put(hexagon, (double) group.countBombOfGroup / group.HexagonList.size());
                }
                //если ячейка в мапе, то корректируем ее значение
                else
                    hexagonsWithProb.put(hexagon,
                            1.0 - (1.0 - value) * (1.0 - (group.countBombOfGroup / group.HexagonList.size())));
            }
        }
        double delta;
//        Корректировка,чтобы сумма вероятностей в группе была равна количеству мин в группе
        do {
            delta = 0.0;
            for (HexGroup group : groups) {
                List<Double> probabilitiesOfGroup = new ArrayList<>();

                for (int i = 0; i < group.HexagonList.size(); i++) {
                    probabilitiesOfGroup.add(hexagonsWithProb.get(group.HexagonList.get(i)));
                }

                double sumOfProbsInGroup = 0.0;

                for (Double d : probabilitiesOfGroup) {
                    sumOfProbsInGroup += d;
                }
                Double correctCoef = group.countBombOfGroup / sumOfProbsInGroup;
                Double newDelta = (Math.abs(sumOfProbsInGroup - group.countBombOfGroup));
                if (newDelta > delta) {
                    delta = newDelta;
                }
                for (Hexagon hex : group.HexagonList) {
                    hexagonsWithProb.put(hex, hexagonsWithProb.get(hex) * correctCoef);
                }

            }
        } while (delta > requiredAccuracy);

        double bestProbVersion = 5.0;
        Hexagon bestHexagonVersion = null;
//      выбор ячейки с наименьшей вероятностью содержать бомбу
        for (Map.Entry<Hexagon, Double> entry : hexagonsWithProb.entrySet()) {
            if (entry.getKey().notOpen() && entry.getValue() < bestProbVersion) {
                bestProbVersion = entry.getValue();
                bestHexagonVersion = entry.getKey();
            }
        }
        if (bestProbVersion == 5.0 || bestHexagonVersion == null) {
            return false;
        }
        openHexagons(bestHexagonVersion.rowCoordinate, bestHexagonVersion.columnСoordinate);
        return true;
    }

    public void randomMove() {
            int x;
            int y;
            do {
                x = random.nextInt(SIZE_OF_FIELD);
                y = random.nextInt(SIZE_OF_FIELD);
            } while (!field[x][y].getFlagStatus() && field[x][y].notOpen());
            openHexagons(x, y);
    }

    public void firstMove() {
        int x = random.nextInt(SIZE_OF_FIELD);
        int y = random.nextInt(SIZE_OF_FIELD);
        placementAndCountingBombs(x ,y);
        openHexagons(x, y);
        firstClickAlert++;
    }


}





