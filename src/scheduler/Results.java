package scheduler;

import model.GanttChart;
import model.GanttEntry;
import model.ProcessMetrics;
import model.Process;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class Results
{
    private final List<ProcessMetrics> processMetrics = new ArrayList<>();

    public Results(GanttChart ganttChart)
    {
        calculateMetrics(ganttChart);
    }

    private void calculateMetrics(GanttChart ganttChart)
    {
        // Define the predicate to check if the process is terminated
        Predicate<GanttEntry> isTerminatedProcess = entry ->
                entry.getProcess() != null
                        && entry.getProcess().isInState(Process.State.TERMINATED);

        // Using streams to filter, map, and collect the results
        Map<Process, Integer> completionTimes = ganttChart.getEntries().stream()
                .filter(isTerminatedProcess)  // Use the predicate for filtering
                .collect(Collectors.toMap(
                        GanttEntry::getProcess,
                        entry -> (int) entry.getEndTime(),
                        Math::max // In case of duplicates
                ));

        // Calculate metrics in a single stream operation
        completionTimes.forEach(this::addProcessMetrics);

        /*
        The map would look like:
        completionTimes =
        {
            model.Process{id=1, name="model.Process A"} -> 50,
            model.Process{id=2, name="model.Process B"} -> 70,
            model.Process{id=3, name="model.Process C"} -> 45
         }
        */
    }


        public List<ProcessMetrics> getAllMetrics ()
        {
            return new ArrayList<>(processMetrics);
        }

        public double getAverageWaitingTime ()
        {
            return processMetrics.stream()
                    .mapToInt(ProcessMetrics::waitingTime)
                    .average()
                    .orElse(0.0);
        }

        public double getAverageTurnaroundTime ()
        {
            return processMetrics.stream()
                    .mapToInt(ProcessMetrics::turnaroundTime)
                    .average()
                    .orElse(0.0);
        }


        private void addProcessMetrics (Process process, int finish)
        {
            int turnaround = finish - process.getArrivalTime();
            int waiting = turnaround - process.getBurstTime();
            processMetrics.add(new ProcessMetrics(process, turnaround, waiting));
        }
    }