package cs4660.quizes;

import cs4660.quizes.models.DTO;
import cs4660.quizes.models.Event;
import cs4660.quizes.models.State;

import java.util.*;

/**
 * Here is your quiz entry point and your app
 */
public class App {
    public static void main(String[] args) {
        // to get a state, you can simply call `Client.getState with the id`
        State initialState = Client.getState("10a5461773e8fd60940a56d2e9ef7bf4").get();
        State finalState = Client.getState("e577aa79473673f6158cc73e0e5dc122").get();

        breadthFirst(initialState);
        System.out.println("\n\n\n");
        dijkstraToString(initialState);
    }

    // NOTE: This method looks different since I finished the bfs method before this new code was added to the App file
    // I then tried to refactor it based on the new code, something I didn't have time to do with Dijkstra's
    public static void breadthFirst(State init) {

        //CREATE LISTS TO USE FOR LATER

        // all states
        Queue<State> states = new LinkedList<>();

        // states visited
        Set<State> explored = new HashSet<>();

        // this used to store parents of states
        Map<State, State> parents = new HashMap<>();
        states.add(init);


        while (!states.isEmpty()) {
            State current = states.poll();
            explored.add(current);

            // for every possible action
            for (State s: Client.getState(current.getId()).get().getNeighbors()) {
                // state transition
                if (s.getId().equals("e577aa79473673f6158cc73e0e5dc122")) {
                    // construct actions from endTile
                    //System.out.println("Found solution with depth of: " + findDepth(parents, current, init));
                    System.out.println("BREADTH FIRST PATH");

                    int cost = 0;

                    // for backwards traversal
                    ArrayList<State> pathBack = new ArrayList<State>();

                    pathBack.add(s);
                    pathBack.add(current);


                    // breadth first search attempt here: iterating through the parents to form the path back
                    while (!parents.get(pathBack.get(pathBack.size() - 1)).equals(init)) {
                        pathBack.add(parents.get(pathBack.get(pathBack.size() - 1)));
                    }
                    pathBack.add(init);


                    // here we traverse our path backwards to get our route from beginning node to end node
                    for (int i = pathBack.size() - 1; i >= 0; i--) {
                        if (i > 0) {
                            Event e = Client.stateTransition(pathBack.get(i).getId(), pathBack.get(i - 1).getId()).get().getEvent();
                            State first = pathBack.get(i);
                            State next = pathBack.get(i - 1);
                            // test: to see each state's id
                            //System.out.println(first.getId() + ":" + next.getId());
                            System.out.println(first.getLocation().getName() + ":" + next.getLocation().getName() + ":"
                                    + e.getEffect() + "  \\\\ " + e.getDescription() + "\n");

                            cost += e.getEffect();
                        }
                    }
                    System.out.println("Total cost: " + cost);
                    return;
                }
                if (!explored.contains(s)) {
                    parents.put(s, current);
                    states.add(s);
                }
            }
        }
        System.out.println("Fully explored");
    }

    public static int findDepth(Map<State, State> parents, State current, State start) {
        State c = current;
        int depth = 0;

        while (!c.equals(start)) {
            depth ++;
            c = parents.get(c);
        }

        return depth;
    }

    // -------------------- DIJKSTRA'S ALGORITHM: ATTEMPT --------------------
    public static List<String> dijkstraTraverse(State s) {
        System.out.print("DIJKSTRA'S TRAVERSAL\n");
        int mindistance = 0;
        ArrayList<String> traversal = new ArrayList<String>();
        PriorityQueue<String> states = new PriorityQueue<String>();
        states.add(s.getId());
        traversal.add(s.getId());
        boolean found = false;

        while(!states.isEmpty()) {
            String temp = states.poll();

            if (!temp.equals("e577aa79473673f6158cc73e0e5dc122")) {
                for (State st : Client.getState(temp).get().getNeighbors()) {
                    String next = st.getId();
                    int weight = Client.stateTransition(temp, next).get().getEvent().getEffect();
                    int throughdist = mindistance + weight;
                    if (!next.equals("e577aa79473673f6158cc73e0e5dc122")) {
                        if (throughdist >= mindistance && !traversal.contains(next)) {
                            states.remove(next);
                            traversal.add(next);

                            mindistance = throughdist;
                            states.add(next);
                        }
                    } else {
                        traversal.add(next);
                        found = true;
                        break;
                    }
                }
            } else {
                traversal.add(temp);
                break;
            }
            if (found) break;
        }
        return traversal;
    }

    public static void dijkstraToString(State s1) {
        List<String> traversal = dijkstraTraverse(s1);
        ArrayList<String> stringpath = new ArrayList<String>();

        // We work backwards

        for (int i = traversal.size() - 1; i > 0; i--) {
            stringpath.add(traversal.get(i));
            int j = i - 1;
            while (j >= 0) {
                boolean found = false;
                List<State> neighbors = Arrays.asList(Client.getState(traversal.get(j)).get().getNeighbors());
                for (State s : neighbors) {
                    if (s.getId().equals(traversal.get(i))) {
                        found = true;
                        stringpath.add(traversal.get(j));
                        break;
                    }
                }
                if (found) {
                    i = j;
                    break;
                } else {
                    j--;
                }
            }
        }

        stringpath.add(s1.getId());
        Collections.reverse(stringpath);

        for (int i = 0; i < stringpath.size() - 1; i++) {
            String first = Client.getState(stringpath.get(i)).get().getLocation().getName();
            String next = Client.getState(stringpath.get(i + 1)).get().getLocation().getName();

            State test1 = Client.getState(stringpath.get(i)).get();
            State test2 = Client.getState(stringpath.get(i + 1)).get();

            DTO res = Client.stateTransition(stringpath.get(i), stringpath.get(i + 1)).get();
            Event e = res.getEvent();
            System.out.println(first + ":" + next + ":" + e.getEffect() + "\n");

        }
    }
}