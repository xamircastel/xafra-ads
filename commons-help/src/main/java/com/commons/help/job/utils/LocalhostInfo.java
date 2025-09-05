package com.commons.help.job.utils;

import java.net.InetAddress;

public class LocalhostInfo {

	public static final LocalhostInfo instance = new LocalhostInfo();

	private final InetAddress localhost = getLocalhost();

	private final String hostName = this.localhost.getHostName();

	private final String ip = this.localhost.getHostAddress();

	public String getIp() {
		return this.ip;
	}

	public String getHostName() {
		return this.hostName;
	}

	public InetAddress getLocalhost() {
		InetAddress localhostAddress = null;
		try {
			localhostAddress = InetAddress.getLocalHost();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return localhostAddress;
	}

	public static final double getRuntimeFreeMemInPerc() {
		Runtime runtime = Runtime.getRuntime();
		double total = runtime.totalMemory();
		double free = runtime.freeMemory();
		return free / total * 100.0D;
	}

	public static final double getRuntimeUsedMemInPerc() {
		Runtime runtime = Runtime.getRuntime();
		double total = runtime.totalMemory();
		double free = runtime.freeMemory();
		double used = total - free;
		return used / total * 100.0D;
	}
}