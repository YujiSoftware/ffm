import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.invoke.MethodHandle;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_INT;

public class Printf {
    private static final Linker LINKER = Linker.nativeLinker();

    private static final MethodHandle PRINTF = LINKER.downcallHandle(
            LINKER.defaultLookup().lookup("printf").orElseThrow(() -> new RuntimeException("The symbol printf is not found")),
            FunctionDescriptor.ofVoid(ADDRESS, JAVA_INT));

    public static void main(String[] args) throws Throwable {
        MemorySegment format = SegmentAllocator.implicitAllocator().allocateUtf8String("Hello world from Java %d");
        int version = Integer.getInteger("java.specification.version");

        PRINTF.invoke(format, version);
    }
}

