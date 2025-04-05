public final class Process {
    private final int pid;
    private final String name;
    private final int arrivalTime;
    private final int burstTime;
    private final int priority;
    private int remainingTime;
    private ProcessState state;

    public Process(int pid, String name, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.state = ProcessState.NEW;
    }

    public void execute(int timeUnits) {
        this.remainingTime -= timeUnits;
        if (this.remainingTime <= 0) {
            this.state = ProcessState.TERMINATED;
        }
    }

    // Getters
    public int getPid() { return pid; }
    public String getName() { return name; }
    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public int getPriority() { return priority; }
    public int getRemainingTime() { return remainingTime; }
    public ProcessState getState() { return state; }

    public enum ProcessState {
        NEW, READY, RUNNING, TERMINATED
    }
}