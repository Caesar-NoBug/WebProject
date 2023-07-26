package org.caesar.finalWork.util;

import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class ThreadUtil {
    public static int getCountOfWaitingThreads(Object monitor) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] ids = bean.findMonitorDeadlockedThreads();
        ThreadInfo[] threadsInfo = bean.getThreadInfo(ids, true, true);

        int waitingThreads = 0;
        for (ThreadInfo info : threadsInfo) {
            // 遍历当前死锁的线程
            MonitorInfo[] monitors = info.getLockedMonitors();
            for (MonitorInfo monitorInfo : monitors) {
                // 判断目标监视器是否被锁定
                if (monitorInfo.getIdentityHashCode() == System.identityHashCode(monitor)) {
                    waitingThreads++;
                }
            }
        }
        return waitingThreads;
    }
}
