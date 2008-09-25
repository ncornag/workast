package es.workast.web.person.net;


/**
 * @author Nicolás Cornaglia
 */
public class Connection {

    private String type;
    private String id;
    private String destinationId;

    public Connection() {
    }

    public Connection(String type, String id, Node destinationNode) {
        this.type = type;
        this.id = id;
        this.destinationId = destinationNode.getId();
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getDestinationId() {
        return destinationId;
    }

}
