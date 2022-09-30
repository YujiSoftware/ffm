import java.io.FileInputStream;
import java.io.IOException;
import java.lang.foreign.GroupLayout;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import static java.lang.foreign.ValueLayout.*;

public class ClassReader {
    private static final GroupLayout CLASS_LAYOUT = MemoryLayout.structLayout(
            JAVA_INT.withOrder(ByteOrder.BIG_ENDIAN).withName("magic"),
            JAVA_SHORT.withOrder(ByteOrder.BIG_ENDIAN).withName("minor_version"),
            JAVA_SHORT.withOrder(ByteOrder.BIG_ENDIAN).withName("major_version")
    );

    private static final VarHandle MAGIC =
            CLASS_LAYOUT.varHandle(PathElement.groupElement("magic"));
    private static final VarHandle MINOR_VERSION =
            CLASS_LAYOUT.varHandle(PathElement.groupElement("minor_version"));
    private static final VarHandle MAJOR_VERSION =
            CLASS_LAYOUT.varHandle(PathElement.groupElement("major_version"));

    public static void main(String[] args) throws IOException {
        ClassLoader loader = ClassReader.class.getClassLoader();
        String name = ClassReader.class.getName() + ".class";

        try (FileInputStream stream = new FileInputStream(loader.getResource(name).getFile());
             FileChannel channel = stream.getChannel()) {
            try (MemorySession session = MemorySession.openConfined()) {
                MemorySegment segment = channel.map(FileChannel.MapMode.READ_ONLY, 0, 8, session);

                int magic = (int) MAGIC.get(segment);
                short minor = (short) MINOR_VERSION.get(segment);
                short major = (short) MAJOR_VERSION.get(segment);

                System.out.format("magic=%02X, minor=%d, major=%d\n", magic, minor, major);
            }
        }
    }
}
