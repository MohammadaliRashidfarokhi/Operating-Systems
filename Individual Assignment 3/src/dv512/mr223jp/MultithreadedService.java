package dv512.mr223jp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * File:	MultithreadedService.java
 * Course: 	20HT - Operating Systems - 1DV512
 * Author: 	*State your name and LNU student ID here! (e.g., ab222cd)*
 *                      Mohammadali Rashidfarokhi - mr223jp
 * Date: 	December 2020
 */

// TODO: put this source code file into a new Java package with meaningful name (e.g., dv512.YourStudentID)!

// You can implement additional fields and methods in code below, but
// you are not allowed to rename or remove any of it!

// Additionally, please remember that you are not allowed to use any third-party libraries

/**
 * The type Multithreaded service.
 */
public class MultithreadedService {

    // TODO: implement a nested public class titled Task here
    // which must have an integer ID and specified burst time (duration) in milliseconds,
    // see below
    // Add further fields and methods to it, if necessary
    // As the task is being executed for the specified burst time, 
    // it is expected to simply go to sleep every X milliseconds (specified below)

    /**
     * The Rng.
     */
// Random number generator that must be used for the simulation
    Random rng;
    /**
     * The Executorservice.
     */
    ExecutorService executorservice;


    /**
     * The Services.
     */
    List<Service> services;
    /**
     * The Service map.
     */
    Map<Integer, Service> serviceMap = new HashMap<Integer, Service>();


    /**
     * Instantiates a new Multithreaded service.
     *
     * @param rngSeed the rng seed
     */
    public MultithreadedService(long rngSeed) {
        this.rng = new Random(rngSeed);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
// If the implementation requires your code to throw some exceptions,
    // you are allowed to add those to the signature of this method
    public static void main(String[] args) throws InterruptedException {
        // TODO: replace the seed value below with your birth date, e.g., "20001001"
        final long rngSeed = 19990601;

        // Do not modify the code below — instead, complete the implementation
        // of other methods!
        MultithreadedService service = new MultithreadedService(rngSeed);

        final int numSimulations = 3;
        final long totalSimulationTimeMs = 15 * 1000L; // 15 seconds

        final int numThreads = 4;
        final int numTasks = 30;
        final long minBurstTimeMs = 1 * 1000L; // 1 second
        final long maxBurstTimeMs = 10 * 1000L; // 10 seconds
        final long sleepTimeMs = 100L; // 100 ms

        for (int i = 0; i < numSimulations; i++) {
            System.out.println("Running simulation #" + i);

            service.runNewSimulation(totalSimulationTimeMs,
                    numThreads, numTasks,
                    minBurstTimeMs, maxBurstTimeMs, sleepTimeMs);

            System.out.println("Simulation results:"
                    + "\n" + "----------------------");
            service.printResults();

            System.out.println("\n");
        }

        System.out.println("----------------------");
        System.out.println("Exiting...");

        // If your program has not completed after the message printed above,
        // it means that some threads are not properly stopped! -> this issue will affect the grade
    }

    /**
     * Reset.
     */
    public void reset() {
        // TODO - remove any information from the previous simulation, if necessary

    }

    /**
     * Return random long.
     *
     * @param max the max
     * @param min the min
     * @return the long
     */
    public long returnRandom(long max, long min) {
        return (max + 1) + ((long) (rng.nextDouble() * (min - (max + 1))));
    }

    /**
     * Run new simulation.
     *
     * @param totalSimulationTimeMs the total simulation time ms
     * @param numThreads            the num threads
     * @param numTasks              the num tasks
     * @param minBurstTimeMs        the min burst time ms
     * @param maxBurstTimeMs        the max burst time ms
     * @param sleepTimeMs           the sleep time ms
     * @throws InterruptedException the interrupted exception
     */
// If the implementation requires your code to throw some exceptions,
    // you are allowed to add those to the signature of this method
    public void runNewSimulation(final long totalSimulationTimeMs,
                                 final int numThreads, final int numTasks,
                                 final long minBurstTimeMs, final long maxBurstTimeMs, final long sleepTimeMs) throws InterruptedException {
        reset();

        // TODO:
        // 1. Run the simulation for the specified time, totalSimulationTimeMs
        // 2. While the simulation is running, use a fixed thread pool with numThreads
        // (see https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Executors.html#newFixedThreadPool(int) )
        // to execute Tasks (implement the respective class, see above!)
        // 3. The total maximum number of tasks is numTasks, 
        // and each task has a burst time (duration) selected randomly
        // between minBurstTimeMs and maxBurstTimeMs (inclusive)
        // 4. The implementation should assign sequential task IDs to the created tasks (0, 1, 2...)
        // and it should assign them to threads in the same sequence (rather any other scheduling approach)
        // 5. When the simulation time is up, it should make sure to stop all of the currently executing
        // and waiting threads!


        executorservice = Executors.newFixedThreadPool(numThreads);

        services = new ArrayList<>();

        for (int i = 0; i < numTasks; i++) {
            Service task = new Service(i, returnRandom(maxBurstTimeMs, minBurstTimeMs), sleepTimeMs);
            services.add(task);
        }


        for (int i = 0; i < services.size(); i++) {
            executorservice.execute(services.get(i));
            serviceMap.put(i, services.get(i));

        }

        executorservice.shutdown();
        try {
            executorservice.awaitTermination(totalSimulationTimeMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Print header for the result.
     */
    public void printHeader() {
        System.out.println("*----*------------*-----------*-----------------------*");
        System.out.println("Service Id |  burst time |   start   |       finish   ");
        System.out.println("*----*------------*-----------*-----------------------*");
    }

    /**
     * Print footer for the result.
     */
    public void printFooter() {
        System.out.println("*----*------------*-----------*-------------*");
    }

    /**
     * Print execution results.
     */
    public void printResults() {
        // TODO:
        System.out.println("Completed tasks:");
        printHeader();
        // 1. For each *completed* task, print its ID, burst time (duration),
        // its start time (moment since the start of the simulation), and finish time
        for (Service t : serviceMap.values()) {

            if (t.isDone() && t.isWaiting()) {
                System.out.println("\t" + t.getService_ID() + "\t\t\t" + t.getBurst() + "\t\t" + t.getStart() + "\t\t" + t.getEnd());
            }
        }
        printFooter();
        System.out.println("Interrupted tasks:");
        // 2. Afterwards, print the list of tasks IDs for the tasks which were currently
        // executing when the simulation was finished/interrupted
        System.out.println("*---------*");
        System.out.println("Service Id");
        System.out.println("*---------*");
        for (Service t : serviceMap.values()) {
            if (t.isWaiting() && !t.isDone()) {

                System.out.println(t.getService_ID());

            }
        }
        System.out.println("*---------*");
        System.out.println("Waiting tasks:");
        // 3. Finally, print the list of tasks IDs for the tasks which were waiting for execution,
        // but were never started as the simulation was finished/interrupted
        System.out.println("*---------*");
        System.out.println("Service Id");
        System.out.println("*---------*");
        for (Service t : serviceMap.values()) {
            if (!t.isWaiting()) {

                System.out.println(t.getService_ID());
            }
        }
        System.out.println("*---------*");
    }

    /**
     * The type Service inherited of Runnable  to create a thread.
     * When the Runnable object is passed, its run method will be called, otherwise it will do nothing.
     */
// ... add further fields, methods, and even classes, if necessary
    public class Service implements Runnable {

        private final int service_ID;
        private final long burst;
        private final long rest;
        private boolean done = false;
        private boolean waiting = false;
        private LocalTime start = null;
        private LocalTime end = null;


        /**
         * Instantiates a new Service.
         *
         * @param ID    the id
         * @param burst the burst time
         * @param rest  the rest time
         */
        public Service(int ID, long burst, long rest) {
            this.service_ID = ID;
            this.burst = burst;
            this.rest = rest;
        }


        /**
         * Gets start.
         *
         * @return the start
         */
        public LocalTime getStart() {
            return start;
        }

        /**
         * Gets service id.
         *
         * @return the service id
         */
        public int getService_ID() {
            return service_ID;
        }

        /**
         * Gets burst.
         *
         * @return the burst
         */
        public long getBurst() {
            return burst;
        }

        /**
         * Gets end.
         *
         * @return the end
         */
        public LocalTime getEnd() {
            return end;
        }

        /**
         * Is done boolean.
         *
         * @return the boolean
         */
        public boolean isDone() {
            return done;
        }

        /**
         * Is waiting boolean.
         *
         * @return the boolean
         */
        public boolean isWaiting() {
            return waiting;
        }


        @Override
        public void run() {

            try {

                start = LocalTime.parse(getCurrentTimeStamp());


                waiting = true;
                long time = System.currentTimeMillis() + burst;

                do {
                    TimeUnit.MILLISECONDS.sleep(rest);
                }
                while (System.currentTimeMillis() < time);

                end = LocalTime.parse(getCurrentTimeStamp());
                done = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

        private String getCurrentTimeStamp() {
            Calendar calendar = Calendar.getInstance();
            long longValue = calendar.getTimeInMillis();

            LocalDateTime date =
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(longValue), ZoneId.systemDefault());
            String formattedString = date.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            return formattedString;
        }
    }
}