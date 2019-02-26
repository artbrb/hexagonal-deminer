package sample;

import java.util.ArrayList;

public class HexGroup {
     ArrayList<Hexagon> HexagonList;
     Integer countBombOfGroup;


    public HexGroup(ArrayList<Hexagon> hexagonList, Integer countBombOfGroup) {

        this.HexagonList = hexagonList;
        this.countBombOfGroup = countBombOfGroup;
    }

    public boolean comparisonOfTwoGroups(HexGroup group) {
        Boolean result = true;
        if (this.HexagonList.size() == group.HexagonList.size()
                && this.countBombOfGroup.equals(group.countBombOfGroup)) {
            for (Hexagon hexagon: this.HexagonList) {
                if (group.HexagonList.contains(hexagon)) {
                    result = true;
                } else {
                    return false;
                }
            }
        }
        return result;
    }
    //    вычитание из группы с большим количеством ячеек
    //     группы, содержащейся в ней
    public void subtractingOfSmallGroup(HexGroup child) {
        for (Hexagon hexagon: this.HexagonList) {
            if (child.HexagonList.contains(hexagon)) {
                this.HexagonList.remove(hexagon);
            }
        }
        this.countBombOfGroup -= child.countBombOfGroup;
    }
    //    количество пересекающихся ячеек
    public Integer intersections(HexGroup group) {
        Integer numberOfIntersections = 0;
        for (Hexagon hexagon: this.HexagonList) {
            if (group.HexagonList.contains(hexagon)) {
                numberOfIntersections++;
            }
        }
        return  numberOfIntersections;
    }
   //   список пересекающихся ячеек
    public ArrayList<Hexagon> listIntersections(HexGroup group) {
        ArrayList<Hexagon> interList = new ArrayList<>();
        for (Hexagon hexagon: this.HexagonList) {
            if (group.HexagonList.contains(hexagon)) {
                interList.add(hexagon);
            }
        }
        return  interList;
    }
    //создание новой группы из пересечения двух других
    public HexGroup getIntersection(HexGroup child) {
        Integer bombCountNewHexGroup;
        ArrayList<Hexagon> interList = this.listIntersections(child);
        bombCountNewHexGroup = this.countBombOfGroup - (child.HexagonList.size() - interList.size());
        return new HexGroup(interList, bombCountNewHexGroup);
    }


}
