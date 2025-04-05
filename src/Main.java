import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create processes
        List<Process> processes = new ArrayList<>();

        processes.add(new Process(1, "P1", 20, 5, 0));
        processes.add(new Process(2, "P2", 0, 3, 0));
        processes.add(new Process(3, "P3", 10, 8, 0));

        // Initialize components
        GanttChart chart = new GanttChart();
        Scheduler scheduler = new RR(2); // Round Robin with quantum=2
        Simulator simulator = new Simulator(processes,scheduler, chart);


        // Run simulation
        simulator.simulate();

        // Get results
        Results results = new Results(chart);

        // Output results
        System.out.println("=== Simulation Results ===");
        System.out.println("Gantt Chart:");
        chart.getEntries().forEach(System.out::println);

        System.out.println("\nProcess Metrics:");
        results.getAllMetrics().forEach(System.out::println);

        System.out.printf("\nAverage Waiting Time: %.2f\n", results.getAverageWaitingTime());
        System.out.printf("Average Turnaround Time: %.2f\n", results.getAverageTurnaroundTime());
    }
}