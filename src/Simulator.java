import java.util.Comparator;
import java.util.List;

public final class Simulator {
    private final Scheduler scheduler;
    private final GanttChart ganttChart;
    private final List<Process> processes;

    public Simulator(List<Process> processes,Scheduler scheduler, GanttChart ganttChart) {
        this.scheduler = scheduler;
        this.ganttChart = ganttChart;
        this.processes = processes;
    }

    public void simulate()
    {
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
        scheduler.initialize(processes);
        int currentTime = 0;

        while (!allProcessesTerminated(processes)) {
            Process next = scheduler.decideNextProcess();

            if (next != null)
            {
                ganttChart.addEntry(next, currentTime, currentTime + 1);
                next.execute(1);
                scheduler.onProcessCompleted(next);
            }
            else
                ganttChart.addIdleEntry(currentTime, currentTime + 1);

            currentTime++;
        }
    }

    public void addLiveProcess(Process process)
    {
        scheduler.addProcess(process);
    }

    private boolean allProcessesTerminated(List<Process> processes) {
        return processes.stream()
                .allMatch(p -> p.getState() == Process.ProcessState.TERMINATED);
    }
}