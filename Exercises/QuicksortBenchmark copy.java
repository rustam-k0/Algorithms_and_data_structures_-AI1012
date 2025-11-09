import java.util.*;

/**
 * QuickSortComparison.java
 * Упрощённые реализации всех вариантов QuickSort и Median.
 * Комментарии объясняют базовые идеи без лишней теории.
 */
public class QuickSortComparison {

    /* ==================== 3.1 CLASSIC QUICK SORT ==================== */
    // Быстрая сортировка: делим массив относительно "опорного" (pivot) элемента.
    public static void quickSort(int[] arr, int left, int right) {
        int i = left, j = right;
        int pivot = arr[(left + right) / 2]; // средний элемент — pivot
        while (i <= j) {
            // Сдвигаем левый индекс пока элемент меньше pivot
            while (arr[i] < pivot) i++;
            // Сдвигаем правый индекс пока элемент больше pivot
            while (arr[j] > pivot) j--;
            // Меняем элементы местами, если нарушен порядок
            if (i <= j) {
                int tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++; j--;
            }
        }
        // Рекурсивно сортируем левую и правую часть
        if (left < j) quickSort(arr, left, j);
        if (i < right) quickSort(arr, i, right);
    }

    /* ==================== 3.2 RANDOMIZED QUICK SORT ==================== */
    // Выбираем pivot случайно, чтобы избежать худшего случая (уже отсортированный массив).
    public static void randomizedQuickSort(int[] arr, int left, int right) {
        if (left >= right) return;
        // Выбираем случайный pivot и ставим его в середину
        int pivotIndex = left + new Random().nextInt(right - left + 1);
        int pivot = arr[pivotIndex];
        int i = left, j = right;
        while (i <= j) {
            while (arr[i] < pivot) i++;
            while (arr[j] > pivot) j--;
            if (i <= j) {
                int tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++; j--;
            }
        }
        if (left < j) randomizedQuickSort(arr, left, j);
        if (i < right) randomizedQuickSort(arr, i, right);
    }

    /* ==================== 3.3 LINEAR-TIME MEDIAN ==================== */
    // Реализация "median of medians" (O(n)).
    // Идея: делим массив на группы по 5, находим медианы групп,
    // затем рекурсивно берём медиану медиан.
    public static int linearMedian(int[] arr) {
        return select(arr, arr.length / 2);
    }

    private static int select(int[] arr, int k) {
        if (arr.length <= 5) {
            Arrays.sort(arr);
            return arr[k];
        }

        // Разбиваем на группы по 5
        List<int[]> groups = new ArrayList<>();
        for (int i = 0; i < arr.length; i += 5) {
            int end = Math.min(i + 5, arr.length);
            int[] group = Arrays.copyOfRange(arr, i, end);
            Arrays.sort(group);
            groups.add(group);
        }

        // Берём медианы групп
        int[] medians = new int[groups.size()];
        for (int i = 0; i < groups.size(); i++) {
            int[] g = groups.get(i);
            medians[i] = g[g.length / 2];
        }

        // Медиана медиан
        int medianOfMedians = select(medians, medians.length / 2);

        // Разделяем массив на <, =, > medianOfMedians
        List<Integer> less = new ArrayList<>();
        List<Integer> equal = new ArrayList<>();
        List<Integer> greater = new ArrayList<>();
        for (int x : arr) {
            if (x < medianOfMedians) less.add(x);
            else if (x == medianOfMedians) equal.add(x);
            else greater.add(x);
        }

        if (k < less.size())
            return select(less.stream().mapToInt(Integer::intValue).toArray(), k);
        else if (k < less.size() + equal.size())
            return medianOfMedians;
        else
            return select(greater.stream().mapToInt(Integer::intValue).toArray(),
                    k - less.size() - equal.size());
    }

    /* ==================== 3.4 QUICK SORT WITH MEDIAN ==================== */
    // Используем медиану как pivot для оптимального разбиения.
    public static void quickSortWithMedian(int[] arr, int left, int right) {
        if (left >= right) return;

        // pivot = медиана массива (реальная или приближённая)
        int[] slice = Arrays.copyOfRange(arr, left, right + 1);
        int pivot = linearMedian(slice);

        int i = left, j = right;
        while (i <= j) {
            while (arr[i] < pivot) i++;
            while (arr[j] > pivot) j--;
            if (i <= j) {
                int tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++; j--;
            }
        }
        if (left < j) quickSortWithMedian(arr, left, j);
        if (i < right) quickSortWithMedian(arr, i, right);
    }

    /* ==================== BENCHMARK ==================== */
    public static void main(String[] args) {
        int n = 10000;
        int[] data = new Random().ints(n, 0, n).toArray();

        // Каждую копию сортируем отдельно, чтобы не влияли друг на друга
        benchmark("Classic QuickSort", data.clone(), arr -> quickSort(arr, 0, arr.length - 1));
        benchmark("Randomized QuickSort", data.clone(), arr -> randomizedQuickSort(arr, 0, arr.length - 1));
        benchmark("QuickSort with Median", data.clone(), arr -> quickSortWithMedian(arr, 0, arr.length - 1));
    }

    private static void benchmark(String name, int[] arr, java.util.function.Consumer<int[]> sorter) {
        long start = System.nanoTime();
        sorter.accept(arr);
        long end = System.nanoTime();
        System.out.printf("%s: %.3f ms%n", name, (end - start) / 1e6);
    }
}
