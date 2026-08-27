// import java.io.*;
import java.util.*;

public class AVL {
    static class Node {
        int key;
        int height;
        Node left;
        Node right;
        Node(int key) {
            this.key = key;
            height = 1;
        }
    }
    static Node root = null;
    static int height(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }
    static int max(int a, int b) {
        if (a > b) {
            return a;
        }
        return b;
    }
    static int balance(Node node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }
    static Node rightRotate(Node y) {
        Node x = y.left;
        Node temp = x.right;
        x.right = y;
        y.left = temp;
        y.height = 1 + max(height(y.left),height(y.right));
        x.height = 1 + max(height(x.left),height(x.right));
        return x;
    }
    static Node leftRotate(Node x) {
        Node y = x.right;
        Node temp = y.left;
        y.left = x;
        x.right = temp;
        x.height = 1 + max(height(x.left),height(x.right));
        y.height = 1 + max( height(y.left), height(y.right));
        return y;
    }
    static Node insert(Node node, int key) {
        if (node == null) {
            return new Node(key);
        }
        if (key < node.key) {
            node.left = insert(node.left, key);
        }
        else if (key > node.key) {
            node.right = insert(node.right, key);
        }
        else {
            return node;
        }
        node.height = 1 + max(height(node.left),height(node.right));
        int b = balance(node);
        // LL
        if (b > 1 && key < node.left.key) {
            return rightRotate(node);
        }
        // RR
        if (b < -1 && key > node.right.key) {
            return leftRotate(node);
        }
        // LR
        if (b > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        // RL
        if (b < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }
    static Node minValue(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }
    static Node delete(Node node, int key) {
        if (node == null) {
            return null;
        }
        if (key < node.key) {
            node.left = delete(node.left, key);
        }
        else if (key > node.key) {
            node.right = delete(node.right, key);
        }
        else {
            // No child
            if (node.left == null &&
                    node.right == null) {
                return null;
            }
            // Only right child
            if (node.left == null) {
                return node.right;
            }
            // Only left child
            if (node.right == null) {
                return node.left;
            }
            // Two children
            Node temp = minValue(node.right);
            node.key = temp.key;
            node.right = delete(node.right, temp.key);
        }
        // Update height
        node.height = 1 + max(height(node.left),height(node.right));
        int b = balance(node);
        // LL
        if (b > 1 && balance(node.left) >= 0) {
             return rightRotate(node);
        }
        // LR
        if (b > 1 && balance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        // RR
        if (b < -1 && balance(node.right) <= 0) {
            return leftRotate(node);
        }
        // RL
        if (b < -1 && balance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }
    static void inorder(Node node) {
        if (node == null) {
            return;
        }
        inorder(node.left);
        System.out.print(node.key + " ");
        inorder(node.right);
    }
    static void display(Node node) {
        if (node == null) {
            return;
        }
        display(node.left);
        System.out.println("Node = " + node.key + " Height = " + node.height +" Balance = " + balance(node) );
        display(node.right);
    }
    public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter number of elements: ");
    int n = sc.nextInt();
    Node root = null;
    System.out.println("Enter elements:");
    for (int i = 0; i < n; i++) {
        int value = sc.nextInt();
        root = insert(root, value);
        System.out.println("After inserting " + value +" -> Height = " + height(root));
    }
    System.out.println("\nInorder Traversal:");
    inorder(root);
    System.out.println( "\nHeight = " + height(root));
    System.out.println("\nTree Details:");
    display(root);
    // DELETE
    System.out.print("\nEnter element to delete: ");
    int deleteValue = sc.nextInt();
    root = delete(root, deleteValue);
    System.out.println("\nAfter deletion of " + deleteValue
    );
    System.out.println("Inorder:");
    inorder(root);
    System.out.println( "\nHeight = " + height(root));
    System.out.println("\nTree Details:");
    display(root);
    sc.close();
}
}