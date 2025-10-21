package Network;

import CollectionObject.Route;

import java.io.Serializable;
import java.util.Stack;

public class Response implements Serializable {
    private String message;
    private Stack<Route> collection;
    private boolean userAuthentication;
    private static final long serialVersionUID = 1L;
    public Response(String message, Stack<Route> collection) {
        this.message = message;
        this.collection= collection;
    }

    public Response(String message, boolean userAuthentication) {
        this.message = message;
        this.userAuthentication = userAuthentication;
    }

    public String getMessage() {
        return message;
    }

    public Stack<Route> getCollection() {
        return collection;
    }

    public boolean isUserAuthenticated() {
        return userAuthentication;
    }
}