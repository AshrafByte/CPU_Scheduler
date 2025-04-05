import java.util.*;

public class RR extends Scheduler
{
    private final int timeQuantum;
    private Process currentProcess;
    private int remainingQuantum;

    public RR(int timeQuantum)
    {
        this.timeQuantum = timeQuantum;
        super.readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getArrivalTime));
    }

    @Override
    public void initialize(List<Process> processes)
    {
        readyQueue.addAll(processes);
        remainingQuantum = timeQuantum;
    }

    @Override
    public Process decideNextProcess()
    {
        if (remainingQuantum == 0 || currentProcess == null)
        {
            if (currentProcess != null && currentProcess.getState() != Process.ProcessState.TERMINATED)
                readyQueue.add(currentProcess);

            currentProcess = readyQueue.poll();
            remainingQuantum = timeQuantum;
        }
        return currentProcess;
    }

    @Override
    public void onProcessCompleted(Process process)
    {
        remainingQuantum = Math.min(remainingQuantum - 1, process.getRemainingTime());
    }
}