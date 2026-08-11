import java.util.Random;
import java.util.Scanner;

public class binary {

    static int comparisons = 0;

    public static int binarySearch(int[] arr, int key) {
        int start = 0;
        int end = arr.length - 1;

        while (start <= end) {
            comparisons++;

            int mid = start + (end - start) / 2;

            if (arr[mid] == key) {
                return mid;
            } else if (arr[mid] < key) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        System.out.print("Enter array size: ");
        int n = sc.nextInt();

        if (n <= 0) {
            System.out.println("Array size must be greater than 0.");
            sc.close();
            return;
        }

        int[] arr = new int[n];

        // Generate sorted random array
        arr[0] = random.nextInt(10);

        for (int i = 1; i < n; i++) {
            arr[i] = arr[i - 1] + random.nextInt(10) + 1;
        }

        System.out.println("Generated Sorted Array:");

        // Prints array for any size, including 1000
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();

        System.out.print("Enter key: ");
        int key = sc.nextInt();

        long startTime = System.nanoTime();
        int result = binarySearch(arr, key);
        long endTime = System.nanoTime();

        System.out.println("\nResult:");

        if (result != -1) {
            System.out.println("Key found at index: " + result);
        } else {
            System.out.println("Key not found");
        }

        System.out.println("Comparisons: " + comparisons);
        System.out.println("Execution time: " + (endTime - startTime) + " ns");

        if (comparisons == 1) {
            System.out.println("Case: Best Case");
            System.out.println("Time Complexity: O(1)");
        } else if (result == -1) {
            System.out.println("Case: Worst Case");
            System.out.println("Time Complexity: O(log n)");
        } else {
            System.out.println("Case: Average Case");
            System.out.println("Time Complexity: O(log n)");
        }

        System.out.println("Space Complexity: O(1)");
        sc.close();
    }
}
