import java.util.*;

public class Blatt03 {

  // ------------------------- //
    // Aufgabe 3.1 (QUICK SORT)
    // Implementieren und benchmarken Sie QUICK SORT 
    // in der in Vorlesung vorgestellten Variante.
    // Konstruieren Sie dabei auch den Worst-Case geeignet.
    // ------------------------- //

    public static void sort(int[] a) {
        quicksort(a, 0, a.length - 1);
    }

    private static void quicksort(int[] a, int left, int right) {
        int i = left;
        int j = right;
        int middle = (left + right) / 2;
        int pivot = a[middle];
        int tmp;

        do {
            while (a[i] < pivot) i++;
            while (a[j] > pivot) j--;
            if (i <= j) {
                tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
                i++;
                j--;
            }
        } while (i <= j);

        if (left < j) quicksort(a, left, j);
        if (i < right) quicksort(a, i, right);
    }

    // Минимальный тест с бенчмарком и худшим случаем
    public static void main(String[] args) {
        // Худший случай: уже отсортированный массив
        int[] worst = {1,2,3,4,5,6,7,8,9,10};

        long start = System.nanoTime();
        sort(worst);
        long end = System.nanoTime();

        System.out.println("Time (Worst-Case): " + (end - start) + " ns");
    }


    // ----------------------------------- //
    // Aufgabe 3.2 (Randomized QUICK SORT)
    // Wahlen Sie nun anstelle des mittleren Elements ein zuf ̈alliges Element. L ̈asst sich im Laufzeitverhalten eine.
    // Veranderung erkennen.//
    // ----------------------------------- //
    // Отличие: пивот выбирается случайно, а не по центру.
    // Это уменьшает шанс "худшего случая" при почти отсортированных данных.
    static void randomizedQuickSort(int[] a, int l, int r) {
        if (l >= r) return;
        int p = l + (int)(Math.random() * (r - l + 1)); // случайный индекс
        int pivot = a[p];
        a[p] = a[r];
        a[r] = pivot; // переносим пивот в конец для удобства
        int i = l;
        for (int j = l; j < r; j++) {
            if (a[j] < pivot) {
                int t = a[i];
                a[i] = a[j];
                a[j] = t;
                i++;
            }
        }
        int t = a[i];
        a[i] = a[r];
        a[r] = t;
        randomizedQuickSort(a, l, i - 1);
        randomizedQuickSort(a, i + 1, r);
    }

    // ------------------------------------------ //
    // Aufgabe 3.4 (QUICK SORT mit Median-Berechnung)
    // Implementieren Sie den in der Vorlesung vorgestellten Algorithmus zur Berechnung des Medians. Hinweis:
    // Nutzen Sie zur Bildung der Mengen A, Bund Ceine List-Implementierung (z.B. LinkedList oder Array-
    // List) in Java. Die rekursiven Aufrufe k ̈onnen dann mit Hilfe der toArray()-Funktionalit ̈at implementiert
    // werden. Zeigt die Implementierung das zu erwartende Laufzeitverhalten?//
    // ------------------------------------------ //
    // Алгоритм "Median of Medians":
    // 1. Делим массив на группы по 5 элементов.
    // 2. Находим медиану каждой группы.
    // 3. Из этих медиан рекурсивно ищем медиану.
    // 4. Разделяем элементы на <, =, > этой медианы.
    // 5. Вызываем себя для нужной части.
    static int medianLinear(int[] a, int k) {
        if (a.length <= 5) { // базовый случай — маленький массив
            Arrays.sort(a);
            return a[k];
        }
        List<Integer> meds = new ArrayList<>();
        for (int i = 0; i < a.length; i += 5) {
            int[] g = Arrays.copyOfRange(a, i, Math.min(i + 5, a.length));
            Arrays.sort(g);
            meds.add(g[g.length / 2]);
        }
        int[] mArr = meds.stream().mapToInt(Integer::intValue).toArray();
        int med = medianLinear(mArr, mArr.length / 2); // медиана медиан
        List<Integer> A = new ArrayList<>(), B = new ArrayList<>(), C = new ArrayList<>();
        for (int x : a) {
            if (x < med) A.add(x);
            else if (x > med) C.add(x);
            else B.add(x);
        }
        if (k < A.size()) return medianLinear(A.stream().mapToInt(Integer::intValue).toArray(), k);
        if (k < A.size() + B.size()) return med;
        return medianLinear(C.stream().mapToInt(Integer::intValue).toArray(), k - A.size() - B.size());
    }

    // ------------------------------------------------- //
    // Задание 3.4 — Quick Sort с использованием медианы //
    // ------------------------------------------------- //
    // Здесь перед сортировкой находим медиану (из предыдущего метода)
    // и используем её как пивот. Это делает разбиение более сбалансированным.
    static void quickSortWithMedian(int[] a, int l, int r) {
        if (l >= r) return;
        int[] sub = Arrays.copyOfRange(a, l, r + 1);
        int med = medianLinear(sub, sub.length / 2);
        int i = l, j = r;
        while (i <= j) {
            while (a[i] < med) i++;
            while (a[j] > med) j--;
            if (i <= j) {
                int t = a[i];
                a[i] = a[j];
                a[j] = t;
                i++;
                j--;
            }
        }
        if (l < j) quickSortWithMedian(a, l, j);
        if (i < r) quickSortWithMedian(a, i, r);
    }

    // ---------------- //
    // Тест и сравнение //
    // ---------------- //
    // Сравнивает все четыре варианта: обычный, случайный, с медианой и поиск медианы отдельно.
    public static void main(String[] args) {
        int[] a = {5,1,9,3,7,2,8,4,6};
        int[] b = a.clone(), c = a.clone(), d = a.clone();

        quickSort(a,0,a.length-1);
        randomizedQuickSort(b,0,b.length-1);
        quickSortWithMedian(d,0,d.length-1);

        System.out.println("QuickSort: " + Arrays.toString(a));
        System.out.println("Randomized: " + Arrays.toString(b));
        System.out.println("QuickSort + Median: " + Arrays.toString(d));
        System.out.println("Linear Median of c: " + medianLinear(c, c.length/2));
    }
}
