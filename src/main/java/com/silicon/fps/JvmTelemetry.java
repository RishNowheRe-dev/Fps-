package com.silicon.fps;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * Read-only JVM garbage-collection telemetry.
 * MXBeans are discovered once; getters only sum already-available counters.
 */
public final class JvmTelemetry {
    private static final List<GarbageCollectorMXBean> COLLECTORS =
            List.copyOf(ManagementFactory.getGarbageCollectorMXBeans());

    private JvmTelemetry() {
    }

    /** Returns the JVM's cumulative completed collection count, or -1 if unavailable. */
    public static long getCollectionCount() {
        long total = 0L;
        boolean available = false;
        for (GarbageCollectorMXBean collector : COLLECTORS) {
            long count = collector.getCollectionCount();
            if (count >= 0L) {
                total += count;
                available = true;
            }
        }
        return available ? total : -1L;
    }

    /** Returns cumulative collection time in milliseconds, or -1 if unavailable. */
    public static long getCollectionTimeMillis() {
        long total = 0L;
        boolean available = false;
        for (GarbageCollectorMXBean collector : COLLECTORS) {
            long time = collector.getCollectionTime();
            if (time >= 0L) {
                total += time;
                available = true;
            }
        }
        return available ? total : -1L;
    }
}
