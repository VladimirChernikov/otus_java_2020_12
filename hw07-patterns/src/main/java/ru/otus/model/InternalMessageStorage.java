package ru.otus.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InternalMessageStorage implements MessageStorage {

    private final List< Message > history;

    public InternalMessageStorage() {
        history = Collections.synchronizedList( new ArrayList<>() );
    }

    public void store( Message oldMsg, Message newMsg ) {
        synchronized ( history ){
            history.add( oldMsg );
            history.add( newMsg );
        }
    }

    @Override
    public String toString() {
        return super.toString() + " " + history;
    }

}
