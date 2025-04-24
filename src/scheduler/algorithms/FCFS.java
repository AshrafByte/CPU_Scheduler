package scheduler.algorithms;

import scheduler.Scheduler;
import model.Process;
import java.util.LinkedList;

public class FCFS extends Scheduler
{
    public FCFS()
    {
        super(new LinkedList<>());
    }

    @Override
    public Process decideNextProcess()
    {
        if (currentProcess != null)
            return currentProcess;

        currentProcess = readyQueue.poll();
        return currentProcess;
    }

}
