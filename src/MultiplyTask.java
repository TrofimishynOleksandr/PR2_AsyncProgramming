import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Задача, яка множить елементи масиву source в діапазоні [start; end)
 * на multiplier та записує результат у result з тим самим індексом.
 */
public class MultiplyTask implements Callable<Void> {

    private final int[] source;
    private final CopyOnWriteArrayList<Integer> result;
    private final int multiplier;
    private final int start;
    private final int end;

    public MultiplyTask(int[] source,
                        CopyOnWriteArrayList<Integer> result,
                        int multiplier,
                        int start,
                        int end) {
        this.source = source;
        this.result = result;
        this.multiplier = multiplier;
        this.start = start;
        this.end = end;
    }

    @Override
    public Void call() {
        for (int i = start; i < end; i++) {
            int newValue = source[i] * multiplier;
            result.set(i, newValue);
        }
        return null;
    }
}