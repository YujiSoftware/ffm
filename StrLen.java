import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;

public class StrLen {
    public static void main(String[] args) throws Throwable {
        Linker linker = Linker.nativeLinker();
        MemorySegment symbol =
                linker.defaultLookup().lookup("strlen").orElseThrow();
        FunctionDescriptor descriptor =
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS);
        MethodHandle strlen =
                linker.downcallHandle(symbol, descriptor);

        MemorySegment cString =
                SegmentAllocator.implicitAllocator().allocateUtf8String("Hello");
        long len = (long) strlen.invoke(cString);

        System.out.println(len);
    }
}
