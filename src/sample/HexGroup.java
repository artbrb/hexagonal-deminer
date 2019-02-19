package sample;

import java.util.ArrayList;

public class HexGroup {
     ArrayList<Hexagon> HexagonList;
     Hexagon centralHexagon;
     Integer countBombOfGroup;


    public HexGroup(ArrayList<Hexagon> hexagonList, Integer countBombOfGroup) {
        this.HexagonList = hexagonList;

        this.countBombOfGroup = centralHexagon.getBombCount();
    }

    public boolean comparison(HexGroup group) {
        Boolean result = true;
        if (this.HexagonList.size() == group.HexagonList.size()) {
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

    public void subtraction(HexGroup child) {
        for (Hexagon hexagon: this.HexagonList) {
            if (child.HexagonList.contains(hexagon)) {
                this.HexagonList.remove(hexagon);
            }
        }
        this.countBombOfGroup = this.countBombOfGroup - child.countBombOfGroup;
    }

    public Integer intersections(HexGroup group) {
        Integer numberOfIntersections = 0;
        for (Hexagon hexagon: this.HexagonList) {
            if (group.HexagonList.contains(hexagon)) {
                numberOfIntersections++;
            }
        }
        return  numberOfIntersections;
    }

    public ArrayList<Hexagon> listIntersections(HexGroup group) {
        ArrayList<Hexagon> interList = new ArrayList<>();
        for (Hexagon hexagon: this.HexagonList) {
            if (group.HexagonList.contains(hexagon)) {
                interList.add(hexagon);
            }
        }
        return  interList;
    }

    public HexGroup getIntersection(HexGroup child) {
        Integer bombCountNewHexGroup;
        ArrayList<Hexagon> interList = this.listIntersections(child);
        bombCountNewHexGroup = this.countBombOfGroup - (child.HexagonList.size() - interList.size());
        return new HexGroup(interList, bombCountNewHexGroup);
    }


}
