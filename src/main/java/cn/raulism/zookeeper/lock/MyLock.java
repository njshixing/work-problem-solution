package cn.raulism.zookeeper.lock;

public interface MyLock {
    void getLock(String name) throws InterruptedException;

    void unlock() throws InterruptedException;
}
