package path;

import exceptions.NoPathException;
import tile.Hexagon;

import java.util.*;

/*
Greedy best first search algorithm
*/

public class PathFinder {
    private static PriorityQueue<SearchTile> queue = new PriorityQueue();
    private static Set<Hexagon> set = new HashSet<>();

    public static List<Hexagon> findPath(Hexagon origin, Hexagon target) throws NoPathException {
        LinkedList<Hexagon> pathHexagons= new LinkedList<>();

        if (origin == target) {
            return pathHexagons; }

        SearchTile finder = new SearchTile(origin, target, null);

        while (finder.getHex() != target) {
            queue.addAll(addToQueue(finder.getHex().getNeighbors(), finder, target));
            if (queue.isEmpty()) {
                throw new NoPathException("Path was not found");
            } else {
                finder = queue.poll();
            }
        }

        while (finder.getCALLER() != null) {
            pathHexagons.add(finder.getHex());
            finder = finder.getCALLER();
        }
        pathHexagons.add(finder.getHex());
        queue.clear();
        set.clear();
        return pathHexagons;
    }

    private static PriorityQueue<SearchTile> addToQueue(List<Hexagon> neighborsList, SearchTile caller, Hexagon target) {
        PriorityQueue<SearchTile> queue = new PriorityQueue<>();
        for (Hexagon hex : neighborsList) {
            SearchTile dh = new SearchTile(hex, target, caller);
            if (set.add(dh.getHex())) {
                queue.add(dh);
            }
        }
        return queue;
    }
}