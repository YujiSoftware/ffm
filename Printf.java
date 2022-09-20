import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_INT;

public class Printf {
    private static final boolean isWindows = System.getProperty("os.name").startsWith("Windows");

    private static final Linker LINKER = Linker.nativeLinker();

    private static final SymbolLookup LOOKUP = SymbolLookup.libraryLookup(isWindows ? "msvcrt" : "c", MemorySession.global());

    private static final MemorySegment PRINTF_SYMBOL =
            LOOKUP.lookup("printf").orElseThrow(() -> new RuntimeException("The symbol printf is not found"));

    private static final FunctionDescriptor PRINTF_FUNCTION = FunctionDescriptor.ofVoid(ADDRESS, JAVA_INT);

    private static final MethodHandle PRINTF = LINKER.downcallHandle(PRINTF_SYMBOL, PRINTF_FUNCTION);

    public static void main(String[] args) throws Throwable {
        MemorySegment format = SegmentAllocator.implicitAllocator().allocateUtf8String("Hello world from Java %d");
        int version = Integer.getInteger("java.specification.version");
        PRINTF.invoke(format, version);
    }
}

