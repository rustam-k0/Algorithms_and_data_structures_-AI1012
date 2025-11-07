public class Main {

    // 1.1 Lineare Suche
    static int linear(int[] a, int x) {
        for (int i = 0; i < a.length; i++)
            if (a[i] == x) return i;
        return -1;
    }

    // Binäre Suche – hier: erstes Vorkommen von 0 in sortiertem Array
    static int binary(int[] a) {
        int l = 0, r = a.length - 1, res = -1;
        while (l <= r) {
            int m = (l + r) / 2;
            if (a[m] == 0) {
                res = m;
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return res;
    }

    // 1.2 Bubble Sort
    static void bubble(int[] a) {
        for (int i = 0; i < a.length - 1; i++)
            for (int j = 0; j < a.length - i - 1; j++)
                if (a[j] > a[j + 1]) {
                    int t = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = t;
                }
    }

    // 1.3 Selection Sort
    static void selection(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++)
                if (a[j] < a[min]) min = j;
            int t = a[i];
            a[i] = a[min];
            a[min] = t;
        }
    }

    public static void main(String[] args) {
        int n = 100000;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;  // Sortiertes Array

        // Zeitmessung Binary Search
        long t0 = System.nanoTime();
        binary(a);
        System.out.println("Binary: " + (System.nanoTime() - t0) / 1e6 + " ms");

        // Zeitmessung Linear Search
        t0 = System.nanoTime();
        linear(a, -1);
        System.out.println("Linear: " + (System.nanoTime() - t0) / 1e6 + " ms");

        // Kleineres Array für Sortieralgorithmen
        int[] arr = new int[10000];
        for (int i = 0; i < arr.length; i++) arr[i] = arr.length - i;

        // Zeit Bubble Sort
        t0 = System.nanoTime();
        bubble(arr.clone());
        System.out.println("Bubble: " + (System.nanoTime() - t0) / 1e6 + " ms");

        // Zeit Selection Sort
        t0 = System.nanoTime();
        selection(arr.clone());
        System.out.println("Selection: " + (System.nanoTime() - t0) / 1e6 + " ms");
    }
}
