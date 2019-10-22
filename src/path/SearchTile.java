package path;

import tile.Hexagon;

public class SearchTile implements Comparable<SearchTile> {
    private final Hexagon HEX;
    private final SearchTile CALLER;
    private final double DISTANCE;

    public SearchTile(Hexagon HEX, Hexagon target, SearchTile caller) {
        this.HEX = HEX;
        this.DISTANCE = HEX.getDistance(target.getCenterPoint());
        this.CALLER = caller;
    }

    public Hexagon getHex() {
        return HEX;
    }

    public SearchTile getCALLER() {
        return CALLER;
    }

    @Override
    public int compareTo(SearchTile searchTile) {
        return Double.compare(DISTANCE, searchTile.DISTANCE);
    }
}
