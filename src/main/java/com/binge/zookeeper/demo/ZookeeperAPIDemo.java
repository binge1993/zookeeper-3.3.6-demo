package com.binge.zookeeper.demo;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @autor binge
 * @date 2018年2月14日
 */
public class ZookeeperAPIDemo {
	private static final String url = "192.168.99.100:2181";// zk 地址
	private static final int sessionTimeOut = 5000;// 会话超时时间。单位为ms
	/** 信号量，阻塞程序执行，用于等待zookeeper连接成功，发送成功信号 */
	static final CountDownLatch connectedSemaphore = new CountDownLatch(1);

	private static ZooKeeper getZookeeper() throws IOException, InterruptedException {

		ZooKeeper zk = new ZooKeeper(url, sessionTimeOut, new Watcher() {

			public void process(WatchedEvent event) {
				// 获取事件的状态
				KeeperState keeperState = event.getState();
				EventType eventType = event.getType();
				// 如果是建立连接
				if (KeeperState.SyncConnected == keeperState) {
					if (EventType.None == eventType) {
						// 如果建立连接成功，则发送信号量，让后续阻塞程序向下执行
						connectedSemaphore.countDown();
						System.out.println("zk 建立连接");
					}
				}
			}

		});

		// 进行阻塞
		connectedSemaphore.await();

		System.out.println("..");

		return zk;
	}

	public static void createPresisitentNode() throws IOException, KeeperException, InterruptedException {
		ZooKeeper zooKeeper = getZookeeper();
		zooKeeper.create("/root", "root data".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}

	public static void createPresisitentSequentialNode() throws IOException, KeeperException, InterruptedException {
		ZooKeeper zooKeeper = getZookeeper();
		zooKeeper.create("/root/app", "sequential data".getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT_SEQUENTIAL);
	}

	public static void createEphemeralNode() throws IOException, KeeperException, InterruptedException {
		ZooKeeper zooKeeper = getZookeeper();
		zooKeeper.create("/root/app1", "app1 data".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	}

	public static void setData() throws IOException, InterruptedException, KeeperException {
		ZooKeeper zooKeeper = getZookeeper();
		zooKeeper.setData("/root", "root data v3".getBytes(), 1);
	}

	public static void getData() throws IOException, InterruptedException, KeeperException {
		ZooKeeper zooKeeper = getZookeeper();
		Stat stat = new Stat();
		byte data[] = zooKeeper.getData("/root", false, stat);
		System.out.println("getData finish!" + new String(data));
	}

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		// ZookeeperAPIDemo.createPresisitentNode();
		// ZookeeperAPIDemo.createEphemeralNode();
		// ZookeeperAPIDemo.createPresisitentSequentialNode();
		// ZookeeperAPIDemo.setData();
		ZookeeperAPIDemo.getData();
		System.out.println("createNode finish!");
	}
}
