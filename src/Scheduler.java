import java.util.List;
import java.util.PriorityQueue;

public abstract class Scheduler
{
    PriorityQueue<Process> readyQueue;

    public void addProcess(Process process)
    {
        if (readyQueue != null)
            readyQueue.add(process);
    }

    public abstract void initialize(List<Process> processes);

    public abstract Process decideNextProcess();

    public abstract void onProcessCompleted(Process process);
}