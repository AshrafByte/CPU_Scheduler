package model;

public final class Process {

    public enum State {NEW, READY, RUNNING, TERMINATED}

    private State state;
    private static int counter = 0 ;
    private final int pid;
    private final String name;
    private final int arrivalTime;
    private final int burstTime;
    private final int priority;
    private int remainingTime;


    public Process(String name, int arrivalTime, int burstTime, int priority) {
        this.pid = ++counter;
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.state = State.NEW;
    }

    public Process(String name, int arrivalTime, int burstTime) {
        this(name,arrivalTime,burstTime,0);
    }


    public void execute(int timeUnits)
    {
        remainingTime = Math.max(0, remainingTime - timeUnits);
        if (remainingTime <= 0)
            transitionTo(State.TERMINATED);

    }

    public void preempt()
    {
        if (isInState(State.READY) && remainingTime > 0)
            transitionTo(State.RUNNING);
    }

    public static void resetCounter() {
        counter = 0;
    }

    public int getPid() { return pid; }
    public String getName() { return name; }
    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public int getPriority() { return priority; }
    public int getRemainingTime() { return remainingTime; }
    public void transitionTo(State newState) {this.state = newState;}
    public boolean isInState(State stateToCheck) {return this.state == stateToCheck;}

}