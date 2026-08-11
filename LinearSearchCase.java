import java.util.Scanner;
import java.util.Random;

public class LinearSearchCase {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        // Take array size
        System.out.print("Enter array size: ");
        int n = sc.nextInt();

        int[] arr = new int[n];

        // Generate random values
        System.out.println("Array elements:");

        for (int i = 0; i < n; i++) {
            arr[i] = random.nextInt(100);
            System.out.print(arr[i] + " ");
        }

        // Take search value
        System.out.print("\n\nEnter value to search: ");
        int key = sc.nextInt();

        // Linear Search
        int comparisons = 0;
        int position = -1;

        for (int i = 0; i < n; i++) {

            comparisons++;

            if (arr[i] == key) {
                position = i;
                break;
            }
        }

        // Result
        if (position != -1) {
            System.out.println("Element found at index: " + position);
        } else {
            System.out.println("Element not found");
        }

        System.out.println("Number of comparisons: " + comparisons);

        // Find case
        if (comparisons == 1) {
            System.out.println("Best Case");
            System.out.println("Time Complexity: O(1)");

        } else if (comparisons == n) {
            System.out.println("Worst Case");
            System.out.println("Time Complexity: O(n)");

        } else {
            System.out.println("Average Case");
            System.out.println("Time Complexity: O(n)");
        }

        sc.close();
    }
}