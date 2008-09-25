package es.workast.web.person.net;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Node {

    // ---------- Properties

    private String type;
    private String id;
    private String title;
    private boolean fixed = false;

    private HashMap<String, Object> data = new HashMap<String, Object>();
    private Map<String, Connection> connections = new HashMap<String, Connection>();

    // ---------- Constructors

    public Node() {
    }

    public Node(String type, String id) {
        this.type = type;
        this.id = id;
    }

    // ---------- Methods

    /**
     * Adds a connection to the Node
     * 
     * @param type
     * @param id
     * @param destinationNode
     * @return
     */
    public Node addConnection(String type, String id, Node destinationNode) {
        Connection connection = new Connection(type, id, destinationNode);
        connections.put(id, connection);
        return this;
    }

    /**
     * Adds a generic information, generally used to render in client
     * 
     * @param key
     * @param value
     * @return
     */
    public Node addData(String key, Object value) {
        data.put(key, value);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Node test = (Node) o;

        if (test.getId() != this.getId()) {
            return false;
        }

        return true;
    }

    // ---------- Accessors

    /**
     * Serialized to JSON values
     * 
     * @return JSON string
     */
    public String getData() {
        String result = null;
        for (String key : data.keySet()) {
            Object value = data.get(key);
            if (result == null) {
                result = "{";
            }
            result = result + key + ":" + value + ",";
        }
        if (result != null) {
            result = result.substring(0, result.length() - 1) + "}";
        }
        return result;
    }

    public Collection<Connection> getConnections() {
        if (connections.size() > 0) {
            return connections.values();
        } else {
            return null;
        }
    }

    public boolean isFixed() {
        return fixed;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

}