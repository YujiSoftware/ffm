import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.invoke.MethodHandle;

import static java.lang.foreign.ValueLayout.JAVA_INT;

public class Raise {
    private static final Linker LINKER = Linker.nativeLinker();

    private static final MethodHandle RAISE =
            LINKER.downcallHandle(
                    LINKER.defaultLookup().lookup("raise").get(),
                    FunctionDescriptor.of(JAVA_INT, JAVA_INT));

    public static void main(String[] args) throws Throwable {
        // kill -3 = get java thread dump
        int ret = (int) RAISE.invoke(3);
        System.out.println(ret);
    }
}
