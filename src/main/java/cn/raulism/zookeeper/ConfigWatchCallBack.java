package cn.raulism.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.Map;

/**
 * 配置变更回调类
 *
 * @author shixing
 */
//@Component
public class ConfigWatchCallBack implements Watcher, AsyncCallback.DataCallback, AsyncCallback.StatCallback {
    private ZooKeeper zk = ZKUtils.getZooKeeper();
    private ConfigFile configFile;
    private String watchPath;

    public void await() {
        zk.exists(watchPath, this, this, "exist");
    }

    @Override
    public void process(WatchedEvent event) {
        String path = event.getPath();
        Event.EventType type = event.getType();
        switch (type) {
            case None:
                break;
            case NodeCreated:
                System.out.println("node " + path + " created");
                zk.getData(path, this, this, "node created");
                break;
            case NodeDeleted:
                System.out.println("node " + path + " deleted");
                Map<String, String> configMap = configFile.getNodeMap();
                configMap.put(path, "");
                configFile.setNodeMap(configMap);
                zk.exists(watchPath, this, this, "exist");
                break;
            case NodeDataChanged:
                System.out.println("node " + path + " changed");
                zk.getData(path, this, this, "node changed");
                break;
            case NodeChildrenChanged:
                break;/**/
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        // 如果data不为空，则写入配置
        if (null != data) {
            Map<String, String> configMap = configFile.getNodeMap();
            configMap.put(path, new String(data));
            configFile.setNodeMap(configMap);
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (null != stat) {
            zk.getData(path, this, this, "node exist");
        }
    }

    public ConfigFile getConfigFile() {
        return configFile;
    }

    public void setConfigFile(ConfigFile configFile) {
        this.configFile = configFile;
    }

    public String getWatchPath() {
        return watchPath;
    }

    public void setWatchPath(String watchPath) {
        this.watchPath = watchPath;
    }
}
