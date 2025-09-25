package programmingtheiot.gda.system;

import programmingtheiot.common.IDataMessageListener;
import programmingtheiot.common.ConfigConst;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Manages system performance monitoring by retrieving CPU and Memory utilization.
 */
public class SystemPerformanceManager
{
    // Logger
    private static final Logger _Logger =
        Logger.getLogger(SystemPerformanceManager.class.getName());

    // Private variables
    private int pollRate = ConfigConst.DEFAULT_POLL_CYCLES;
    private IDataMessageListener dataMsgListener = null;

    private ScheduledExecutorService schedExecSvc = null;
    private SystemCpuUtilTask sysCpuUtilTask = null;
    private SystemMemUtilTask sysMemUtilTask = null;

    private Runnable taskRunner = null;
    private boolean isStarted = false;

    // Constructor
    public SystemPerformanceManager()
    {
        // Set poll rate to 30 seconds to match professor's example
        this.pollRate = 30;

        this.schedExecSvc   = Executors.newScheduledThreadPool(1);
        this.sysCpuUtilTask = new SystemCpuUtilTask();
        this.sysMemUtilTask = new SystemMemUtilTask();

        this.taskRunner = () -> {
            this.handleTelemetry();
        };
    }

    // Retrieve CPU and Memory telemetry
    public void handleTelemetry()
    {
        float cpuUtil = this.sysCpuUtilTask.getTelemetryValue();
        float memUtil = this.sysMemUtilTask.getTelemetryValue();

        // Log at INFO level so messages are visible
        _Logger.info("CPU utilization: " + cpuUtil + ", Mem utilization: " + memUtil);
    }

    public void setDataMessageListener(IDataMessageListener listener)
    {
        this.dataMsgListener = listener;
        _Logger.info("Data message listener set: " + listener);
    }

    // Start scheduled monitoring
    public boolean startManager()
    {
        if (!this.isStarted) {
            _Logger.info("SystemPerformanceManager is starting...");

            ScheduledFuture<?> futureTask =
                this.schedExecSvc.scheduleAtFixedRate(
                    this.taskRunner,
                    1L,             // initial delay 1 second
                    this.pollRate,  // repeat every 30 seconds
                    TimeUnit.SECONDS
                );

            this.isStarted = true;
        } else {
            _Logger.info("SystemPerformanceManager is already started.");
        }

        return this.isStarted;
    }

    // Stop scheduled monitoring
    public boolean stopManager()
    {
        this.schedExecSvc.shutdown();
        this.isStarted = false;

        _Logger.info("SystemPerformanceManager is stopped.");
        return true;
    }
}
