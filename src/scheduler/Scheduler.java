package scheduler;

import java.util.Queue;
import model.Process;

public abstract class Scheduler
{
    protected Queue<Process> readyQueue;
    protected Process currentProcess;

    public Scheduler(Queue<Process> readyQueue)
    {
        this.readyQueue = readyQueue;
        currentProcess = null;
    }

    public void addProcess(Process process)
    {
        if ( readyQueue != null && process.isInState(Process.State.READY))
            readyQueue.add(process);

    }

    public abstract Process decideNextProcess();

    public void onProcessCompleted(Process process)
    {
        if (process.isInState(Process.State.TERMINATED))
            currentProcess = null;
    }

}