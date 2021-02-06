import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class XjrCyclicBarrier {

    private static int[] result = new int[1];

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);


        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("I'm"+Thread.currentThread().getName());
                result[0] = sum();
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException aE) {
                    aE.printStackTrace();
                }
            }
        }).start();

        cyclicBarrier.await(); // 把主线程当成子线程来阻塞，保证能够执行得到 sum() 之后，才执行 sout

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



