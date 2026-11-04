import java.util.Arrays;
import java.util.Scanner;

/**
 * Segment Tree program for range minimum and maximum queries.
 * The user enters the array and chooses operations from the console.
 */
public class Main {

    static class Result {
        final int min;
        final int max;

        Result(int min, int max) {
            this.min = min;
            this.max = max;
        }

        static Result merge(Result left, Result right) {
            return new Result(Math.min(left.min, right.min), Math.max(left.max, right.max));
        }
    }

    static class SegmentTree {
        private final int n;
        private final int[] minTree;
        private final int[] maxTree;
        private final boolean[] used;

        SegmentTree(int[] values) {
            n = values.length;
            minTree = new int[4 * n];
            maxTree = new int[4 * n];
            used = new boolean[4 * n];
            build(values, 1, 0, n - 1);
        }

        private void build(int[] values, int node, int start, int end) {
            used[node] = true;
            if (start == end) {
                minTree[node] = maxTree[node] = values[start];
                return;
            }

            int middle = start + (end - start) / 2;
            build(values, node * 2, start, middle);
            build(values, node * 2 + 1, middle + 1, end);
            pull(node);
        }

        private void pull(int node) {
            minTree[node] = Math.min(minTree[node * 2], minTree[node * 2 + 1]);
            maxTree[node] = Math.max(maxTree[node * 2], maxTree[node * 2 + 1]);
        }

        Result query(int left, int right) {
            validateRange(left, right);
            return query(1, 0, n - 1, left, right);
        }

        private Result query(int node, int start, int end, int left, int right) {
            if (left <= start && end <= right) {
                return new Result(minTree[node], maxTree[node]);
            }

            int middle = start + (end - start) / 2;
            if (right <= middle) {
                return query(node * 2, start, middle, left, right);
            }
            if (left > middle) {
                return query(node * 2 + 1, middle + 1, end, left, right);
            }

            return Result.merge(
                query(node * 2, start, middle, left, middle),
                query(node * 2 + 1, middle + 1, end, middle + 1, right)
            );
        }

        void update(int index, int value) {
            if (index < 0 || index >= n) {
                throw new IllegalArgumentException("Index must be between 0 and " + (n - 1) + ".");
            }
            update(1, 0, n - 1, index, value);
        }

        private void update(int node, int start, int end, int index, int value) {
            if (start == end) {
                minTree[node] = maxTree[node] = value;
                return;
            }

            int middle = start + (end - start) / 2;
            if (index <= middle) {
                update(node * 2, start, middle, index, value);
            } else {
                update(node * 2 + 1, middle + 1, end, index, value);
            }
            pull(node);
        }

        private void validateRange(int left, int right) {
            if (left < 0 || right >= n || left > right) {
                throw new IllegalArgumentException("Enter a valid range from 0 to " + (n - 1) + ".");
            }
        }

        String treeRepresentation() {
            StringBuilder result = new StringBuilder();
            for (int node = 1; node < 4 * n; node++) {
                if (used[node]) {
                    result.append("Node ").append(node)
                          .append(": min = ").append(minTree[node])
                          .append(", max = ").append(maxTree[node]).append('\n');
                }
            }
            return result.toString();
        }
    }

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.println("Segment Tree: Range Minimum and Maximum Query");

            int n = readPositiveInt(input, "Enter the number of elements: ");
            int[] values = new int[n];
            System.out.println("Enter " + n + " integer values:");
            for (int i = 0; i < n; i++) {
                values[i] = readInt(input, "Element [" + i + "]: ");
            }

            SegmentTree tree = new SegmentTree(values);
            System.out.println("\nOriginal array: " + Arrays.toString(values));
            System.out.println("\nSegment Tree representation:");
            System.out.print(tree.treeRepresentation());

            boolean running = true;
            while (running) {
                System.out.println("\n1. Query minimum and maximum in a range");
                System.out.println("2. Update an array element");
                System.out.println("3. Show current array and Segment Tree");
                System.out.println("4. Exit");
                int choice = readInt(input, "Choose an option: ");

                try {
                    switch (choice) {
                        case 1:
                            int left = readInt(input, "Enter left index (0 to " + (n - 1) + "): ");
                            int right = readInt(input, "Enter right index (0 to " + (n - 1) + "): ");
                            Result answer = tree.query(left, right);
                            System.out.println("Range [" + left + ", " + right + "]");
                            System.out.println("Minimum = " + answer.min);
                            System.out.println("Maximum = " + answer.max);
                            break;
                        case 2:
                            int index = readInt(input, "Enter index to update (0 to " + (n - 1) + "): ");
                            int newValue = readInt(input, "Enter new value: ");
                            tree.update(index, newValue);
                            values[index] = newValue;
                            System.out.println("Updated array: " + Arrays.toString(values));
                            break;
                        case 3:
                            System.out.println("Current array: " + Arrays.toString(values));
                            System.out.println("Segment Tree representation:");
                            System.out.print(tree.treeRepresentation());
                            break;
                        case 4:
                            running = false;
                            System.out.println("Program ended.");
                            break;
                        default:
                            System.out.println("Please enter a number from 1 to 4.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Input error: " + e.getMessage());
                }
            }
        }
    }

    private static int readPositiveInt(Scanner input, String prompt) {
        int value;
        do {
            value = readInt(input, prompt);
            if (value <= 0) {
                System.out.println("Please enter a positive number.");
            }
        } while (value <= 0);
        return value;
    }

    private static int readInt(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (input.hasNextInt()) {
                return input.nextInt();
            }
            System.out.println("Please enter a whole number.");
            input.next();
        }
    }
}
