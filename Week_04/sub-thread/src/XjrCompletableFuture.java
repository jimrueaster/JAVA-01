import java.util.concurrent.CompletableFuture;

public class XjrCompletableFuture {
    public static void main(String[] args) {
        int result = CompletableFuture.supplyAsync(XjrCompletableFuture::sum).join();
        System.out.println(result);
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

