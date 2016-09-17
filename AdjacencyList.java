package csula.cs4660.graphs.representations;

/**
 * Created by Clauds on 9/4/2016.
 */
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Adjacency list is probably the most common implementation to store the unknown
 * loose graph
 *
 * TODO: please implement the method body
 */
public class AdjacencyList implements Representation {
    private Map<Node, Collection<Edge>> adjacencyList;
    private Map<Node, List<Node>> neighbors;
    int numNodes;

    public AdjacencyList(File file) {
        try {
            Scanner input = new Scanner(file);
            String currline;
            String[] nums;
            int currval = 0;
            Node currto, currfrom;
            Edge currEdge;
            numNodes = Integer.parseInt(input.nextLine());

            adjacencyList = new HashMap<Node, Collection<Edge>>();
            neighbors = new HashMap<Node, List<Node>>();
            for (int i = 0; i < numNodes; i++) {
                adjacencyList.put(new Node(i), new ArrayList<Edge>());
                neighbors.put(new Node(i), new ArrayList<Node>());
            }



            while (input.hasNextLine()) {
                currline = input.nextLine();
                nums = currline.split(":");
                currfrom = new Node(Integer.parseInt(nums[0]));
                currto = new Node(Integer.parseInt(nums[1]));
                currval = Integer.parseInt(nums[2]);

                currEdge = new Edge(currfrom, currto, currval);

                adjacencyList.get(currfrom).add(currEdge);
                neighbors.get(currfrom).add(currto);
                adjacencyList.get(currto).add(currEdge);
            }

            //


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public AdjacencyList() {

    }

    @Override
    public boolean adjacent(Node x, Node y) {
        Collection<Edge> c = adjacencyList.get(x);
        Collection<Edge> c2 = adjacencyList.get(y);
        boolean adj = false;
        for (Edge e : c) {
            for (Edge e2 : c2) {
                if (e == e2) adj = true;
            }
        }
        return adj;
    }

    @Override
    public List<Node> neighbors(Node x) {
        return neighbors.get(x);
    }

    @Override
    public boolean addNode(Node x) {
        boolean addable = false;
        if (!adjacencyList.containsKey(x)) {
            adjacencyList.put(x, new ArrayList<Edge>());
            addable = true;
            numNodes++;
        }
        return addable;
    }

    @Override
    public boolean removeNode(Node x) {
        boolean cont = false;
        ArrayList<Edge> toRemove = new ArrayList<Edge>();
        ArrayList<Node> ntr = new ArrayList<Node>();
        if (adjacencyList.containsKey(x)) {
            cont = true;
            for (Collection<Edge> c : adjacencyList.values()) {
                for (Edge e : c) {
                    if (e.getFrom().equals(x) || e.getTo().equals(x)) {
                        toRemove.add(e);
                    }
                }
            }
        }
        for (Edge e : toRemove) {
            removeEdge(e);
        }
        adjacencyList.remove(x);
        return cont;
    }

    @Override
    public boolean addEdge(Edge x) {
        Collection<Collection<Edge>> el = adjacencyList.values();
        boolean found = true;
        boolean n = true;
        for (Collection<Edge> c : el) {
            if (c.contains(x)) {
                found = false;
            }
        }
        if (found) {
            Node f = x.getFrom();
            Node t = x.getTo();
            if (!adjacencyList.containsKey(f) || !adjacencyList.containsKey(t)) {
                return false;
            } else {
                adjacencyList.get(f).add(x);
                neighbors.get(f).add(t);
                adjacencyList.get(t).add(x);
            }
        }

        return found;
    }

    @Override
    public boolean removeEdge(Edge x) {
        Collection<Collection<Edge>> el = adjacencyList.values();
        boolean found = false;
        for (Collection<Edge> c : el) {
            if (c.contains(x)) {
                Node from = x.getFrom();
                Node to = x.getTo();

                adjacencyList.get(from).remove(x);
                adjacencyList.get(to).remove(x);
                neighbors.get(from).remove(to);
                found = true;
            }
        }

        return found;
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
