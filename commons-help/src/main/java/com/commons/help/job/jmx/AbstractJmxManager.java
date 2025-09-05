package com.commons.help.job.jmx;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public abstract class AbstractJmxManager {
	public enum JmxProps {
		DEFAULT_MONITOR_USER("monitorRole"), 
		DEFAULT_MONITOR_PASS("clouve3t"), 
		DEFAULT_CONSTROL_USER("controlRole"),
		DEFAULT_CONSTROL_PASS("groud1ou"), 
		DEFAULT_JMX_PORT("9111"), 
		DEFAULT_JMX_PORT_BACKUP("9112");

		private final String propValue;

		JmxProps(String propValue) {
			this.propValue = propValue;
		}

		public String getPropValue() {
			return this.propValue;
		}
	}

	public <T> boolean createMBean(String objectNameName, Class<T> mBeanInterfaceClass, T mBeanImplInstance) {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		boolean mbeanCreated = false;
		try {
			ObjectName objectName = new ObjectName(objectNameName);
			if (!mbs.isRegistered(objectName)) {
				ObjectInstance instance = mbs.registerMBean(mBeanImplInstance, objectName);
				if (instance != null) {
					mbeanCreated = true;
					logInfo("MBean for type " + mBeanInterfaceClass + " created with name " + objectNameName);
				}
			} else {
				logInfo("MBean for type " + mBeanInterfaceClass + " already registered with name " + objectNameName);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return mbeanCreated;
	}

	public boolean unregisterMBean(String objectNameName) {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		boolean mbeanUnregistered = false;
		try {
			ObjectName objectName = new ObjectName(objectNameName);
			if (mbs.isRegistered(objectName)) {
				mbs.unregisterMBean(objectName);
				mbeanUnregistered = true;
				logInfo("MBean for objectName name " + objectNameName + " unregistered.");
			} else {
				logInfo("MBean for objectName name " + objectNameName + " is not registered.");
			}
		} catch (Exception e) {
			logError(e);
		}
		return mbeanUnregistered;
	}

	public ConcurrentMap<String, JMXConnector> subscribeToNotificationSingleTime(String hostName, String port,
			ConcurrentMap<String, JMXConnector> hostNameConnectorMap, NotificationListener notificationListener,
			String mBeanObjectNamePath, String key) throws IOException {
		if (!hostNameConnectorMap.containsKey(key)) {
			JMXConnector jmxConnector = getJMXConnector(hostName, port);
			if (jmxConnector != null) {
				MBeanServerConnection mbsc = jmxConnector.getMBeanServerConnection();
				ObjectName remoteObject = getRemoteObject(mBeanObjectNamePath);
				if (remoteObject != null && mbsc.isRegistered(remoteObject)) {
					addNotificationToServerConnection(mbsc, remoteObject, notificationListener);
					hostNameConnectorMap.put(key, jmxConnector);
				}
			} else {
				logInfo("Not able to connect jmxConnector to host: " + hostName + " and port: " + port);
			}
		}
		return hostNameConnectorMap;
	}

	public ObjectName getRemoteObject(String mBeanObjectNamePath) {
		ObjectName remoteObject = null;
		try {
			remoteObject = ObjectName.getInstance(mBeanObjectNamePath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return remoteObject;
	}

	public void addNotificationToServerConnection(MBeanServerConnection mbsc, ObjectName remoteObject,
			NotificationListener notificationListener) {
		try {
			mbsc.addNotificationListener(remoteObject, notificationListener, (NotificationFilter) null, (Object) null);
		} catch (Exception e) {
			logError(e);
		}
	}

	public JMXConnector getJMXConnector(String hostName, String port) {
		JMXConnector jmxConnector = null;
		try {
			jmxConnector = JMXConnectorFactory.connect(getJMXServiceUrl(hostName, port),
					getControlRoleCredentialsMap());
		} catch (Exception e) {
			logError("Error treatement...", e);
		}
		return jmxConnector;
	}

	public JMXServiceURL getJMXServiceUrl(String hostName, String port) {
		JMXServiceURL serviceUrl = null;
		try {
			serviceUrl = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + hostName + ":" + port + "/jmxrmi");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return serviceUrl;
	}

	public Map<String, String[]> getControlRoleCredentialsMap() {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Map<String, String[]> credentialsMap = (Map) new HashMap<>();
		String[] credentials = { JmxProps.DEFAULT_CONSTROL_USER.getPropValue(),
				JmxProps.DEFAULT_CONSTROL_PASS.getPropValue() };
		credentialsMap.put("jmx.remote.credentials", credentials);
		return credentialsMap;
	}

	public abstract void logError(String paramString, Exception paramException);

	public abstract void logError(Exception paramException);

	public abstract void logInfo(String paramString);
}
