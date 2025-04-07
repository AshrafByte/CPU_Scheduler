public final class Process {
    private final int pid;
    private final String name;
    private final int arrivalTime;
    private final int burstTime;
    private final int priority;
    private int remainingTime;
    public enum ProcessState {NEW, READY, RUNNING, TERMINATED}
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
        remainingTime = Math.max(0, remainingTime - timeUnits);
        if (remainingTime == 0)
            state = ProcessState.TERMINATED;

    }

    public void preempt() {
        if (state == ProcessState.RUNNING && remainingTime > 0) {
            state = ProcessState.READY;
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
    public void setRunning() {state = ProcessState.RUNNING;}
    public void setReady() {state = ProcessState.READY;}

}