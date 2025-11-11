import java.util.Arrays;

/*
 * Задание 3.1: Реализация QuickSort по варианту из лекции (слайд 36).
 * В качестве опорного элемента (Pivot) выбирается средний элемент.
 */
public class QuickSortMitte {

    /*
     * Публичный метод-обертка, который запускает сортировку.
     */
    public static void sort(int[] a) {
        // Вызываем рекурсивный метод, начиная со всего массива
        quicksort(a, 0, a.length - 1);
    }

    /*
     * Рекурсивный вспомогательный метод QuickSort.
     * @param a      Массив для сортировки
     * @param left   Левая граница (индекс)
     * @param right  Правая граница (индекс)
     */
    private static void quicksort(int[] a, int left, int right) {
        
        int i = left;  // Левый указатель
        int j = right; // Правый указатель

        // 1. Выбор опорного элемента (Pivot)
        // Выбираем элемент из середины, как показано в лекции
        int middle = (left + right) / 2;
        int pivot = a[middle];

        // 2. Этап разделения (Partition)
        // Этот do-while цикл взят прямо со слайда 36
        do {
            // Двигаем левый указатель (i) вправо,
            // пока не найдем элемент >= pivot
            while (a[i] < pivot) {
                i++;
            }

            // Двигаем правый указатель (j) влево,
            // пока не найдем элемент <= pivot
            while (a[j] > pivot) {
                j--;
            }

            // Если указатели не "перекрестились" (i все еще левее j)
            if (i <= j) {
                // ...меняем элементы a[i] и a[j] местами
                int tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;

                // Сдвигаем указатели, чтобы продолжить поиск
                i++;
                j--;
            }
        } while (i <= j); // (пока указатели не пересекутся)

        // 3. Рекурсивные вызовы
        // Рекурсивно сортируем левую часть (если она есть)
        if (left < j) {
            quicksort(a, left, j);
        }
        // Рекурсивно сортируем правую часть (если она есть)
        if (i < right) {
            quicksort(a, i, right);
        }
    }

    // --- Тестирование и "Worst Case" ---
    public static void main(String[] args) {
        System.out.println("--- Задание 3.1: QuickSort (Middle Pivot) ---");

        // 1. Обычный случай (неотсортированный массив)
        int[] arrayNormal = {92, 37, 61, 59, 42, 79, 28, 14, 84};
        System.out.println("До:    " + Arrays.toString(arrayNormal));
        sort(arrayNormal);
        System.out.println("После: " + Arrays.toString(arrayNormal));
        System.out.println();

        // 2. "Worst Case" (Худший случай)
        // В задании просят "сконструировать" худший случай.
        // Худший случай для QuickSort (O(n^2)) наступает, когда pivot
        // всегда оказывается минимумом или максимумом.
        // Для многих реализаций (например, с выбором 'a[left]' как pivot)
        // худшим случаем является УЖЕ отсортированный массив.

        // НО! Наша реализация (слайд 36) берет СРЕДНИЙ элемент.
        // Для нее отсортированный массив - это ЛУЧШИЙ случай (Best Case)!
        // (т.к. средний элемент будет медианой)

        // Давайте просто покажем это на отсортированном массиве.
        int[] arrayWorstCase = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println("До (Sorted - 'Worst Case' для других): " + Arrays.toString(arrayWorstCase));
        sort(arrayWorstCase);
        System.out.println("После: " + Arrays.toString(arrayWorstCase));
    }
}


import java.util.Arrays;
import java.util.Random;

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
        int i = left;
        int j = right;

        // 1. Выбор опорного элемента (Pivot)
        // ВМЕСТО ВЫБОРА СРЕДНЕГО...
        // ...выбираем случайный элемент в диапазоне [left, right]
        int randomIndex = left + randomGenerator.nextInt(right - left + 1);
        int pivot = a[randomIndex];

        // 2. Этап разделения (Partition) - (Код идентичен 3.1)
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

        // 3. Рекурсивные вызовы - (Код идентичен 3.1)
        if (left < j) quicksort(a, left, j);
        if (i < right) quicksort(a, i, right);
    }

    // --- Тестирование ---
    public static void main(String[] args) {
        System.out.println("--- Задание 3.2: QuickSort (Random Pivot) ---");
        int[] arrayNormal = {92, 37, 61, 59, 42, 79, 28, 14, 84};
        System.out.println("До:    " + Arrays.toString(arrayNormal));
        sort(arrayNormal);
        System.out.println("После: " + Arrays.toString(arrayNormal));
    }
}


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Задание 3.3: Алгоритм "Медиана Медиан" (Select).
 * Находит k-й по величине элемент в массиве за линейное время O(n).
 * Реализация основана на псевдокоде (слайд 43).
 */
public class LinearTimeMedian {

    /*
     * Главная функция: находит k-й (по индексу) наименьший элемент в S.
     * @param S Массив
     * @param k Индекс искомого элемента (0...n-1)
     * @return k-й наименьший элемент
     */
    public static int select(int[] S, int k) { 
        int n = S.length;

        // 1. Базовый случай (Base Case)
        // Если массив маленький, просто сортируем его и берем k-й элемент.
        // В лекции n < 50
        if (n < 50) {
            Arrays.sort(S);
            return S[k];
        }

        // 2. Разделение на группы по 5 и поиск медиан
        
        int numMedians = (int) Math.ceil(n / 5.0);
        int[] medians = new int[numMedians];

        for (int i = 0; i < numMedians; i++) {
            int groupStart = i * 5;
            int groupEnd = Math.min(groupStart + 5, n);

            // Находим медиану в каждой группе (простой сортировкой)
            medians[i] = findMedianOfGroup(S, groupStart, groupEnd);
        }

        // 3. Рекурсивно ищем "Медиану Медиан" (m)
        
        int m = select(medians, (numMedians - 1) / 2);

        // 4. Разделение S на 3 группы: A(<m), B(=m), C(>m)
        // Используем List, как в подсказке к заданию
        List<Integer> A = new ArrayList<>();
        List<Integer> B = new ArrayList<>();
        List<Integer> C = new ArrayList<>();

        for (int x : S) {
            if (x < m) A.add(x);
            else if (x == m) B.add(x);
            else C.add(x);
        }

        // 5. Рекурсивный поиск в одной из групп
        if (k < A.size()) {
            // Искомый элемент в группе A
            return select(listToArray(A), k);
        } else if (k < A.size() + B.size()) {
            // Искомый элемент в группе B (это сама медиана m)
            return m;
        } else {
            // Искомый элемент в группе C
            // k обновляется: k - |A| - |B|
            return select(listToArray(C), k - A.size() - B.size());
        }
    }

    /*
     * Вспомогательный метод: находит медиану в маленькой группе.
     */
    private static int findMedianOfGroup(int[] S, int start, int end) {
        int[] group = Arrays.copyOfRange(S, start, end);
        Arrays.sort(group);
        return group[group.length / 2];
    }

    /*
     * Вспомогательный метод: конвертирует List<Integer> в int[].
     * (Как в подсказке к заданию)
     */
    private static int[] listToArray(List<Integer> list) {
        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }

    // --- Тестирование ---
    public static void main(String[] args) {
        System.out.println("--- Задание 3.3: Linear Time Median (Select) ---");
        // Массив побольше, чтобы n > 50
        int[] array = {
              10, 5, 7, 6, 8, 2, 9, 3, 4, 1,
              20, 25, 27, 26, 28, 22, 29, 23, 24, 21,
              30, 35, 37, 36, 38, 32, 39, 33, 34, 31,
              40, 45, 47, 46, 48, 42, 49, 43, 44, 41,
              50, 55, 57, 56, 58, 52, 59, 53, 54, 51
        };
        System.out.println("Массив (50 элементов): " + Arrays.toString(array));

        // Медиана - это k = (n-1)/2 = 24-й элемент (индекс 24)
        int medianK = (array.length - 1) / 2;
        int medianValue = select(array, medianK);
        System.out.println("Ищем k=" + medianK + " (медиану).");
        System.out.println("Найденная медиана: " + medianValue); // Должно быть 25.5 -> 25 или 26

        // Давайте отсортируем для проверки
        Arrays.sort(array);
        System.out.println("Проверка (массив отсорт.): " + Arrays.toString(array));
        System.out.println("Медиана в отсорт. массиве: " + array[medianK]);
    }
}


import java.util.Arrays;

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
        // Добавляем базовый случай, т.к. нам нужен n > 0 для 'select'
        if (left >= right) {
            return;
        }

        int i = left;
        int j = right;
        int n = right - left + 1; // Размер текущего подмассива

        // 1. Выбор опорного элемента (Pivot)
        // Создаем временный подмассив, т.к. наш 'select' работает с целым массивом
        int[] subarray = new int[n];
        System.arraycopy(a, left, subarray, 0, n);

        // Находим индекс медианы k для текущего подмассива
        int medianK = (n - 1) / 2;

        // Вычисляем настоящую медиану, используя наш алгоритм из 3.3
        int pivot = LinearTimeMedian.select(subarray, medianK);

        // 2. Этап разделения (Partition) - (Код идентичен 3.1)
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

        // 3. Рекурсивные вызовы - (Код идентичен 3.1)
        if (left < j) quicksort(a, left, j);
        if (i < right) quicksort(a, i, right);
    }

    // --- Тестирование ---
    public static void main(String[] args) {
        System.out.println("--- Задание 3.4: QuickSort (Median Pivot) ---");
        int[] arrayNormal = {92, 37, 61, 59, 42, 79, 28, 14, 84};
        System.out.println("До:    " + Arrays.toString(arrayNormal));
        sort(arrayNormal);
        System.out.println("После: " + Arrays.toString(arrayNormal));
    }
}