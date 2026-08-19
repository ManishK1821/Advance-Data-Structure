class BST {
    static class Node {
        int data;
        Node left, right;

        Node(int data) {
            this.data = data;
        }
    }

    Node root;

   
    Node insert(Node root, int data) {
        if (root == null)
            return new Node(data);

        if (data < root.data)
            root.left = insert(root.left, data);
        else
            root.right = insert(root.right, data);

        return root;
    }

    
    void inorder(Node root) {
        if (root != null) {
            inorder(root.left);
            System.out.print(root.data + " ");
            inorder(root.right);
        }
    }

   
    Node min(Node root) {
        while (root.left != null)
            root = root.left;
        return root;
    }

    
    Node delete(Node root, int key) {
        if (root == null) return null;

        if (key < root.data)
            root.left = delete(root.left, key);

        else if (key > root.data)
            root.right = delete(root.right, key);

        else {
            
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;

            
            Node temp = min(root.right);
            root.data = temp.data;
            root.right = delete(root.right, temp.data);
        }

        return root;
    }

    public static void main(String[] args) {

        int[] a = {50, 30, 70, 20, 40, 60, 80, 10};

       
        BST t = new BST();
        for (int x : a) t.root = t.insert(t.root, x);

        t.root = t.delete(t.root, 10);
        System.out.print("Delete leaf (10): ");
        t.inorder(t.root);

       
        t = new BST();
        for (int x : a) t.root = t.insert(t.root, x);

        t.root = t.delete(t.root, 20);
        System.out.print("\nDelete one child (20): ");
        t.inorder(t.root);

        
        t = new BST();
        for (int x : a) t.root = t.insert(t.root, x);

        t.root = t.delete(t.root, 70);
        System.out.print("\nDelete two children (70): ");
        t.inorder(t.root);
    }
}