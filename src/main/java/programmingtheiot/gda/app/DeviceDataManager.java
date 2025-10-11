package programmingtheiot.gda.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import programmingtheiot.common.ConfigConst;
import programmingtheiot.common.ConfigUtil;
import programmingtheiot.common.IDataMessageListener;
import programmingtheiot.common.ResourceNameEnum;
import programmingtheiot.common.IActuatorDataListener;

import programmingtheiot.data.ActuatorData;
import programmingtheiot.data.SensorData;
import programmingtheiot.data.SystemPerformanceData;
import programmingtheiot.data.SystemStateData;

import programmingtheiot.gda.connection.CoapServerGateway;
import programmingtheiot.gda.connection.IPersistenceClient;
import programmingtheiot.gda.connection.IPubSubClient;
import programmingtheiot.gda.system.SystemPerformanceManager;

/**
 * Core data manager for the Gateway Device Application (GDA).
 * Implements IDataMessageListener for processing incoming messages.
 */
@SuppressWarnings("unused") // suppress unused field warnings
public class DeviceDataManager implements IDataMessageListener
{
    // Logger
    private static final Logger _Logger =
        Logger.getLogger(DeviceDataManager.class.getName());

    // Private fields
    private boolean enableMqttClient = true;
    private boolean enableCoapServer = false;
    private boolean enableCloudClient = false;
    private boolean enablePersistenceClient = false;
    private boolean enableSystemPerf = false;

    private IPubSubClient mqttClient = null;
    private IPubSubClient cloudClient = null;
    private IPersistenceClient persistenceClient = null;
    private CoapServerGateway coapServer = null;
    private SystemPerformanceManager sysPerfMgr = null;

    // Constructor
    public DeviceDataManager()
    {
        super();

        ConfigUtil configUtil = ConfigUtil.getInstance();

        this.enableMqttClient =
            configUtil.getBoolean(ConfigConst.GATEWAY_DEVICE, ConfigConst.ENABLE_MQTT_CLIENT_KEY);

        this.enableCoapServer =
            configUtil.getBoolean(ConfigConst.GATEWAY_DEVICE, ConfigConst.ENABLE_COAP_SERVER_KEY);

        this.enableCloudClient =
            configUtil.getBoolean(ConfigConst.GATEWAY_DEVICE, ConfigConst.ENABLE_CLOUD_CLIENT_KEY);

        this.enablePersistenceClient =
            configUtil.getBoolean(ConfigConst.GATEWAY_DEVICE, ConfigConst.ENABLE_PERSISTENCE_CLIENT_KEY);

        initManager();
    }

    // Private initialization
    private void initManager()
    {
        ConfigUtil configUtil = ConfigUtil.getInstance();

        this.enableSystemPerf =
            configUtil.getBoolean(ConfigConst.GATEWAY_DEVICE, ConfigConst.ENABLE_SYSTEM_PERF_KEY);

        if (this.enableSystemPerf) {
            this.sysPerfMgr = new SystemPerformanceManager();
            this.sysPerfMgr.setDataMessageListener(this); // important callback wiring
        }

        if (this.enableMqttClient) {
            // TODO: Implement MQTT client setup in Lab Module 7
        }

        if (this.enableCoapServer) {
            // TODO: Implement CoAP server setup in Lab Module 8
        }

        if (this.enableCloudClient) {
            // TODO: Implement cloud client setup in Lab Module 10
        }

        if (this.enablePersistenceClient) {
            // TODO: Implement persistence client setup (optional Lab Module 5)
        }
    }

    // Public methods
    public void startManager()
    {
        _Logger.info("Starting DeviceDataManager...");

        if (this.sysPerfMgr != null) {
            this.sysPerfMgr.startManager();
        }
    }

    public void stopManager()
    {
        _Logger.info("Stopping DeviceDataManager...");

        if (this.sysPerfMgr != null) {
            this.sysPerfMgr.stopManager();
        }
    }

    // IDataMessageListener implementations

    @Override
    public boolean handleActuatorCommandResponse(ResourceNameEnum resourceName, ActuatorData data)
    {
        if (data != null) {
            _Logger.info("Handling actuator command response: " + data.getName());

            if (data.hasError()) {
                _Logger.warning("Error flag set for ActuatorData instance.");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean handleActuatorCommandRequest(ResourceNameEnum resourceName, ActuatorData data)
    {
        _Logger.info("handleActuatorCommandRequest called for: " + (data != null ? data.getName() : "null"));
        return false; // stub for lab exercises
    }

    @Override
    public void setActuatorDataListener(String name, IActuatorDataListener listener)
    {
        _Logger.info("setActuatorDataListener called for: " + name);
        // stub for lab exercises
    }

    @Override
    public boolean handleIncomingMessage(ResourceNameEnum resourceName, String msg)
    {
        if (msg != null) {
            _Logger.info("Handling incoming message: " + msg);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleSensorMessage(ResourceNameEnum resourceName, SensorData data)
    {
        if (data != null) {
            _Logger.info("Handling sensor message: " + data.getName());

            if (data.hasError()) {
                _Logger.warning("Error flag set for SensorData instance.");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean handleSystemPerformanceMessage(ResourceNameEnum resourceName, SystemPerformanceData data)
    {
        if (data != null) {
            _Logger.info("Handling system performance message: " + data.getName());

            if (data.hasError()) {
                _Logger.warning("Error flag set for SystemPerformanceData instance.");
            }
            return true;
        }
        return false;
    }

    // Optional: handle system state data
    private void handleIncomingDataAnalysis(ResourceNameEnum resourceName, SystemStateData data)
    {
        _Logger.log(Level.FINE, "handleIncomingDataAnalysis() called for SystemStateData.");
    }

    private void handleIncomingDataAnalysis(ResourceNameEnum resourceName, ActuatorData data)
    {
        _Logger.log(Level.FINE, "handleIncomingDataAnalysis() called for ActuatorData.");
    }

    private boolean handleUpstreamTransmission(ResourceNameEnum resourceName, String jsonData, int qos)
    {
        _Logger.log(Level.FINE, "handleUpstreamTransmission() called.");
        return true;
    }
}
