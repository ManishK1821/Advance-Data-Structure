import java.io.*;
import java.nio.file.*;
import java.util.*;

public class RedBlackTreeAssignment{

    static class RedBlackTree {
        private static final boolean RED = true;
        private static final boolean BLACK = false;

        class Node {
            int key;
            boolean color;
            Node left, right, parent;

            Node(int key, boolean color) {
                this.key = key;
                this.color = color;
                this.left = NIL;
                this.right = NIL;
                this.parent = NIL;
            }
        }

        private final Node NIL = new Node(Integer.MIN_VALUE, BLACK);
        private Node root = NIL;

        RedBlackTree() {
            NIL.left = NIL;
            NIL.right = NIL;
            NIL.parent = NIL;
        }

        @SuppressWarnings ("unused")
        private boolean isRed(Node n) {
            return n != NIL && n.color == RED;
        }

        @SuppressWarnings ("unused")
        private boolean isBlack(Node n) {
            return n == NIL || n.color == BLACK;
        }

        public boolean search(int key) {
            return searchNode(key) != NIL;
        }

        private Node searchNode(int key) {
            Node current = root;
            while (current != NIL) {
                if (key == current.key) return current;
                current = key < current.key ? current.left : current.right;
            }
            return NIL;
        }

        public boolean insert(int key) {
            Node parent = NIL;
            Node current = root;

            while (current != NIL) {
                parent = current;
                if (key == current.key) return false;
                current = key < current.key ? current.left : current.right;
            }

            Node z = new Node(key, RED);
            z.parent = parent;

            if (parent == NIL) {
                root = z;
            } else if (key < parent.key) {
                parent.left = z;
            } else {
                parent.right = z;
            }

            insertFixup(z);
            return true;
        }

        private void insertFixup(Node z) {
            while (z.parent.color == RED) {
                if (z.parent == z.parent.parent.left) {
                    Node y = z.parent.parent.right;

                    if (y.color == RED) {
                        z.parent.color = BLACK;
                        y.color = BLACK;
                        z.parent.parent.color = RED;
                        z = z.parent.parent;
                    } else {
                        if (z == z.parent.right) {
                            z = z.parent;
                            leftRotate(z);
                        }
                        z.parent.color = BLACK;
                        z.parent.parent.color = RED;
                        rightRotate(z.parent.parent);
                    }
                } else {
                    Node y = z.parent.parent.left;

                    if (y.color == RED) {
                        z.parent.color = BLACK;
                        y.color = BLACK;
                        z.parent.parent.color = RED;
                        z = z.parent.parent;
                    } else {
                        if (z == z.parent.left) {
                            z = z.parent;
                            rightRotate(z);
                        }
                        z.parent.color = BLACK;
                        z.parent.parent.color = RED;
                        leftRotate(z.parent.parent);
                    }
                }

                if (z == root) break;
            }
            root.color = BLACK;
        }

        private void leftRotate(Node x) {
            Node y = x.right;
            x.right = y.left;

            if (y.left != NIL) y.left.parent = x;

            y.parent = x.parent;

            if (x.parent == NIL) {
                root = y;
            } else if (x == x.parent.left) {
                x.parent.left = y;
            } else {
                x.parent.right = y;
            }

            y.left = x;
            x.parent = y;
        }

        private void rightRotate(Node y) {
            Node x = y.left;
            y.left = x.right;

            if (x.right != NIL) x.right.parent = y;

            x.parent = y.parent;

            if (y.parent == NIL) {
                root = x;
            } else if (y == y.parent.right) {
                y.parent.right = x;
            } else {
                y.parent.left = x;
            }

            x.right = y;
            y.parent = x;
        }

        public boolean delete(int key) {
            Node z = searchNode(key);
            if (z == NIL) return false;

            Node y = z;
            boolean originalColor = y.color;
            Node x;

            if (z.left == NIL) {
                x = z.right;
                transplant(z, z.right);
            } else if (z.right == NIL) {
                x = z.left;
                transplant(z, z.left);
            } else {
                y = minimum(z.right);
                originalColor = y.color;
                x = y.right;

                if (y.parent == z) {
                    x.parent = y;
                } else {
                    transplant(y, y.right);
                    y.right = z.right;
                    y.right.parent = y;
                }

                transplant(z, y);
                y.left = z.left;
                y.left.parent = y;
                y.color = z.color;
            }

            if (originalColor == BLACK) deleteFixup(x);
            return true;
        }

        private void transplant(Node u, Node v) {
            if (u.parent == NIL) {
                root = v;
            } else if (u == u.parent.left) {
                u.parent.left = v;
            } else {
                u.parent.right = v;
            }
            v.parent = u.parent;
        }

        private Node minimum(Node node) {
            while (node.left != NIL) node = node.left;
            return node;
        }

        private void deleteFixup(Node x) {
            while (x != root && x.color == BLACK) {
                if (x == x.parent.left) {
                    Node w = x.parent.right;

                    if (w.color == RED) {
                        w.color = BLACK;
                        x.parent.color = RED;
                        leftRotate(x.parent);
                        w = x.parent.right;
                    }

                    if (w.left.color == BLACK && w.right.color == BLACK) {
                        w.color = RED;
                        x = x.parent;
                    } else {
                        if (w.right.color == BLACK) {
                            w.left.color = BLACK;
                            w.color = RED;
                            rightRotate(w);
                            w = x.parent.right;
                        }

                        w.color = x.parent.color;
                        x.parent.color = BLACK;
                        w.right.color = BLACK;
                        leftRotate(x.parent);
                        x = root;
                    }
                } else {
                    Node w = x.parent.left;

                    if (w.color == RED) {
                        w.color = BLACK;
                        x.parent.color = RED;
                        rightRotate(x.parent);
                        w = x.parent.left;
                    }

                    if (w.right.color == BLACK && w.left.color == BLACK) {
                        w.color = RED;
                        x = x.parent;
                    } else {
                        if (w.left.color == BLACK) {
                            w.right.color = BLACK;
                            w.color = RED;
                            leftRotate(w);
                            w = x.parent.left;
                        }

                        w.color = x.parent.color;
                        x.parent.color = BLACK;
                        w.left.color = BLACK;
                        rightRotate(x.parent);
                        x = root;
                    }
                }
            }
            x.color = BLACK;
        }

        public int height() {
            if (root == NIL) return 0;
            int height = 0;
            ArrayDeque<Node> queue = new ArrayDeque<>();
            queue.add(root);
            while (!queue.isEmpty()) {
                int levelSize = queue.size();
                height++;
                for (int i = 0; i < levelSize; i++) {
                    Node n = queue.remove();
                    if (n.left != NIL) queue.add(n.left);
                    if (n.right != NIL) queue.add(n.right);
                }
            }
            return height;
        }

        public void inorder(PrintWriter out) {
            inorder(root, out);
            out.println();
        }

        private void inorder(Node node, PrintWriter out) {
            if (node == NIL) return;
            inorder(node.left, out);
            out.print(node.key);
            out.print(' ');
            inorder(node.right, out);
        }

        public List<Integer> inorderList() {
            List<Integer> result = new ArrayList<>();
            inorderList(root, result);
            return result;
        }

        private void inorderList(Node node, List<Integer> result) {
            if (node == NIL) return;
            inorderList(node.left, result);
            result.add(node.key);
            inorderList(node.right, result);
        }

        public void validate() {
            if (root == NIL) return;
            if (root.color != BLACK) throw new IllegalStateException("Root is not black.");
            validateNode(root, null, null);
        }

        private int validateNode(Node node, Integer min, Integer max) {
            if (node == NIL) return 1;

            if (min != null && node.key <= min) throw new IllegalStateException("BST property violated.");
            if (max != null && node.key >= max) throw new IllegalStateException("BST property violated.");

            if (node.color == RED && (node.left.color == RED || node.right.color == RED)) {
                throw new IllegalStateException("Red node has red child.");
            }

            int leftBlackHeight = validateNode(node.left, min, node.key);
            int rightBlackHeight = validateNode(node.right, node.key, max);

            if (leftBlackHeight != rightBlackHeight) {
                throw new IllegalStateException("Black-height property violated.");
            }

            return leftBlackHeight + (node.color == BLACK ? 1 : 0);
        }
    }

    static class BinarySearchTree {
        static class Node {
            int key;
            Node left, right;
            Node(int key) { this.key = key; }
        }

        private Node root;

        public boolean insert(int key) {
            if (root == null) {
                root = new Node(key);
                return true;
            }

            Node current = root;
            while (true) {
                if (key == current.key) return false;
                if (key < current.key) {
                    if (current.left == null) {
                        current.left = new Node(key);
                        return true;
                    }
                    current = current.left;
                } else {
                    if (current.right == null) {
                        current.right = new Node(key);
                        return true;
                    }
                    current = current.right;
                }
            }
        }

        public boolean search(int key) {
            Node current = root;
            while (current != null) {
                if (key == current.key) return true;
                current = key < current.key ? current.left : current.right;
            }
            return false;
        }

        public boolean delete(int key) {
            Node parent = null;
            Node current = root;

            while (current != null && current.key != key) {
                parent = current;
                current = key < current.key ? current.left : current.right;
            }
            if (current == null) return false;

            if (current.left != null && current.right != null) {
                Node successorParent = current;
                Node successor = current.right;
                while (successor.left != null) {
                    successorParent = successor;
                    successor = successor.left;
                }
                current.key = successor.key;
                parent = successorParent;
                current = successor;
            }

            Node child = current.left != null ? current.left : current.right;

            if (parent == null) {
                root = child;
            } else if (parent.left == current) {
                parent.left = child;
            } else {
                parent.right = child;
            }
            return true;
        }

        public int height() {
            if (root == null) return 0;
            int height = 0;
            ArrayDeque<Node> queue = new ArrayDeque<>();
            queue.add(root);
            while (!queue.isEmpty()) {
                int levelSize = queue.size();
                height++;
                for (int i = 0; i < levelSize; i++) {
                    Node n = queue.remove();
                    if (n.left != null) queue.add(n.left);
                    if (n.right != null) queue.add(n.right);
                }
            }
            return height;
        }
    }

    static class BenchmarkRow {
        String scenario;
        int nodes;
        int rbHeight;
        int bstHeight;
        double rbInsertMs;
        double bstInsertMs;
        double rbDeleteUs;
        double bstDeleteUs;
        double rbSearchNs;
        double bstSearchNs;

        BenchmarkRow(String scenario, int nodes, int rbHeight, int bstHeight,
                     double rbInsertMs, double bstInsertMs,
                     double rbDeleteUs, double bstDeleteUs,
                     double rbSearchNs, double bstSearchNs) {
            this.scenario = scenario;
            this.nodes = nodes;
            this.rbHeight = rbHeight;
            this.bstHeight = bstHeight;
            this.rbInsertMs = rbInsertMs;
            this.bstInsertMs = bstInsertMs;
            this.rbDeleteUs = rbDeleteUs;
            this.bstDeleteUs = bstDeleteUs;
            this.rbSearchNs = rbSearchNs;
            this.bstSearchNs = bstSearchNs;
        }
    }

    private static final int[] SIZES = {100, 500, 1000, 5000, 10000, 50000};
    private static final int SEARCH_REPETITIONS = 2000;

    public static void main(String[] args) throws Exception {
        Path inputPath = Paths.get("input.txt");
        Path outputPath = Paths.get("output.txt");
        Path csvPath = Paths.get("benchmark.csv");

        List<Integer> input = readIntegers(inputPath);
        if (input.size() < 50000) {
            System.out.println("input.txt contains " + input.size() + " integers.");
            System.out.println("The assignment requires up to 50000 nodes.");
            System.out.println("Please put at least 50000 unique integers in input.txt and run again.");
            return;
        }

        int[] base = firstUnique(input, 50000);
        System.out.println("Loaded 50000 unique integers from input.txt.");

        // Main required Red-Black Tree operation on the supplied input.
        RedBlackTree mainTree = new RedBlackTree();
        for (int value : base) mainTree.insert(value);
        mainTree.validate();

        System.out.println("\nRed-Black Tree after insertion:");
        printInorderForConsole(mainTree, 100);

        int deleteKey = base[base.length / 2];
        System.out.println("Deleting specified test key: " + deleteKey);
        mainTree.delete(deleteKey);
        mainTree.validate();

        System.out.println("Red-Black Tree after deletion:");
        printInorderForConsole(mainTree, 100);

        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(outputPath))) {
            out.println("Red-Black Tree final in-order traversal after deleting key " + deleteKey + ":");
            mainTree.inorder(out);
        }
        System.out.println("Final in-order traversal written to output.txt");

        // Prepare three input orders required by the assignment.
        int[] randomData = base.clone();
        int[] sortedData = base.clone();
        Arrays.sort(sortedData);
        int[] reverseData = sortedData.clone();
        reverse(reverseData);

        List<BenchmarkRow> rows = new ArrayList<>();
        benchmarkScenario("Random", randomData, rows);
        benchmarkScenario("Sorted", sortedData, rows);
        benchmarkScenario("Reverse-Sorted", reverseData, rows);

        writeCsv(csvPath, rows);
        System.out.println("Benchmark CSV written to benchmark.csv");
        System.out.println("Benchmark completed.");
    }

    private static void benchmarkScenario(String scenario, int[] data, List<BenchmarkRow> rows) {
        System.out.println("\n===== " + scenario + " input =====");

        for (int n : SIZES) {
            int[] values = Arrays.copyOf(data, n);
            int searchKey = values[n / 2];
            int deleteKey = values[n / 2];

            // Warm-up JIT with one build of each tree.
            buildRB(values);
            buildBST(values);

            double rbInsertMs = averageRBInsertTime(values);
            double bstInsertMs = averageBSTInsertTime(values);

            RedBlackTree rbForHeight = buildRB(values);
            BinarySearchTree bstForHeight = buildBST(values);
            int rbHeight = rbForHeight.height();
            int bstHeight = bstForHeight.height();

            double rbDeleteUs = averageDeleteTimeRB(values, deleteKey);
            double bstDeleteUs = averageDeleteTimeBST(values, deleteKey);

            double rbSearchNs = averageSearchTimeRB(values, searchKey);
            double bstSearchNs = averageSearchTimeBST(values, searchKey);

            rows.add(new BenchmarkRow(
                    scenario, n, rbHeight, bstHeight,
                    rbInsertMs, bstInsertMs,
                    rbDeleteUs, bstDeleteUs,
                    rbSearchNs, bstSearchNs
            ));

            System.out.printf(
                    Locale.US,
                    "n=%d | RB height=%d | BST height=%d | RB insert=%.4f ms | BST insert=%.4f ms | RB delete=%.3f us | BST delete=%.3f us | RB search=%.2f ns | BST search=%.2f ns%n",
                    n, rbHeight, bstHeight, rbInsertMs, bstInsertMs,
                    rbDeleteUs, bstDeleteUs, rbSearchNs, bstSearchNs
            );
        }
    }

    private static double averageRBInsertTime(int[] values) {
        // One warm-up run followed by one measured run keeps the benchmark
        // practical even for a 50,000-node sorted BST.
        buildRB(values);
        long start = System.nanoTime();
        RedBlackTree tree = buildRB(values);
        long end = System.nanoTime();
        tree.validate();
        return (end - start) / 1_000_000.0;
    }

    private static double averageBSTInsertTime(int[] values) {
        buildBST(values);
        long start = System.nanoTime();
        BinarySearchTree tree = buildBST(values);
        long end = System.nanoTime();
        if (tree.height() < 0) throw new AssertionError();
        return (end - start) / 1_000_000.0;
    }

    private static double averageDeleteTimeRB(int[] values, int deleteKey) {
        RedBlackTree tree = buildRB(values);
        long start = System.nanoTime();
        boolean deleted = tree.delete(deleteKey);
        long end = System.nanoTime();
        if (!deleted) throw new AssertionError("RB delete failed");
        tree.validate();
        return (end - start) / 1_000.0;
    }

    private static double averageDeleteTimeBST(int[] values, int deleteKey) {
        BinarySearchTree tree = buildBST(values);
        long start = System.nanoTime();
        boolean deleted = tree.delete(deleteKey);
        long end = System.nanoTime();
        if (!deleted) throw new AssertionError("BST delete failed");
        return (end - start) / 1_000.0;
    }

    private static double averageSearchTimeRB(int[] values, int searchKey) {
        RedBlackTree tree = buildRB(values);
        long total = 0;
        int found = 0;
        for (int r = 0; r < SEARCH_REPETITIONS; r++) {
            int key = values[r % values.length];
            long start = System.nanoTime();
            boolean result = tree.search(key);
            long end = System.nanoTime();
            total += end - start;
            if (result) found++;
        }
        if (found != SEARCH_REPETITIONS) throw new AssertionError();
        return total / (double) SEARCH_REPETITIONS;
    }

    private static double averageSearchTimeBST(int[] values, int searchKey) {
        BinarySearchTree tree = buildBST(values);
        long total = 0;
        int found = 0;
        for (int r = 0; r < SEARCH_REPETITIONS; r++) {
            int key = values[r % values.length];
            long start = System.nanoTime();
            boolean result = tree.search(key);
            long end = System.nanoTime();
            total += end - start;
            if (result) found++;
        }
        if (found != SEARCH_REPETITIONS) throw new AssertionError();
        return total / (double) SEARCH_REPETITIONS;
    }

    private static RedBlackTree buildRB(int[] values) {
        RedBlackTree tree = new RedBlackTree();
        for (int value : values) tree.insert(value);
        return tree;
    }

    private static BinarySearchTree buildBST(int[] values) {
        BinarySearchTree tree = new BinarySearchTree();
        for (int value : values) tree.insert(value);
        return tree;
    }

    private static List<Integer> readIntegers(Path path) throws IOException {
        if (!Files.exists(path) || Files.size(path) == 0) {
            generateDefaultInputFile(path, 50000);
        }

        String content = Files.readString(path);
        String[] tokens = content.trim().split("\\s+");
        List<Integer> values = new ArrayList<>();
        for (String token : tokens) {
            if (!token.isBlank()) values.add(Integer.parseInt(token));
        }

        if (values.size() < 50000) {
            generateDefaultInputFile(path, 50000);
            return readIntegers(path);
        }

        return values;
    }

    private static void generateDefaultInputFile(Path path, int count) throws IOException {
        Set<Integer> values = new LinkedHashSet<>();
        Random random = new Random(42L);

        while (values.size() < count) {
            values.add(random.nextInt(1_000_000_000) + 1);
        }

        StringBuilder builder = new StringBuilder();
        for (int value : values) {
            if (builder.length() > 0) builder.append(' ');
            builder.append(value);
        }

        Files.writeString(path, builder.toString(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE);
    }

    private static int[] firstUnique(List<Integer> input, int count) {
        LinkedHashSet<Integer> set = new LinkedHashSet<>();
        for (int value : input) {
            set.add(value);
            if (set.size() == count) break;
        }
        if (set.size() < count) {
            throw new IllegalArgumentException("input.txt must contain at least " + count + " unique integers.");
        }
        int[] result = new int[count];
        int i = 0;
        for (int value : set) result[i++] = value;
        return result;
    }

    private static void reverse(int[] array) {
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    private static void printInorderForConsole(RedBlackTree tree, int maxToPrint) {
        List<Integer> values = tree.inorderList();
        if (values.size() <= maxToPrint) {
            System.out.println(values);
        } else {
            System.out.println("Tree contains " + values.size() + " nodes; full traversal is stored in output.txt.");
            System.out.println("First 50: " + values.subList(0, 50));
            System.out.println("Last 50: " + values.subList(values.size() - 50, values.size()));
        }
    }

    private static void writeCsv(Path path, List<BenchmarkRow> rows) throws IOException {
        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(path))) {
            out.println("Scenario,Nodes,RB_Height,BST_Height,RB_Insert_ms,BST_Insert_ms,RB_Delete_us,BST_Delete_us,RB_Search_ns,BST_Search_ns");
            for (BenchmarkRow row : rows) {
                out.printf(
                        Locale.US,
                        "%s,%d,%d,%d,%.6f,%.6f,%.6f,%.6f,%.6f,%.6f%n",
                        row.scenario, row.nodes, row.rbHeight, row.bstHeight,
                        row.rbInsertMs, row.bstInsertMs,
                        row.rbDeleteUs, row.bstDeleteUs,
                        row.rbSearchNs, row.bstSearchNs
                );
            }
        }
    }
}
