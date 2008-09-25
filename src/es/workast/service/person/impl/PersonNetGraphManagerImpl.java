package es.workast.service.person.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import es.workast.model.group.Group;
import es.workast.model.person.Person;
import es.workast.service.person.PersonNetGraphManager;
import es.workast.web.person.net.Net;
import es.workast.web.person.net.Node;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@Service
public class PersonNetGraphManagerImpl implements PersonNetGraphManager {

    private static final String ROOTTYPE = "ROOT";
    private static final String GROUPTYPE = "GROUP";
    private static final String GROUPPREFIX = "G";
    private static final String PERSONTYPE = "PERSON";
    private static final String PERSONPREFIX = "P";
    private static final String MEMBERCONNECTIONPREFIX = "MC";

    private static final String FOLLOWINGCONNECTIONTYPE = "FOLLOWING";
    private static final String FOLLOWINGCONNECTIONPREFIX = "FG";
    private static final String FOLLOWERCONNECTIONTYPE = "FOLLOWER";
    private static final String FOLLOWERCONNECTIONPREFIX = "FR";
    private static final String MEMBERCONNECTIONTYPE = "MEMBER";
    private static final String P2PCONNECTIONTYPE = "P2P";
    private static final String P2PCONNECTIONPREFIX = "P2P";
    private static final String GROUPCONNECTIONTYPE = "GROUP";
    private static final String GROUPCONNECTIONTPREFIX = "GC";

    private static final String GROUPNODETYPE = "GROUP_NODE";
    private static final String FOLLOWINGNODETYPE = "FOLLOWING_NODE";
    private static final String FOLLOWERSNODETYPE = "FOLLOWERS_NODE";

    private static final String HASPICTUREKEY = "hasPicture";

    // ---------- Services

    private final Log logger = LogFactory.getLog(getClass());

    /**
     * @see es.workast.service.person.PersonNetGraphManager#getPersonNet(es.workast.model.person.Person)
     */
    public final Net getPersonNet(Person person) {

        Map<Person, Node> persons = new HashMap<Person, Node>();
        Net net = new Net();

        Node root = net.addNode(ROOTTYPE, PERSONPREFIX + person.getId());
        persons.put(person, root);
        root.setTitle(person.getDisplayName());
        root.setFixed(true);
        root.addData(HASPICTUREKEY, person.isHasPicture());

        // Get Followers
        Node followersNode = net.addNode(FOLLOWERSNODETYPE, "followers");
        root.addConnection("FOLLOWERS", "followers", followersNode);
        addConnections(net, persons, followersNode, person.getFollowers(), FOLLOWERCONNECTIONTYPE, FOLLOWERCONNECTIONPREFIX);

        // Get Following
        Node followingsNode = net.addNode(FOLLOWINGNODETYPE, "followings");
        root.addConnection("FOLLOWINGS", "folllowings", followingsNode);
        addConnections(net, persons, followingsNode, person.getFollowing(), FOLLOWINGCONNECTIONTYPE, FOLLOWINGCONNECTIONPREFIX);

        // Get Groups
        Node groupsNode = net.addNode(GROUPNODETYPE, "groups");
        followingsNode.addConnection("GROUPS", "groups", groupsNode);

        Set<Group> groups = person.getGroups();
        for (Group group : groups) {
            if (group.getId() != Group.GLOBAL_GROUP_ID) {
                Node groupNode = net.addNode(GROUPTYPE, GROUPPREFIX + group.getId());
                groupNode.setTitle(group.getName());
                groupsNode.addConnection(GROUPCONNECTIONTYPE, GROUPCONNECTIONTPREFIX + group.getId(), groupNode);
                Set<Person> members = group.getMembers();
                for (Person member : members) {
                    if (!member.getId().equals(person.getId())) {
                        Node memberNode = net.addNode(PERSONTYPE, PERSONPREFIX + member.getId());
                        persons.put(member, memberNode);
                        memberNode.setTitle(member.getDisplayName());
                        memberNode.addData(HASPICTUREKEY, member.isHasPicture());
                        groupNode.addConnection(MEMBERCONNECTIONTYPE, MEMBERCONNECTIONPREFIX + member.getId(), memberNode);
                    }
                }
            }
        }

        // Persons connecting persons...
        for (Entry<Person, Node> entry : persons.entrySet()) {
            Person gPerson = entry.getKey();
            if (!person.equals(gPerson)) {
                Node node = entry.getValue();
                for (Person fp : gPerson.getFollowers()) {
                    if (!person.equals(fp) && persons.containsKey(fp)) {
                        Node destNode = persons.get(fp);
                        node.addConnection(P2PCONNECTIONTYPE, P2PCONNECTIONPREFIX + node.getId() + destNode.getId(), destNode);
                    }
                }
            }
        }

        return net;
    }

    /**
     * Bolk node adds
     * 
     * @param net
     * @param persons
     * @param root
     * @param collection
     * @param connectionType
     * @param connectionPrefix
     */
    private void addConnections(Net net, Map<Person, Node> persons, Node root, Set<Person> collection, String connectionType, String connectionPrefix) {
        for (Person person : collection) {
            Node node = net.addNode(PERSONTYPE, PERSONPREFIX + person.getId());
            persons.put(person, node);
            node.setTitle(person.getDisplayName());
            node.addData(HASPICTUREKEY, person.isHasPicture());
            root.addConnection(connectionType, connectionPrefix + person.getId(), node);
        }
    }

}
