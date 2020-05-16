package cn.raulism.zookeeper;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class ZKUtils {
    private static final ZKConf zookeeperConf = new ZKConf();
    private static ZooKeeper zk;

    static {
        zookeeperConf.setAddress("192.168.254.10:2181,192.168.254.12:2181,192.168.254.13:2181,192.168.254.14:2181");
        zookeeperConf.setSessionTime(2000);
        try {
            zk = new ZooKeeper(zookeeperConf.getAddress(), zookeeperConf.getSessionTime(), new DefaultWatch());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ZooKeeper getZooKeeper() {
        return zk;
    }
}
