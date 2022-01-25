package dv512.mr223jp;

import java.util.ArrayList;
import java.util.Random;

/*
 * File:	RandomScheduling.java
 * Course: 	20HT - Operating Systems - 1DV512
 * Author: 	Mohammadali Rashidfarokhi - mr223jp
 * Date: 	November 2020
 */

// TODO: put this source code file into a new Java package with meaningful name (e.g., dv512.YourStudentID)!

// You can implement additional fields and methods in code below, but
// you are not allowed to rename or remove any of it!

// Additionally, please remember that you are not allowed to use any third-party libraries

public class RandomScheduling {
    private ScheduledProcess process;
    private boolean cup = true;
    private int completeProcess;
    private int begin;

    public static class ScheduledProcess {
        int processId;
        int burstTime;
        int arrivalMoment;
        boolean complete;
        boolean begin;

        // The total time the process has waited since its arrival
        int totalWaitingTime;

        // The total CPU time the process has used so far
        // (when equal to burstTime -> the process is complete!)
        int allocatedCpuTime;


        public ScheduledProcess(int processId, int burstTime, int arrivalMoment) {
            this.processId = processId;
            this.burstTime = burstTime;
            this.arrivalMoment = arrivalMoment;
            this.complete = false;
            this.begin = false;
        }
        // ... add further fields and methods, if necessary
    }

    // Random number generator that must be used for the simulation
    Random rng;

    // ... add further fields and methods, if necessary
    ArrayList<ScheduledProcess> Processes = new ArrayList<>();

    public RandomScheduling(long rngSeed) {
        this.rng = new Random(rngSeed);
    }

    int etime;
    int t;
    int avgwaiting;

    public void reset() {
        Processes.clear();
        t = 0;
        avgwaiting = 0;

// TODO - remove any information from the previous simulation, if necessary

    }

    public void runNewSimulation(final boolean isPreemptive, final int timeQuantum,
                                 final int numProcesses,
                                 final int minBurstTime, final int maxBurstTime,
                                 final int maxArrivalsPerTick, final double probArrival) {

        // TODO:
        // 1. Run a simulation as a loop, with one iteration considered as one unit of time (tick)
        // 2. The simulation should involve the provided number of processes "numProcesses"
        // 3. The simulation loop should finish after the all of the processes have completed
        // 4. On each tick, a new process might arrive with the given probability (chance)
        // 5. However, the max number of new processes per tick is limited
        // by the given value "maxArrivalsPerTick"
        // 6. The burst time of the new process is chosen randomly in the interval
        // between the values "minBurstTime" and "maxBurstTime" (inclusive)

        // 7. When the CPU is idle and no process is running, the scheduler
        // should pick one of the available processes *at random* and start its execution
        // 8. If the preemptive version of the scheduling algorithm is invoked, then it should
        // allow up to "timeQuantum" time units (loop iterations) to the process,
        // and then preempt the process (pause its execution and return it to the queue)

        // If necessary, add additional fields (and methods) to this class to
        // accomodate your solution

        // Note: for all of the random number generation purposes in this method,
        // use "this.rng" !

        cup = true;
        int establishProcesses = 0;
        completeProcess = 0;
        int unhandled = numProcesses;
        etime = 0;
        process = null;
        int identifier;

        while (completeProcess != numProcesses) {

            int tick = 0;
            int x = 0;
            do {
                if (x >= unhandled) break;
                double random = rng.nextDouble();

                if (random <= probArrival) {
                    establishProcesses++;
                    tick++;
                    int burst;
                    burst = this.rng.nextInt(maxBurstTime - minBurstTime + 1) + minBurstTime;
                    Processes.add(new ScheduledProcess(establishProcesses, burst, etime));
                }
                if (tick < maxArrivalsPerTick) {
                    x++;
                } else {
                    break;
                }
            } while (true);
            unhandled = numProcesses - establishProcesses;
            if (cup) {
                identifier = rng.nextInt(Processes.size());
                if (Processes.get(identifier).complete) {
                    do {
                        identifier = rng.nextInt(Processes.size());
                    } while (Processes.get(identifier).complete);
                }
                process = Processes.get(identifier);
                process.begin = true;
            }
            if (!isPreemptive) {
                nonPreemptive();
            } else {
                preemptive(timeQuantum);
            }
            etime++;
        }


        int i = 0;
        while (i < Processes.size()) {
            ScheduledProcess process;
            process = Processes.get(i);
            t = t + process.totalWaitingTime;
            i++;
        }
        avgwaiting = t / numProcesses;
        //reset();
    }

    public void nonPreemptive() {

        if (process.begin) {
            begin = etime;
        }
        process.totalWaitingTime = begin - process.arrivalMoment;
        process.allocatedCpuTime = process.allocatedCpuTime + 1;
        process.begin = false;
        if (process.allocatedCpuTime != process.burstTime) {
            cup = false;
        } else {
            process.complete = true;
            cup = true;
            begin = 0;
            completeProcess++;
        }
    }

    public void preemptive(final int timeQuantum) {
        process.allocatedCpuTime = process.allocatedCpuTime + 1;
        process.totalWaitingTime = (etime + 1) - process.arrivalMoment - process.allocatedCpuTime;
        begin += 1;
        cup = false;
        if (begin >= timeQuantum) {
            cup = true;
            begin = 0;
        }
        if (process.allocatedCpuTime != process.burstTime) {
            return;
        }
        cup = true;
        process.complete = true;
        completeProcess += 1;
    }


    public void printResults() {
        // TODO:
        // 1. For each process, print its ID, burst time, arrival time, and total waiting time
        // 2. Afterwards, print the complete execution time of the simulation
        // and the average process waiting time
        int i = 0;
        while (i < Processes.size()) {
            ScheduledProcess process = Processes.get(i);
            String information = "process Id => " + process.processId + " ,BurstTime => " + process.burstTime +
                    " ,arrivalMoment => " + process.arrivalMoment + " ,totalWaitingTime => " + process.totalWaitingTime;
            System.out.println(information);
            i++;
        }
        System.out.println("**********");
        System.out.println("Complete execution time => " + etime);
        System.out.println("Average process Waiting time => " + avgwaiting);
        reset();
    }

    public static void main(String args[]) {
        // TODO: replace the seed value below with your birth date, e.g., "20001001"
        final long rngSeed = 19990601;


        // Do not modify the code below — instead, complete the implementation
        // of other methods!
        RandomScheduling scheduler = new RandomScheduling(rngSeed);

        final int numSimulations = 5;
        final int numProcesses = 10;
        final int minBurstTime = 2;
        final int maxBurstTime = 10;
        final int maxArrivalsPerTick = 2;
        final double probArrival = 0.75;
        final int timeQuantum = 2;

        boolean[] preemptionOptions = {false, true};

        for (boolean isPreemptive : preemptionOptions) {

            for (int i = 0; i < numSimulations; i++) {
                System.out.println("Running " + ((isPreemptive) ? "preemptive" : "non-preemptive")
                        + " simulation #" + i);

                scheduler.runNewSimulation(
                        isPreemptive, timeQuantum,
                        numProcesses,
                        minBurstTime, maxBurstTime,
                        maxArrivalsPerTick, probArrival);

                System.out.println("Simulation results:"
                        + "\n" + "----------------------");
                scheduler.printResults();

                System.out.println("\n");
            }
        }
    }
}