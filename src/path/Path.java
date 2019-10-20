package path;

import exceptions.NoPathException;
import tile.Hexagon;

import java.util.*;

public class Path {
    PriorityQueue<DistanceHex> queue = new PriorityQueue();
    Set<Hexagon> set = new HashSet<>();

    public List<Hexagon> find(Hexagon origin, Hexagon target, List<Hexagon> hexMap) throws NoPathException {
        LinkedList<Hexagon> pathHexs= new LinkedList<>();

        if (origin == target) {
            return pathHexs; }

        DistanceHex finder = new DistanceHex(origin, target, null);

        while (finder.getHex() != target) {
            queue.addAll(addToQueue(finder.getHex().getNeighbors(), finder, target));
            if (queue.isEmpty()) {
                throw new NoPathException("Path was not found");
            } else {
                finder = queue.poll();
            }
        }

        while (finder.getCALLER() != null) {
            pathHexs.add(finder.getCALLER().getHex());
            finder = finder.getCALLER();
        }
        pathHexs.removeLast();
        return pathHexs;
    }

    public PriorityQueue<DistanceHex> addToQueue(List<Hexagon> neighborsList, DistanceHex caller, Hexagon target) {
        PriorityQueue<DistanceHex> queue = new PriorityQueue<>();
        for (Hexagon hex : neighborsList) {
            DistanceHex dh = new DistanceHex(hex, target, caller);
            if (set.add(dh.getHex())) {
                queue.add(dh);
            }
        }
        return queue;
    }
}