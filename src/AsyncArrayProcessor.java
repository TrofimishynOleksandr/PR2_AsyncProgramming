import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsyncArrayProcessor {

    private final ExecutorService executor;
    private final int threadCount;

    public AsyncArrayProcessor(int threadCount) {
        this.threadCount = threadCount;
        this.executor = Executors.newFixedThreadPool(threadCount);
    }

    /**
     * Асинхронно множить усі елементи масиву на multiplier.
     * Масив розбивається на частини, кожна з яких обробляється окремим Callable.
     * Використовуються Future, isDone(), isCancelled().
     */
    public CopyOnWriteArrayList<Integer> multiplyArray(int[] source, int multiplier) {
        int n = source.length;

        CopyOnWriteArrayList<Integer> result =
                new CopyOnWriteArrayList<>(Collections.nCopies(n, 0));

        // Список Future для відслідковування задач
        List<Future<Void>> futures = new ArrayList<>();

        // Визначаємо розмір шматка
        int chunkSize = (int) Math.ceil((double) n / threadCount);

        // Створюємо та відправляємо задачі
        int taskIndex = 0;
        for (int start = 0; start < n; start += chunkSize) {
            int end = Math.min(start + chunkSize, n);

            MultiplyTask task = new MultiplyTask(source, result, multiplier, start, end);
            Future<Void> future = executor.submit(task);
            futures.add(future);

            System.out.printf("Створено задачу %d для індексів [%d; %d)%n", taskIndex, start, end);
            taskIndex++;
        }

        // Очікуємо завершення всіх задач, перевіряючи isDone() та isCancelled()
        for (int i = 0; i < futures.size(); i++) {
            Future<Void> future = futures.get(i);

            try {
                future.get(); // блокуємося, поки задача не завершиться
            } catch (ExecutionException e) {
                System.out.printf("Помилка під час виконання задачі %d: %s%n", i, e.getCause());
            } catch (InterruptedException e) {
                System.out.printf("Потік був перерваний під час очікування результату задачі %d%n", i);
                Thread.currentThread().interrupt();
            }
        }

        return result;
    }

    public void shutdown() {
        executor.shutdown();
    }
}