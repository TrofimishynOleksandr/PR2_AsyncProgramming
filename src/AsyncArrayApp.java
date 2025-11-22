import java.util.Random;
import java.util.Scanner;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AsyncArrayApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = readArraySize(scanner);
        int multiplier = readMultiplier(scanner);

        int[] array = generateRandomIntArray(n, -100, 100);

        System.out.println("\nПочатковий масив:");
        printIntArray(array);

        // Створюємо обробник масивів з фіксованим пулом потоків
        int threadCount = Math.min(Runtime.getRuntime().availableProcessors(), n);
        AsyncArrayProcessor processor = new AsyncArrayProcessor(threadCount);

        long startTime = System.nanoTime();

        // Асинхронна обробка масиву (множення на multiplier)
        CopyOnWriteArrayList<Integer> result = processor.multiplyArray(array, multiplier);

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        processor.shutdown();

        System.out.println("\nРезультуючий масив (після множення):");
        printList(result);

        System.out.println("\nЧас роботи програми: " + durationMs + " ms");
    }

    private static int readArraySize(Scanner scanner) {
        int n;
        while (true) {
            System.out.print("Введіть кількість елементів масиву (від 40 до 60): ");
            n = scanner.nextInt();
            if (n >= 40 && n <= 60) {
                return n;
            }
            System.out.println("Некоректне значення! Спробуйте ще раз.");
        }
    }

    private static int readMultiplier(Scanner scanner) {
        System.out.print("Введіть множник (ціле число): ");
        return scanner.nextInt();
    }

    private static int[] generateRandomIntArray(int size, int min, int max) {
        int[] arr = new int[size];
        Random random = new Random();

        int bound = max - min + 1;
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(bound) + min;
        }
        return arr;
    }

    private static void printIntArray(int[] arr) {
        for (int value : arr) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    private static void printList(List<Integer> list) {
        for (Integer value : list) {
            System.out.print(value + " ");
        }
        System.out.println();
    }
}
