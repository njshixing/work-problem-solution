package cn.raulism.zookeeper.lock;

import cn.raulism.zookeeper.ZKUtils;
import org.apache.zookeeper.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DistributeLock implements MyLock, AsyncCallback.StringCallback, AsyncCallback.ChildrenCallback, Watcher {
    private CountDownLatch latch = new CountDownLatch(1);
    private ZooKeeper zk = ZKUtils.getZooKeeper();
    private String lockName;

    @Override
    public void getLock(String name) throws InterruptedException {
        zk.create("/lock/lock", name.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, this, name);
        latch.await();
    }

    @Override
    public void unlock() throws InterruptedException {
        try {
            System.out.println("delete " + "/lock/" + lockName);
            zk.delete("/lock/" + lockName, -1);
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        // 如果创建成功，则查看子节点数据，排序，排名，等待前一名结束
        zk.getChildren("/lock", false, this, ctx);
        lockName = name.replace("/lock/", "");
    }

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children) {
        if (null == children) {
            System.out.println(path + " no children");
        } else {
            // 排序
            Collections.sort(children);
            System.out.println(Arrays.toString(children.toArray()));
            // 看排名
            int index = children.indexOf(lockName);
            // 如果是第一名，则执行任务
            if (index < 1) {
                System.out.println(lockName + " first");
                try {
                    zk.setData(path, lockName.getBytes(), -1);
                    latch.countDown();
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                // 找到前一个，进行监控
                String prefix = children.get(index - 1);
                try {
                    zk.exists("/lock/" + prefix, this);
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void process(WatchedEvent event) {
        Event.EventType type = event.getType();
        switch (type) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                zk.getChildren("/lock", this, this, "ctx");
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
        }
    }
}
