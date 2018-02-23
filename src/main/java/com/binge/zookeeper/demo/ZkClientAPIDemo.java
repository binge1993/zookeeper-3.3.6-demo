package com.binge.zookeeper.demo;

import org.I0Itec.zkclient.ZkClient;

/**
 * @autor binge
 * @date 2018年2月23日
 */
public class ZkClientAPIDemo {

	// server list
	private static final String url = "192.168.99.100:2181";

	private static ZkClient getZkClient() {
		return new ZkClient(url);
	}

	public static void createPersistentNode() {
		ZkClient zkClient = getZkClient();
		zkClient.createPersistent("/root");
	}
}
