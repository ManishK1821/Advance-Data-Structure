import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Segment Tree for Range Minimum Query (RMQ) and Range Maximum Query.
 *
 * Input format (input.txt):
 *   n
 *   a0 a1 ... a(n-1)
 *   q
 *   Q left right       (range query, inclusive)
 *   U index newValue   (point update)
 *
 * Example:
 *   8
 *   12 5 18 3 9 21 7 14
 *   5
 *   Q 0 3
 *   Q 2 6
 *   U 3 25
 *   Q 0 7
 *   Q 4 7
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
            if (values == null || values.length == 0) {
                throw new IllegalArgumentException("The array must contain at least one element.");
            }
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

            Result first = query(node * 2, start, middle, left, middle);
            Result second = query(node * 2 + 1, middle + 1, end, middle + 1, right);
            return Result.merge(first, second);
        }

        void update(int index, int value) {
            if (index < 0 || index >= n) {
                throw new IllegalArgumentException("Update index is outside the array: " + index);
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
                throw new IllegalArgumentException("Invalid query range: [" + left + ", " + right + "]");
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
        Path inputFile = Path.of(args.length > 0 ? args[0] : "input.txt");
        Path outputFile = Path.of(args.length > 1 ? args[1] : "output.txt");

        try (Scanner input = new Scanner(Files.newBufferedReader(inputFile));
             BufferedWriter output = Files.newBufferedWriter(outputFile)) {

            int n = input.nextInt();
            int[] values = new int[n];
            for (int i = 0; i < n; i++) {
                values[i] = input.nextInt();
            }

            SegmentTree tree = new SegmentTree(values);
            write(output, "Original array: " + Arrays.toString(values));
            write(output, "\nSegment Tree representation:");
            output.write(tree.treeRepresentation());

            int operations = input.nextInt();
            for (int i = 0; i < operations; i++) {
                String command = input.next().toUpperCase();
                if (command.equals("Q")) {
                    int left = input.nextInt();
                    int right = input.nextInt();
                    Result answer = tree.query(left, right);
                    write(output, "Query [" + left + ", " + right + "]");
                    write(output, "Minimum = " + answer.min);
                    write(output, "Maximum = " + answer.max);
                } else if (command.equals("U")) {
                    int index = input.nextInt();
                    int newValue = input.nextInt();
                    tree.update(index, newValue);
                    values[index] = newValue;
                    write(output, "Update: Index = " + index + ", New Value = " + newValue);
                    write(output, "Array after update: " + Arrays.toString(values));
                } else {
                    throw new IllegalArgumentException("Unknown operation '" + command + "'. Use Q or U.");
                }
            }

            write(output, "\nComplexities: build O(n), query O(log n), update O(log n), space O(n).");
            System.out.println("Results written to " + outputFile.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Input error: " + e.getMessage());
        }
    }

    private static void write(BufferedWriter output, String text) throws IOException {
        output.write(text);
        output.newLine();
    }
}
