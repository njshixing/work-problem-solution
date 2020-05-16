package cn.raulism.zookeeper;

import java.util.Map;

public class ConfigFile {
    private Map<String, String> nodeMap;

    public Map<String, String> getNodeMap() {
        return nodeMap;
    }

    public void setNodeMap(Map<String, String> nodeMap) {
        this.nodeMap = nodeMap;
    }
}
