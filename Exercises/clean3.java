import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Вспомогательный класс, содержащий общий метод разделения (partition),
 * который используется всеми вариантами QuickSort.
 */
class PartitionHelper {

    /**
     * Выполняет этап разделения (Partition)
     * @return Массив из 2-х элементов: {новый_индекс_i, новый_индекс_j}
     */
    public static int[] partition(int[] a, int left, int right, int pivot) {
        int i = left;
        int j = right;

        do {
            while (a[i] < pivot) i++;
            while (a[j] > pivot) j--;
            if (i <= j) {
                int tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
                i++;
                j--;
            }
        } while (i <= j);

        return new int[]{i, j};
    }
}

/*
 * Задание 3.1: Реализация QuickSort по варианту из лекции (слайд 36).
 * В качестве опорного элемента (Pivot) выбирается средний элемент.
 */
public class QuickSortMitte {

    public static void sort(int[] a) {
        quicksort(a, 0, a.length - 1);
    }

    private static void quicksort(int[] a, int left, int right) {
        // 1. Выбор опорного элемента (уникально для этого класса)
        int middle = (left + right) / 2;
        int pivot = a[middle];

        // 2. Вызов общего метода разделения
        int[] indices = PartitionHelper.partition(a, left, right, pivot);
        int i = indices[0];
        int j = indices[1];

        // 3. Рекурсивные вызовы (логика общая)
        if (left < j) {
            quicksort(a, left, j);
        }
        if (i < right) {
            quicksort(a, i, right);
        }
    }
}

/*
 * Задание 3.2: QuickSort со случайным выбором Pivot.
 * Это изменение помогает избежать худшего случая O(n^2).
 */
public class QuickSortRandom {

    private static Random randomGenerator = new Random();

    public static void sort(int[] a) {
        quicksort(a, 0, a.length - 1);
    }

    private static void quicksort(int[] a, int left, int right) {
        // 1. Выбор опорного элемента (уникально для этого класса)
        int randomIndex = left + randomGenerator.nextInt(right - left + 1);
        int pivot = a[randomIndex];

        // 2. Вызов общего метода разделения
        int[] indices = PartitionHelper.partition(a, left, right, pivot);
        int i = indices[0];
        int j = indices[1];

        // 3. Рекурсивные вызовы (логика общая)
        if (left < j) quicksort(a, left, j);
        if (i < right) quicksort(a, i, right);
    }
}

/*
 * Задание 3.3: Алгоритм "Медиана Медиан" (Select).
 * Находит k-й по величине элемент в массиве за линейное время O(n).
 * Реализация основана на псевдокоде (слайд 43).
 */
public class LinearTimeMedian {

    public static int select(int[] S, int k) {
        int n = S.length;

        if (n < 50) {
            Arrays.sort(S);
            return S[k];
        }

        int numMedians = (int) Math.ceil(n / 5.0);
        int[] medians = new int[numMedians];

        for (int i = 0; i < numMedians; i++) {
            int groupStart = i * 5;
            int groupEnd = Math.min(groupStart + 5, n);
            medians[i] = findMedianOfGroup(S, groupStart, groupEnd);
        }

        int m = select(medians, (numMedians - 1) / 2);

        List<Integer> A = new ArrayList<>();
        List<Integer> B = new ArrayList<>();
        List<Integer> C = new ArrayList<>();

        for (int x : S) {
            if (x < m) A.add(x);
            else if (x == m) B.add(x);
            else C.add(x);
        }

        if (k < A.size()) {
            return select(listToArray(A), k);
        } else if (k < A.size() + B.size()) {
            return m;
        } else {
            return select(listToArray(C), k - A.size() - B.size());
        }
    }

    private static int findMedianOfGroup(int[] S, int start, int end) {
        int[] group = Arrays.copyOfRange(S, start, end);
        Arrays.sort(group);
        return group[group.length / 2];
    }

    private static int[] listToArray(List<Integer> list) {
        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }
}

/*
 * Задание 3.4: QuickSort с выбором Pivot'а через "Медиану Медиан".
 * Это гарантирует время O(n log n),
 * т.к. pivot всегда является идеальным.
 */
public class QuickSortMedian {

    public static void sort(int[] a) {
        quicksort(a, 0, a.length - 1);
    }

    private static void quicksort(int[] a, int left, int right) {
        if (left >= right) {
            return;
        }

        // 1. Выбор опорного элемента (уникально для этого класса)
        int n = right - left + 1;
        int[] subarray = new int[n];
        System.arraycopy(a, left, subarray, 0, n);

        int medianK = (n - 1) / 2;
        int pivot = LinearTimeMedian.select(subarray, medianK);

        // 2. Вызов общего метода разделения
        int[] indices = PartitionHelper.partition(a, left, right, pivot);
        int i = indices[0];
        int j = indices[1];

        // 3. Рекурсивные вызовы (логика общая)
        if (left < j) quicksort(a, left, j);
        if (i < right) quicksort(a, i, right);
    }
}