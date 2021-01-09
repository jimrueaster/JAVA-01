
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) {

        try {
            Object obj = new HelloClassLoader().findClass("Hello").getDeclaredConstructor().newInstance();
            Method method = obj.getClass().getMethod("hello");
            method.invoke(obj);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException aE) {
            aE.printStackTrace();
        }
    }

    private byte[] readFile(String filePath) {
        byte[] result = null;
        try {
            result = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException aE) {
            aE.printStackTrace();
        }
        return result;
    }

    private byte offset255(byte c) {

        return (byte) (255 - c);
    }

    @Override
    protected Class<?> findClass(String name) {

        byte[] xlassBytes = readFile("./Hello.xlass");

        byte[] classBytes = new byte[xlassBytes.length];

        for (int i = 0; i < xlassBytes.length; i++) {
            classBytes[i] = offset255(xlassBytes[i]);
        }

        return defineClass(name, classBytes, 0, classBytes.length);
    }
}
