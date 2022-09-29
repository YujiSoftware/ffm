import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.charset.StandardCharsets;

import static java.lang.foreign.ValueLayout.*;

public class MessageBox {
    private static final int MB_ICONINFORMATION = 0x00000040;
    private static final int MB_OKCANCEL = 0x00000001;

    public static MemorySegment allocateUtf16String(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_16LE);
        MemorySegment addr = MemorySession.openImplicit().allocate(bytes.length + 1);
        
        MemorySegment segment = MemorySegment.ofArray(bytes);
        addr.copyFrom(segment);
        addr.set(JAVA_BYTE, bytes.length, (byte) 0);

        return addr;
    }

    public static void main(String[] args) throws Throwable {
        Linker linker = Linker.nativeLinker();
        SymbolLookup lookup =
                SymbolLookup.libraryLookup("User32", MemorySession.openImplicit());
        MemorySegment symbol =
                lookup.lookup("MessageBoxW").orElseThrow();
        FunctionDescriptor descriptor =
                FunctionDescriptor.of(JAVA_INT, JAVA_LONG, ADDRESS, ADDRESS, JAVA_INT);
        MethodHandle messageBox =
                linker.downcallHandle(symbol, descriptor);

        MemorySegment text = allocateUtf16String("Hello world!");
        MemorySegment caption = allocateUtf16String("Java");

        int ret = (int) messageBox.invoke(0, text, caption, MB_OKCANCEL | MB_ICONINFORMATION);
        System.out.println(ret);
    }
}
