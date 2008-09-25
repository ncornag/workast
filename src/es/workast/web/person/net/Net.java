package es.workast.web.person.net;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public class Net {

    private Map<String, Node> nodes = new HashMap<String, Node>();

    public Node addNode(String type, String id) {
        Node node = nodes.get(id);
        if (node == null) {
            node = new Node(type, id);
        }
        nodes.put(node.getId(), node);
        return node;
    }

    public Node getNode(String id) {
        return nodes.get(id);
    }

    public Collection<Node> getNodes() {
        return nodes.values();
    }

}
