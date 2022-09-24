import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.invoke.MethodHandle;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_INT;

public class Printf {
    public static void main(String[] args) throws Throwable {
        Linker linker = Linker.nativeLinker();
        MethodHandle printf = linker.downcallHandle(
                linker.defaultLookup().lookup("printf").orElseThrow(),
                FunctionDescriptor.ofVoid(ADDRESS, JAVA_INT));

        MemorySegment format = SegmentAllocator.implicitAllocator().allocateUtf8String("Hello world from Java %d");
        int version = Integer.getInteger("java.specification.version");

        printf.invoke(format, version);
    }
}

