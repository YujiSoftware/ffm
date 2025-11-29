import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.invoke.MethodHandle;

import static java.lang.foreign.ValueLayout.JAVA_INT;

public class Raise {
    public static void main(String[] args) throws Throwable {
        Linker linker = Linker.nativeLinker();
        MethodHandle raise =
                linker.downcallHandle(
                        linker.defaultLookup().find("raise").orElseThrow(),
                        FunctionDescriptor.of(JAVA_INT, JAVA_INT));
        
        // kill -3 = get java thread dump
        int ret = (int) raise.invoke(3);
        System.out.println(ret);
    }
}
