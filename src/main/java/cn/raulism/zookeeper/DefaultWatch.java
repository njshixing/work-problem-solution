package cn.raulism.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

public class DefaultWatch implements Watcher {
    private CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void process(WatchedEvent event) {
        Event.KeeperState state = event.getState();
        switch (state) {
            case Disconnected:
                System.out.println("Disconnected...c...new...");
                break;
            case SyncConnected:
                System.out.println("Connected...c...ok...");
                latch.countDown();
                break;
        }
    }
}
