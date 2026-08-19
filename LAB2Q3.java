import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class Node {
    int key;
    Node left, right;

    Node(int key) {
        this.key = key;
    }
}

class BST {
    Node root;

    void insert(int key) {
        if (root == null) {
            root = new Node(key);
            return;
        }

        Node current = root;

        while (true) {
            if (key < current.key) {
                if (current.left == null) {
                    current.left = new Node(key);
                    return;
                }
                current = current.left;
            } else if (key > current.key) {
                if (current.right == null) {
                    current.right = new Node(key);
                    return;
                }
                current = current.right;
            } else {
                return;
            }
        }
    }

    boolean search(int key) {
        Node current = root;

        while (current != null) {
            if (key == current.key)
                return true;
            else if (key < current.key)
                current = current.left;
            else
                current = current.right;
        }

        return false;
    }

    void delete(int key) {
        root = deleteNode(root, key);
    }

    Node deleteNode(Node root, int key) {
        if (root == null)
            return null;

        if (key < root.key) {
            root.left = deleteNode(root.left, key);
        } else if (key > root.key) {
            root.right = deleteNode(root.right, key);
        } else {
            if (root.left == null)
                return root.right;

            if (root.right == null)
                return root.left;

            Node successor = root.right;

            while (successor.left != null)
                successor = successor.left;

            root.key = successor.key;
            root.right = deleteNode(root.right, successor.key);
        }

        return root;
    }

    int height() {
        return height(root);
    }

    int height(Node node) {
        if (node == null)
            return -1;

        return 1 + Math.max(
                height(node.left),
                height(node.right)
        );
    }
}

public class LAB2Q3 {

    static Random random = new Random();

    static void runExperiment(int n, String order) {

        Integer[] data = new Integer[n];

        for (int i = 0; i < n; i++)
            data[i] = i;

        if (order.equals("Random")) {
            List<Integer> list = Arrays.asList(data);
            Collections.shuffle(list);
        } else if (order.equals("Reverse-Sorted")) {
            for (int i = 0; i < n / 2; i++) {
                int temp = data[i];
                data[i] = data[n - 1 - i];
                data[n - 1 - i] = temp;
            }
        }

        BST bst = new BST();

        long start = System.nanoTime();

        for (int x : data)
            bst.insert(x);

        long buildTime = System.nanoTime() - start;

        int treeHeight = bst.height();

        Integer[] searchKeys = data.clone();
        Collections.shuffle(Arrays.asList(searchKeys));

        start = System.nanoTime();

        for (int i = 0; i < 1000; i++)
            bst.search(searchKeys[i % n]);

        long searchTime = System.nanoTime() - start;

        Integer[] deleteKeys = data.clone();
        Collections.shuffle(Arrays.asList(deleteKeys));

        start = System.nanoTime();

        for (int i = 0; i < 500; i++)
            bst.delete(deleteKeys[i % n]);

        long deleteTime = System.nanoTime() - start;

        System.out.printf(
            "%-8d %-18s %-15.6f %-10d %-18.6f %-15.6f%n",
            n,
            order,
            buildTime / 1_000_000.0,
            treeHeight,
            searchTime / 1_000_000.0,
            deleteTime / 1_000_000.0
        );
    }

    public static void main(String[] args) {

        int[] sizes = {1000, 5000, 10000};
        String[] orders = {
            "Random",
            "Sorted",
            "Reverse-Sorted"
        };

        System.out.println("BST PERFORMANCE RESULTS");
        System.out.println(
            "----------------------------------------------------------------------------------------"
        );

        System.out.printf(
            "%-8s %-18s %-15s %-10s %-18s %-15s%n",
            "n",
            "Input Order",
            "Build(ms)",
            "Height",
            "1000 Searches(ms)",
            "500 Deletions(ms)"
        );

        System.out.println(
            "----------------------------------------------------------------------------------------"
        );

        for (int n : sizes) {
            for (String order : orders) {
                runExperiment(n, order);
            }
        }
    }
}