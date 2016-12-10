import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/

class Player {
    

    // converts our old position vs our desired position into a direction that we can print
    public static String direction(int[] oldcoords, int[] newcoords) {
        if (oldcoords[1] < newcoords[1]) {
            return "RIGHT";
        }
        if (oldcoords[0] < newcoords[0]) {
            return "DOWN";
        }
        if (oldcoords[1] > newcoords[1]) {
            return "LEFT";
        }
        return "UP";
    }
    
    // boardState stores the board itself
    public static int[][] boardState = new int[20][30];
    
    // allNeighbors is a list that stores every possible neighbor for each tile in the board.
    public static ArrayList<int[]>[][] allNeighbors = new ArrayList[20][30];
    
    
    // setup function that initializes the boardstate and stores each tile's neighbors
    public static void setUp() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 30; j++) {
                ArrayList<int[]> n = new ArrayList<int[]>();
                int[] currCoords = new int[]{i, j};
                if (i < 19) {
                    n.add(new int[] { i + 1, j});
                }
                if (i > 0) {
                    n.add(new int[] { i - 1, j});
                } 
                if (j < 29) {
                    n.add(new int[] { i, j + 1});
                }
                if (j > 0) {
                     n.add(new int[] { i, j - 1});
                }
                allNeighbors[i][j] = n;
                boardState[i][j] = -2;
            }
        }
        
        
    }

    // for testing purposes. prints out our current board state.
    public static void printBoard() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 30; j++) {
                System.err.print(boardState[i][j] + " ");
            }
            System.err.println();
        }
    }

    
    public static int getScore(HashMap<Integer, int[]> starts, int numplayers, int myid) {
        HashMap<Integer, HashMap<int[], Integer>> graphs = new HashMap<Integer, HashMap<int[], Integer>>();
        
        // a copy of our boardstate so we don't overwrite anything
        int[][] graphset = new int[20][30];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 30; j++) {
                graphset[i][j] = boardState[i][j];
            }
        }

        // the order players are going (we put ourselves first!)
        int[] order = new int[numplayers];
        int counter = 0;
        for (int i = myid; i < numplayers; i++) {
            order[counter] = i;
            counter++;
        }
        for (int i = 0; i < myid; i++) {
            order[counter] = i;
            counter++;
        }

        // we copy the initial start positions to change them as we go along
        HashMap<Integer, ArrayList<int[]>> algStarts = new HashMap<Integer, ArrayList<int[]>>();  
        for (int k : starts.keySet()) {
            
            algStarts.put(k, new ArrayList<int[]>());
            algStarts.get(k).add(starts.get(k));
            
        }
        
        /*
        DEBUG: check if starting positions copy
        for (int k : algStarts.keySet()) {
            for (int i = 0; i < algStarts.get(k).size(); i++) {
                System.err.println("PLAYER " + k + " START: " + algStarts.get(k).get(i)[0] + " , " + algStarts.get(k).get(i)[1]);
            }
        }*/ 
        int iteration = 1;
        while (true) {
            boolean full = true;
            HashMap<int[], Integer> moves = new HashMap<int[], Integer>();
            for (int o : order) {
                int neighborcount = 0;
                if (algStarts.get(o) != null) {
                for (int[] x : algStarts.get(o)) {
                    //DEBUG: Print player starting coords to compare them
                    //System.err.println("Player " + o + " ALGSTART: " + x[0] + ", " + x[1]);
                    for (int[] n : allNeighbors[x[0]][x[1]]) {
                        if (graphset[n[0]][n[1]] == -2 || (moves.containsKey(n) && iteration == 1)) {
                            //DEBUG: which neighbors found.
                            //System.err.println("Player " + o + " ALGSTART NEIGHBOR: " + n[0] + ", " + n[1]);
                            full = false;
                            graphset[n[0]][n[1]] = o;
                            moves.put(n, o);

                            neighborcount++;
                        }
                    }
                    //DEBUG: total neighbors found 
                    //System.err.println("NEIGHBORCOUNT " + o + ": " + neighborcount);
                }
                }
            }
            
            for (int[] k : moves.keySet()) {
                if (graphs.get(moves.get(k)) == null) {
                    graphs.put(moves.get(k), new HashMap<int[], Integer>());
                }
                graphs.get(moves.get(k)).put(k, iteration);
            }
            
            

            
            if (full) break;
            
            // CLEAR ALGSTARTS TO TRY AGAIN
            for (int k : algStarts.keySet()) {
                algStarts.put(k, new ArrayList<int[]>());
            }
            
            for (int[] k : moves.keySet()) {
                algStarts.get(moves.get(k)).add(k);
            }
            iteration += 1;
            
            //DEBUG: CHECK IF ALGSTARTS AND NEIGHBORS MATCH UP
            /*
            System.err.println("NEW ALGSTARTS: ");
            for (int k : algStarts.keySet()) {
                for (int[] x : algStarts.get(k)) {
                    if (k == 0) {
                    System.err.println("PLAYER " + k + " ATTEMPTS START AT " + x[0] + ", " + x[1]);
                    }
                }
            }
            
            
            for (int k : graphs.keySet()) {
                if (k == 0) {
                System.err.println("GRAPH SIZE " + k + ": " + graphs.get(k).size());
                }
            } */
            
            
        }
        
        // DEBUG: Check possible scores
        /*        
            for (int k : graphs.keySet()) {
                for (int[] j : graphs.get(k).keySet()) {
                    System.err.println("BOT " + k + " tries " + j[0] + ", " + j[1] + " at iteration " + iteration);
                }
                System.err.println("BOT " + k + " HAS " + graphs.get(k).size() + " POSSIBLE MOVES WITH " + iteration + " ITERATIONS ");
            } */
        if (graphs.get(myid) != null) {
            return graphs.get(myid).size();
        } else {
            return 0;
        }
    }

// main method
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        setUp();
        // game loop
        while (true) {
            int N = in.nextInt(); // total number of players (2 to 4).
            int P = in.nextInt(); // your player number (0 to 3).
            int[][] currentMoves = new int[2][N]; // the moves being made on this turn
            HashMap<Integer, int[]> currMoves = new HashMap<Integer, int[]>();
            for (int i = 0; i < N; i++) {
                int X0 = in.nextInt(); // starting X coordinate of lightcycle (or -1)
                int Y0 = in.nextInt(); // starting Y coordinate of lightcycle (or -1)
                int X1 = in.nextInt(); // starting X coordinate of lightcycle (can be the same as X0 if you play before this player)
                int Y1 = in.nextInt(); // starting Y coordinate of lightcycle (can be the same as Y0 if you play before this player)
                
                // eliminate dead players from consideration
                // this sets their squares to the empty value if their starting coordinates = -1, indicating death
                if (X0 == -1 && Y1 == -1) {
                    System.err.println("PLAYER " + i + " HAS DIED");
                    for (int j = 0; j < 20; j++) {
                        for (int k = 0; k < 30; k++) {
                            if (boardState[j][k] == i) {
                                boardState[j][k] = -2;
                            }
                        }
                    }
                // if everything is okay we just add it onto the board and to our currentMoves list
                } else {
                    boardState[Y0][X0] = i;
                    boardState[Y1][X1] = i;
                    int[] add = new int[]{Y1, X1};
                    currMoves.put(i, add);
                    currentMoves[0][i] = X1;
                    currentMoves[1][i] = Y1;     
                }

            }
            //DEBUG: print boardstate and check it works
            //printBoard();
             HashMap<Integer, int[]> scores = new HashMap<Integer, int[]>(); // store our possible scores!
             
             // here it's time to call our minimaxer.
             int[] me = new int[2];
            for (int p = 0; p < N; p++) {
                if (p == P) {
                    
                    //System.err.println("Us!");
                    int yy = currMoves.get(p)[0];
                    int xx = currMoves.get(p)[1];
                    
                    
                    me[0] = yy;
                    me[1] = xx;
                    System.err.println("WHERE I'M AT: (" + me[0] + ", " + me[1] + ")");
                   
                    
                    // here we look through neighbors of our current position (me!)
                    for (int[] n : allNeighbors[yy][xx]) {
                       
                       // if this neighbor isn't empty we add it to our hashmap of possible starting positions
                        if (boardState[n[0]][n[1]] == -2) {
                         //System.err.println("POSSIBLE NEIGHBOR : (" +  n[0] + ", " + n[1] + ")");
                         HashMap<Integer, int[]> playerStarts = new HashMap<Integer, int[]>();
                         for (int k : currMoves.keySet()) {
                            playerStarts.put(k, currMoves.get(k));
                         }
                         playerStarts.put(P, n);
                         
                         // 
                         for (int key : currMoves.keySet()) {
                             //DEBUG: Possible moves.
                             //System.err.println("UP NEXT " + key + ": (" + playerStarts.get(key)[0] + ", " + playerStarts.get(key)[1] + ")");
                             if (currMoves.get(key)[0] == -1 && currMoves.get(key)[1] == -1) {
                                System.err.println("Dead cycle found!");
                                playerStarts.put(key, new int[2]);
                            }
                         }
                         // get all possible scores!
                         int score = getScore(playerStarts, N, P);
                         System.err.println("NEIGHBOR " + n[0] + ", " + n[1] + " PRODUCES SCORE " + score);
                         scores.put(score, n);
                         
                        }
                        
                        
                    }
                    
                }
            }
            
            // if we're not immediately about to die, let's make the move that leads to the best score
            if (!scores.keySet().isEmpty()) {
                int bestscore = Collections.max(scores.keySet());
                System.err.println("BEST SCORE: " + bestscore);
                System.out.println(direction(me, scores.get(bestscore)));
            } else {
                System.out.println("LEFT");
            }
            //System.out.println("LEFT"); // A single line with UP, DOWN, LEFT or RIGHT
        }
    }
    
    
}
