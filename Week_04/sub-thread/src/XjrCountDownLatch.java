import java.util.concurrent.CountDownLatch;

public class XjrCountDownLatch {

    private static int result = 0;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        long start = System.currentTimeMillis();

        new Thread(new Runnable() {
            @Override
            public void run() {
                result = sum();
                countDownLatch.countDown();
            }
        }).start();
        
        countDownLatch.await();
        System.out.println("异步计算结果为：" + result);
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
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

