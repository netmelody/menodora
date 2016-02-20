package org.netmelody.menodora.core;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public final class Timer {

    private final java.util.Timer timer = new java.util.Timer();
    private final List<TimerTaskWrapper> wrappers = new ArrayList<Timer.TimerTaskWrapper>();

    public void schedule(TimerTask task, long delay) {
        final TimerTaskWrapper wrapper = new TimerTaskWrapper(task);
        wrappers.add(wrapper);
        timer.schedule(wrapper, delay);
    }

    public void schedule(TimerTask task, long delay, long period) {
        final TimerTaskWrapper wrapper = new TimerTaskWrapper(task);
        wrappers.add(wrapper);
        timer.schedule(wrapper, delay, period);
    }

    public void purge() {
        timer.purge();
    }

    public void done() {
        while(!allDone()) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
            }
        }
    }

    public boolean allDone() {
        for (TimerTaskWrapper task : wrappers) {
            if (!task.done) {
                return false;
            }
        }
        return true;
    }

    private final class TimerTaskWrapper extends TimerTask {
        private final TimerTask wrapped;
        private boolean done;

        private TimerTaskWrapper(TimerTask wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public void run() {
            wrapped.run();
            done = true;
        }

        @Override
        public boolean cancel() {
            done = true;
            return super.cancel();
        }
    }
}
