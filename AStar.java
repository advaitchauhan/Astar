
/**
 * This is the class that performs A* search on a given
 * rush hour puzzle with a given heuristic.  The main search
 * computation is carried out in the constructor.
 * The solution (a path from the initial state to
 * a goal state) is returned as an array of <tt>State</tt>s called
 * <tt>path</tt> (where the first element <tt>path[0]</tt> is the
 * initial state).  If no solution is found, the <tt>path</tt> field
 * is set to <tt>null</tt>. 
 */
import java.util.*;

public class AStar {

	/** The solution path is stored here */
	public State[] path;

	/**
	 * This is the constructor that performs A* search to compute a solution for the
	 * given puzzle using the given heuristic.
	 */
	public AStar(Puzzle puzzle, Heuristic heuristic) {

		Node goalNode = null;

		// initialize frontier and explored set
		PriorityQueue<Node> frontier = new PriorityQueue<Node>(10, new Comparator<Node>() {
			public int compare(Node n1, Node n2) {
				int fN1 = heuristic.getValue(n1.getState()) + n1.getDepth();
				int fN2 = heuristic.getValue(n2.getState()) + n2.getDepth();

				if (fN1 < fN2)
					return -1;
				else if (fN1 > fN2)
					return 1;
				else
					return 0;
			}
		});

		HashMap<State, Integer> frontierMap = new HashMap<State, Integer>(); // parallel frontier containing states, for
																				// lookup purposes
		HashSet<State> explored = new HashSet<State>(); // initialize explored set

		// add initial nodes and states to frontier
		frontier.add(puzzle.getInitNode());
		frontierMap.put(puzzle.getInitNode().getState(), puzzle.getInitNode().getDepth());

		// A* search for the goal node
		while (!frontier.isEmpty()) {
			Node curNode = frontier.poll(); // choose node with minimum f(n)

			// check if goal state and return if so - why does a goal check later prevent
			// optimality?
			if (curNode.getState().isGoal()) {
				goalNode = curNode;
				break;
			}

			explored.add(curNode.getState()); // add node to explored (we have already checked that it's not there yet)

			for (Node childNode : curNode.expand()) {
				if ((!explored.contains(childNode.getState())) && (!frontierMap.containsKey(childNode.getState()))) {
					frontier.add(childNode);
					frontierMap.put(childNode.getState(), childNode.getDepth());
				}

				/*
				 * if child state already in frontier, and with higher cost, then replace the
				 * state in the frontier with this lower cost state
				 */
				if (frontierMap.containsKey(childNode.getState())) {
					if (frontierMap.get(childNode.getState()) > childNode.getDepth()) {
						// we find the the corresponding node in the frontier and remove
						// this happens in linear time...
						boolean removed = false;
						Node n = null;
						Iterator<Node> iter = frontier.iterator();
						while (iter.hasNext()) {
							n = iter.next();
							if (n.getState().equals(childNode.getState()))
								continue;
						}

						frontier.remove(n);
						removed = true;

						// make replacements
						frontierMap.put(childNode.getState(), childNode.getDepth());
						frontier.add(childNode);
						if (!removed)
							System.out.println("A node wasn't removed from frontier!");
					}
				}
			}
		}

		if (goalNode == null)
			path = null;

		// populate path
		else {
			ArrayList<State> pathList = new ArrayList<State>();
			Node curNode = goalNode;
			while (curNode != null) {
				pathList.add(curNode.getState());
				curNode = curNode.getParent();
			}
			Collections.reverse(pathList);
			State[] pathArray = new State[pathList.size()];
			path = pathList.toArray(pathArray);
		}

		// run a few checks
		if (!path[path.length - 1].equals(goalNode.getState()))
			System.out.println("Goal state not at the end!");

		if (!path[0].equals(puzzle.getInitNode().getState()))
			System.out.println("Initial state is not at the start!");
	}

}
