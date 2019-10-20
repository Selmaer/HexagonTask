package path;

import tile.Hexagon;

public class DistanceHex implements  Comparable<DistanceHex> {
    private final Hexagon HEX;
    private final DistanceHex CALLER;
    private final double DISTANCE;

    public DistanceHex(Hexagon HEX, Hexagon target, DistanceHex caller) {
        this.HEX = HEX;
        this.DISTANCE = HEX.getDistance(target.getCenterPoint());
        this.CALLER = caller;
    }

    public Hexagon getHex() {
        return HEX;
    }

    public double getDISTANCE() {
        return DISTANCE;
    }

    @Override
    public int compareTo(DistanceHex distanceHex) {
        if (DISTANCE > distanceHex.DISTANCE) {
            return 1;
        } else if (DISTANCE < distanceHex.DISTANCE) {
            return -1;
        } else {
            return 0;
        }
    }

    public DistanceHex getCALLER() {
        return CALLER;
    }
}
