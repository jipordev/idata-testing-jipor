package co.istad.idata.generator;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.net.URL;
import java.net.URLClassLoader;

public class DynamicClassLoader {
    public static void compileAndLoad(String className) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, "./src/main/java/" + className.replace('.', '/') + ".java");

        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new URL("file:./src/main/java/")});
        Class<?> cls = Class.forName(className, true, classLoader);
        System.out.println("Class loaded: " + cls.getName());
    }
}
