package q4WebTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.runners.Parameterized;
import org.junit.runners.model.RunnerScheduler;

/**
 * Class used from the following source:
 * http://jankesterblog.blogspot.com/2011/10
 * /junit4-running-parallel-junit-classes.html
 * 
 * @author Jan Kester
 * 
 */
public class Parallelized extends Parameterized {

    private static class ThreadPoolScheduler implements RunnerScheduler {
        private ExecutorService executor;

        public ThreadPoolScheduler() {
            String threads = System.getProperty("junit.parallel.threads", "16");
            int numThreads = Integer.parseInt(threads);
            executor = Executors.newFixedThreadPool(numThreads);
        }

        public void finished() {
            executor.shutdown();
            try {
                executor.awaitTermination(12, TimeUnit.HOURS);
            } catch (InterruptedException exc) {
                throw new RuntimeException(exc);
            }
        }

        public void schedule(Runnable childStatement) {
            executor.submit(childStatement);
        }
    }

    /**
     * Instantiates a new parallelized.
     * 
     * @param klass
     *            the klass
     * @throws Throwable
     *             the throwable
     */
    public Parallelized(Class<?> klass) throws Throwable {
        super(klass);
        setScheduler(new ThreadPoolScheduler());
    }
}