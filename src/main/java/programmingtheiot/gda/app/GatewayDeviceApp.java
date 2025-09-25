package programmingtheiot.gda.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import programmingtheiot.gda.system.SystemPerformanceManager;

/**
 * Main GDA application.
 */
public class GatewayDeviceApp
{
    // static logger
    private static final Logger _Logger =
        Logger.getLogger(GatewayDeviceApp.class.getName());

    // class-scoped vars
    private SystemPerformanceManager sysPerfMgr = null;

    /**
     * Package-scoped constructor
     * 
     * @param args Command line arguments
     */
    public GatewayDeviceApp(String[] args)
    {
        super();

        _Logger.info("Initializing GDA...");

        // Call argument parser (ignores args for now)
        parseArgs(args);

        // Instantiate SystemPerformanceManager
        this.sysPerfMgr = new SystemPerformanceManager();
    }

    /**
     * Starts the application.
     */
    public void startApp()
    {
        _Logger.info("Starting GDA...");

        try {
            if (this.sysPerfMgr.startManager()) {
                _Logger.info("GDA started successfully.");
            } else {
                _Logger.warning("Failed to start system performance manager!");
                stopApp(-1);
            }
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Failed to start GDA. Exiting.", e);
            stopApp(-1);
        }
    }

    /**
     * Stops the application.
     * 
     * @param code Exit code
     */
    public void stopApp(int code)
    {
        _Logger.info("Stopping GDA...");

        try {
            if (this.sysPerfMgr.stopManager()) {
                _Logger.log(Level.INFO, "GDA stopped successfully with exit code {0}.", code);
            } else {
                _Logger.warning("Failed to stop system performance manager!");
            }
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Failed to cleanly stop GDA. Exiting.", e);
        }

        System.exit(code);
    }

    /**
     * Initialize configuration.
     * 
     * @param fileName Configuration file name
     */
    private void initConfig(String fileName)
    {
        _Logger.info("Attempting to load configuration: " +
            (fileName == null ? "Default." : fileName));
    }

    /**
     * Parse command line arguments (currently ignored).
     * 
     * @param args Command line arguments
     */
    private void parseArgs(String[] args)
    {
        _Logger.info("No command line args to parse.");
        initConfig(null);
    }

    /**
     * Main application entry point.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args)
    {
        GatewayDeviceApp gwApp = new GatewayDeviceApp(args);

        gwApp.startApp();

        try {
            Thread.sleep(65000L); // run for ~65 seconds
        } catch (InterruptedException e) {
            // ignore
        }

        gwApp.stopApp(0);
    }
}
