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
 * Object oriented representation of graph is using OOP approach to store nodes
 * and edges
 *
 * TODO: Please fill the body of methods in this class
 */
public class ObjectOriented implements Representation {
    private Collection<Node> nodes;
    private Collection<Edge> edges;
    int numNodes;

    public ObjectOriented(File file) {
        try {

            Scanner input = new Scanner(file);
            String currline;
            String[] nums;
            int currval, currto, currfrom = 0;
            Edge currEdge;
            numNodes = Integer.parseInt(input.nextLine());
            nodes = new ArrayList<Node>();
            edges = new ArrayList<Edge>();

            for (int i = 0; i < numNodes; i++) {
                nodes.add(new Node(i));
            }


            while (input.hasNextLine()) {
                currline = input.nextLine();
                nums = currline.split(":");
                currfrom = Integer.parseInt(nums[0]);
                currto = Integer.parseInt(nums[1]);
                currval = Integer.parseInt(nums[2]);
                edges.add(new Edge(new Node(currfrom), new Node(currto), currval));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ObjectOriented() {

    }

    @Override
    public boolean adjacent(Node x, Node y) {
        boolean adj = false;
        if (!nodes.contains(x) || !nodes.contains(y)) {
            adj = false;
        } else {
            for (Edge e : edges) {
                if ((e.getTo().equals(x) && e.getFrom().equals(y)) || (e.getTo().equals(y) && e.getFrom().equals(x))) {
                    adj = true;
                }
            }
        }
        return adj;
    }

    @Override
    public List<Node> neighbors(Node x) {
        ArrayList<Node> results = new ArrayList<Node>();
        for (Edge e : edges) {
            Node t = e.getTo();
            if (e.getFrom().equals(x)) {
                results.add(t);
            }
        }
        return results;
    }

    @Override
    public boolean addNode(Node x) {
        if (!nodes.contains(x)) {
            nodes.add(x);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeNode(Node x) {
        boolean removable = true;
        ArrayList<Edge> toRemove = new ArrayList<Edge>();
        if (nodes.contains(x)) {
            for (Edge e : edges) {
                if (e.getFrom().equals(x) || e.getTo().equals(x)) {
                    toRemove.add(e);
                }
            }
        } else {
            removable = false;
        }
        if (removable) {
            edges.removeAll(toRemove);
            nodes.remove(x);
        }
        return removable;
    }

    @Override
    public boolean addEdge(Edge x) {
        Node t = x.getTo();
        Node f = x.getFrom();
        if (!nodes.contains(t) || !nodes.contains(f)) {
            return false;
        } else if (edges.contains(x)) {
            return false;
        } else {
            edges.add(x);
            return true;
        }
    }

    @Override
    public boolean removeEdge(Edge x) {
        Node t = x.getTo();
        Node f = x.getFrom();
        if (!nodes.contains(t) || !nodes.contains(f)) {
            return false;
        } else if (!edges.contains(x)) {
            return false;
        } else {
            edges.remove(x);
            return true;
        }
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
