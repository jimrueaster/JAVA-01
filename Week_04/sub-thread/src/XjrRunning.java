
import java.io.IOException;

public class XjrRunning {

    private static int[] result = new int[1];
    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                result[0] = sum();
            }
        });

        thread1.start();
        thread1.join();

        System.out.println(result[0]);


    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }
}

