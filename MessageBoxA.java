import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;

import static java.lang.foreign.ValueLayout.*;

public class MessageBoxA {
    private static final int MB_ICONINFORMATION = 0x00000040;
    private static final int MB_OKCANCEL = 0x00000001;

    public static void main(String[] args) throws Throwable {
        Linker linker = Linker.nativeLinker();
        try (MemorySession session = MemorySession.openConfined()) {
            SymbolLookup lookup = SymbolLookup.libraryLookup("User32", session);
            MethodHandle messageBox = linker.downcallHandle(
                    lookup.lookup("MessageBoxA").orElseThrow(),
                    FunctionDescriptor.of(JAVA_INT, JAVA_LONG, ADDRESS, ADDRESS, JAVA_INT));

            MemorySegment text = session.allocateUtf8String("Hello world.");
            MemorySegment caption = session.allocateUtf8String("Java");

            int ret = (int) messageBox.invoke(0, text, caption, MB_OKCANCEL | MB_ICONINFORMATION);

            System.out.println(ret);
        }
    }
}
