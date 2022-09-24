import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;

public class StrLen {
    public static void main(String[] args) throws Throwable {
        Linker linker = Linker.nativeLinker();
        MethodHandle strlen = linker.downcallHandle(
                linker.defaultLookup().lookup("strlen").orElseThrow(),
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));

        MemorySegment cString = SegmentAllocator.implicitAllocator().allocateUtf8String("Hello");
        long len = (long) strlen.invoke(cString);

        System.out.println(len);
    }
}
