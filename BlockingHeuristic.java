
/**
 * This is a class for the blocking
 * heuristic.  This heuristic returns zero for goal states, and
 * otherwise returns one plus the number of cars blocking the path of
 * the goal car to the exit.  
 */
import java.util.*;

public class BlockingHeuristic implements Heuristic {
    private int goalFixedY;
    private int numCars;
    private HashSet<Integer> horizontalCars;
    private HashSet<Integer> verticalCars;

    public BlockingHeuristic(Puzzle puzzle) {
        if (puzzle.getCarSize(0) != 2)
            throw new IllegalArgumentException("Goal car's length isn't 2!");
        if (puzzle.getCarOrient(0))
            throw new IllegalArgumentException("Goal car is vertically oriented!");

        verticalCars = new HashSet<Integer>();
        goalFixedY = puzzle.getFixedPosition(0);
        numCars = puzzle.getNumCars();

        // store only the vertical cars
        for (int i = 1; i < numCars; i++) {
            if (puzzle.getCarOrient(i))
                verticalCars.add(i);
        }
    }

    /**
     * returns the value of the heuristic function at the given state.
     */
    public int getValue(State state) {
        Puzzle puzzle = state.getPuzzle();
        int goalVarX = state.getVariablePosition(0);
        int blockedCount = 0;

        // for each vertical car, determine if it is blocking
        for (Integer i : verticalCars) {
            // check that x coordinate of blocking car is greater
            if (puzzle.getFixedPosition(i) < goalVarX + 2)
                continue;

            // check that y coordinates of blocking car match that of goal car
            for (int j = 0; j < puzzle.getCarSize(i); j++) {
                if (state.getVariablePosition(i) + j == this.goalFixedY) {
                    blockedCount++;
                    continue;
                }
            }
        }

        if (state.isGoal())
            return 0;
        else
            return blockedCount + 1;
    }

}
