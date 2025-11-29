import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;

import static java.lang.foreign.ValueLayout.*;

public class MessageBoxA {
    private static final int MB_ICONINFORMATION = 0x00000040;
    private static final int MB_OKCANCEL = 0x00000001;

    public static void main(String[] args) throws Throwable {
        Linker linker = Linker.nativeLinker();
        try (Arena arena = Arena.ofConfined()) {
            SymbolLookup lookup =
                    SymbolLookup.libraryLookup("User32", arena);
            MethodHandle messageBox = linker.downcallHandle(
                    lookup.find("MessageBoxA").orElseThrow(),
                    FunctionDescriptor.of(JAVA_INT, JAVA_LONG, ADDRESS, ADDRESS, JAVA_INT));

            MemorySegment text = arena.allocateFrom("Hello world.");
            MemorySegment caption = arena.allocateFrom("Java");

            int ret = (int) messageBox.invoke(0, text, caption, MB_OKCANCEL | MB_ICONINFORMATION);

            System.out.println(ret);
        }
    }
}
