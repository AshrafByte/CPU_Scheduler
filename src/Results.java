import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class Results {
    private final List<ProcessMetrics> processMetrics = new ArrayList<>();

    public Results(GanttChart ganttChart) {
        calculateMetrics(ganttChart);
    }

    private void calculateMetrics(GanttChart ganttChart)
    {
        // Group entries by process and find their last execution
        Map<Process, Optional<GanttEntry>> lastExecutions = ganttChart.getEntries().stream()
                .filter(entry -> entry.getProcess() != null) // Skip idle entries
                .collect(Collectors.groupingBy(
                        GanttEntry::getProcess,
                        Collectors.maxBy(Comparator.comparingLong(GanttEntry::getEndTime))
                ));

        // Calculate metrics for each process
        lastExecutions.forEach((process, lastEntryOpt) -> {
            if (lastEntryOpt.isPresent())
            {
                GanttEntry lastEntry = lastEntryOpt.get();
                int finishTime = (int) lastEntry.getEndTime();
                int turnaround = finishTime - process.getArrivalTime();
                int waiting = turnaround - process.getBurstTime();
                processMetrics.add(new ProcessMetrics(process, turnaround, waiting));
            }
        });
    }

    public List<ProcessMetrics> getAllMetrics() {
        return new ArrayList<>(processMetrics);
    }

    public double getAverageWaitingTime() {
        return processMetrics.stream()
                .mapToInt(ProcessMetrics::waitingTime)
                .average()
                .orElse(0.0);
    }

    public double getAverageTurnaroundTime() {
        return processMetrics.stream()
                .mapToInt(ProcessMetrics::turnaroundTime)
                .average()
                .orElse(0.0);
    }
}