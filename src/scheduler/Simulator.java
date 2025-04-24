package scheduler;

import model.GanttChart;

import java.util.*;
import java.util.function.Predicate;
import model.Process;

public final class Simulator
{
    private final int sleepTimeMs = 1000;
    final int TICK_DURATION = 1;

    private final Scheduler scheduler;
    private final GanttChart ganttChart;
    private final List<Process> processes;
    private int timer;
    private volatile boolean isRunning;

    public Simulator(List<Process> processes, Scheduler scheduler, GanttChart ganttChart)
    {
        timer = 0;
        isRunning = false;
        this.scheduler = Objects.requireNonNull(scheduler);
        this.ganttChart = Objects.requireNonNull(ganttChart);
        this.processes = new ArrayList<>(Objects.requireNonNull(processes));
    }

    public synchronized void addProcess(Process process)
    {
        if (isRunning)
            addToReadyQueue(process);
        processes.add(process);
    }

    public void runLive() {
        run(true);
    }

    public void runStatic() {
        run(false);
    }

    /// //////////////////////////////////////////////////////////////////////////////////////////
    private void run(boolean live)
    {
        isRunning = true;
        while (!allProcessesTerminated())
        {
            tick();
            if (live) sleep();
        }
        isRunning = false;
    }

    private void sleep()
    {
        try {Thread.sleep(sleepTimeMs);} // Control tick rate manually
        catch (InterruptedException e) {throw new RuntimeException(e);}
    }

    private boolean allProcessesTerminated()
    {
        return processes.stream()
                .allMatch(p -> p.isInState(Process.State.TERMINATED));
    }

    private void tick()
    {
        if (!isRunning) return;

        checkNewArrivals();

        Process next = scheduler.decideNextProcess();
        if (next != null)
        {
            ganttChart.addEntry(next, timer, timer + TICK_DURATION);
            next.execute(TICK_DURATION);
            scheduler.onProcessCompleted(next);
        }
        else
            ganttChart.addIdleEntry(timer, timer + TICK_DURATION);

        timer += TICK_DURATION;
    }

    private void checkNewArrivals()
    {
        Predicate<Process> isArrived = p ->
                p.getArrivalTime() <= timer &&
                        p.isInState(Process.State.NEW);

        processes.stream()
                .filter(isArrived)
                .forEach(this::addToReadyQueue);

    }

    private void addToReadyQueue(Process process)
    {

        if (!process.isInState(Process.State.NEW))
            return;

        process.transitionTo(Process.State.READY);
        scheduler.addProcess(process);
    }


}