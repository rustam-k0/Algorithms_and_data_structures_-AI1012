```java
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ======================================================================
// ЗАДАНИЕ 3.1 — QUICK SORT (СРЕДНИЙ ЭЛЕМЕНТ КАК PIVOT)
// ======================================================================
class QuickSortMitte {
    public static void sort(int[] a) {
        quicksort(a, 0, a.length - 1);
    }

    private static void quicksort(int[] a, int left, int right) {
        int i = left;
        int j = right;
        int middle = (left + right) / 2;
        int pivot = a[middle];

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

        if (left < j) quicksort(a, left, j);
        if (i < right) quicksort(a, i, right);
    }

    public static void runDemo() {
        System.out.println("--- Задание 3.1: QuickSort (Middle Pivot) ---");
        int[] arrayNormal = {92, 37, 61, 59, 42, 79, 28, 14, 84};
        System.out.println("До:   " + Arrays.toString(arrayNormal));
        sort(arrayNormal);
        System.out.println("После: " + Arrays.toString(arrayNormal));
        System.out.println();

        int[] arrayWorstCase = {1,2,3,4,5,6,7,8,9,10};
        System.out.println("До (Sorted): " + Arrays.toString(arrayWorstCase));
        sort(arrayWorstCase);
        System.out.println("После: " + Arrays.toString(arrayWorstCase));
        System.out.println();
    }
}


// ======================================================================
// ЗАДАНИЕ 3.2 — RANDOMIZED QUICK SORT (СЛУЧАЙНЫЙ ВЫБОР PIVOT)
// ======================================================================
class QuickSortRandom {
    private static Random randomGenerator = new Random();

    public static void sort(int[] a) {
        quicksort(a, 0, a.length - 1);
    }

    private static void quicksort(int[] a, int left, int right) {
        int i = left;
        int j = right;
        int randomIndex = left + randomGenerator.nextInt(right - left + 1);
        int pivot = a[randomIndex];

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

        if (left < j) quicksort(a, left, j);
        if (i < right) quicksort(a, i, right);
    }

    public static void runDemo() {
        System.out.println("--- Задание 3.2: QuickSort (Random Pivot) ---");
        int[] arrayNormal = {92, 37, 61, 59, 42, 79, 28, 14, 84};
        System.out.println("До:   " + Arrays.toString(arrayNormal));
        sort(arrayNormal);
        System.out.println("После: " + Arrays.toString(arrayNormal));
        System.out.println();
    }
}


// ======================================================================
// ЗАДАНИЕ 3.3 — LINEAR TIME MEDIAN (МЕДИАНА МЕДИАН)
// ======================================================================
class LinearTimeMedian {
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

        if (k < A.size()) return select(listToArray(A), k);
        else if (k < A.size() + B.size()) return m;
        else return select(listToArray(C), k - A.size() - B.size());
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

    public static void runDemo() {
        System.out.println("--- Задание 3.3: Linear Time Median (Select) ---");
        int[] array = new int[50];
        for (int i = 0; i < 50; i++) array[i] = i + 1;
        int medianK = (array.length - 1) / 2;
        int medianValue = select(array, medianK);
        System.out.println("Медиана: " + medianValue);
        System.out.println();
    }
}


// ======================================================================
// ЗАДАНИЕ 3.4 — QUICK SORT С ВЫБОРОМ PIVOT ЧЕРЕЗ МЕДИАНУ МЕДИАН
// ======================================================================
class QuickSortMedian {
    public static void sort(int[] a) {
        quicksort(a, 0, a.length - 1);
    }

    private static void quicksort(int[] a, int left, int right) {
        if (left >= right) return;
        int i = left;
        int j = right;
        int n = right - left + 1;
        int[] subarray = new int[n];
        System.arraycopy(a, left, subarray, 0, n);
        int medianK = (n - 1) / 2;
        int pivot = LinearTimeMedian.select(subarray, medianK);

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

        if (left < j) quicksort(a, left, j);
        if (i < right) quicksort(a, i, right);
    }

    public static void runDemo() {
        System.out.println("--- Задание 3.4: QuickSort (Median Pivot) ---");
        int[] arrayNormal = {92, 37, 61, 59, 42, 79, 28, 14, 84};
        System.out.println("До:   " + Arrays.toString(arrayNormal));
        sort(arrayNormal);
        System.out.println("После: " + Arrays.toString(arrayNormal));
        System.out.println();
    }
}


// ======================================================================
// ОСНОВНОЙ КЛАСС ДЛЯ ЗАПУСКА ВСЕХ ДЕМОНСТРАЦИЙ
// ======================================================================
public class Blatt03 {
    public static void main(String[] args) {
        QuickSortMitte.runDemo();
        QuickSortRandom.runDemo();
        LinearTimeMedian.runDemo();
        QuickSortMedian.runDemo();
    }
}
```
