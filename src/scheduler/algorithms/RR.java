package scheduler.algorithms;

import scheduler.Scheduler;
import model.Process;
import java.util.LinkedList;

public class RR extends Scheduler
{
    private final int timeQuantum;
    private int remainingQuantum;

    public RR(int timeQuantum)
    {
        super(new LinkedList<>());
        this.timeQuantum = timeQuantum;
    }


    @Override
    public Process decideNextProcess()
    {

        // Handle quantum expiration
        if (currentProcess != null && remainingQuantum <= 0)
        {
            currentProcess.preempt(); // Proper state transition
            readyQueue.add(currentProcess);
            currentProcess = null;
        }

        // Get next process if needed
        if (currentProcess == null)
        {
            currentProcess = readyQueue.poll();
            remainingQuantum = timeQuantum;
        }

        return currentProcess;
    }

    @Override
    public void onProcessCompleted(Process process)
    {
        super.onProcessCompleted(process);
        remainingQuantum--;
    }
}