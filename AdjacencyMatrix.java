package csula.cs4660.graphs.representations;

/**
 * Created by Clauds on 9/4/2016.
 */
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;
import groovy.json.internal.ArrayUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Adjacency matrix in a sense store the nodes in two dimensional array
 *
 * TODO: please fill the method body of this class
 */
public class AdjacencyMatrix implements Representation {
    private Node[] nodes;
    private int[][] adjacencyMatrix;
    int numNodes;

    public AdjacencyMatrix(File file) {
        try {
            Scanner input = new Scanner(file);
            String currline;
            String[] nums;
            int currval, currto, currfrom = 0;
            Edge currEdge;
            numNodes = Integer.parseInt(input.nextLine());

            nodes = new Node[numNodes];
            for (int i = 0; i < numNodes; i++) {
                nodes[i] = new Node(i);
            }
            adjacencyMatrix = new int[numNodes][numNodes];
            for (int i = 0; i < numNodes; i++) {
                for (int j = 0; j < numNodes; j++) {
                    adjacencyMatrix[i][j] = 0;
                }
            }



            while (input.hasNextLine()) {
                currline = input.nextLine();
                nums = currline.split(":");
                currfrom = Integer.parseInt(nums[0]);
                currto = Integer.parseInt(nums[1]);
                currval = Integer.parseInt(nums[2]);

                adjacencyMatrix[currfrom][currto] = currval;
            }

            //


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public AdjacencyMatrix() {

    }

    @Override
    public boolean adjacent(Node x, Node y) {
        //go through adjacency matrix and return true if the value at the index of node y is greater than 0
        //
        int xval = (int) x.getData();
        int yval = (int) y.getData();

        boolean adj = false;

        int xind = 0;
        int yind = 0;
        for (int i = 0; i < numNodes; i++) {
            if (nodes[i].equals(x)) {
                xind = i;
            }
            for (int j = 0; j < numNodes; j++) {
                if (nodes[j].equals(y)) {
                    yind = j;
                }
            }
        }

        if (adjacencyMatrix[xind][yind] != 0 || adjacencyMatrix[yind][xind] > 0) {
            adj = true;
        }
        return adj;
    }

    @Override
    public List<Node> neighbors(Node x) {
        // go through adjacency matrix and add all nodes represented by a 1 in node x's row to the result list
        int val = (int) x.getData();
        ArrayList<Node> results = new ArrayList<Node>();
        int yind = 0;
        for (int i = 0; i < numNodes; i++) {
            if (nodes[i].equals(x))  {
                yind = i;
            }
        }
        for (int j = 0; j < numNodes; j++) {
            if (adjacencyMatrix[yind][j] != 0) {
                results.add(nodes[j]);
            }
        }

        return results;
    }

    @Override
    public boolean addNode(Node x) {
        int[][] tempadj = new int[numNodes + 1][numNodes + 1];
        Node[] tempnodes;
        boolean addable = true;
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].equals(x)) addable = false;
        }
        if (addable) {
            tempnodes = Arrays.copyOf(nodes, nodes.length + 1);
            tempnodes[numNodes] = x;
            for (int i = 0; i < numNodes; i++) {
                tempadj[i] = Arrays.copyOf(adjacencyMatrix[i], numNodes + 1);
            }
            for (int j = 0; j < numNodes; j++) {
                tempadj[j][numNodes] = 0;
                tempadj[numNodes][j] = 0;
            }
            adjacencyMatrix = tempadj;
            nodes = tempnodes;
            numNodes++;
        }

        return addable;
    }

    @Override
    public boolean removeNode(Node x) {
        int val = (int) x.getData();
        int[][] tempadj = new int[numNodes - 1][numNodes - 1];

        Node[] tempnode = new Node[numNodes - 1];

        boolean removable = false;
        int ind = 0;

        if (Arrays.asList(nodes).contains(x)) {
            ind = Arrays.asList(nodes).indexOf(x);
            removable = true;
        }

        if (removable) {
            int p = 0;
            for (int i = 0; i < numNodes; i++) {
                if (i != ind) {
                    tempnode[p] = nodes[i];
                    System.arraycopy(adjacencyMatrix[i], 0, tempadj[p], 0, ind-1);
                    System.arraycopy(adjacencyMatrix[i], ind+1, tempadj[p], ind, adjacencyMatrix[i].length-1-ind);
                    p++;
                }
            }
            adjacencyMatrix = tempadj;
            nodes = tempnode;
            numNodes--;
        }
        return removable;
    }

    @Override
    public boolean addEdge(Edge x) {
        Node to = x.getTo();
        Node from = x.getFrom();
        int val = x.getValue();
        int xind = 0, yind = 0;
        boolean addable = true;
        for (int i = 0; i < numNodes; i++) {
            if (nodes[i].equals(to)) {
                xind = i;
            } else if (nodes[i].equals(from)) {
                yind = i;
            }
        }


        if (adjacencyMatrix[yind][xind] != 0) {
            addable = false;
        } else {
            adjacencyMatrix[yind][xind] = val;
        }

        return addable;
    }

    @Override
    public boolean removeEdge(Edge x) {
        Node to = x.getTo();
        Node from = x.getFrom();
        int xind = 0, yind = 0;
        for (int i = 0; i < numNodes; i++) {
            if (nodes[i].equals(to)) {
                xind = i;
            } else if (nodes[i].equals(from)) {
                yind = i;
            }
        }
        boolean removable = true;

        if (adjacencyMatrix[yind][xind] != 0) {
            adjacencyMatrix[yind][xind] = 0;
        } else {
            removable = false;
        }

        return removable;
    }

    @Override
    public int distance(Node from, Node to) {
        return 0;
    }

    @Override
    public Optional<Node> getNode(int index) {
        return null;
    }
}