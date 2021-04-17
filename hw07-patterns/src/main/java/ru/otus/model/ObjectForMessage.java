package ru.otus.model;

import java.util.List;

public class ObjectForMessage {
    private final List<String> data;

    private ObjectForMessage( List<String> data ) {
        this.data = data;
    } 

    public List<String> getData() {
        return data;
    }

    // public void setData(List<String> data) {
    //     this.data = data;
    // }

    public ObjectForMessage copy() {
        return this.toBuilder().build();
    }

    public Builder toBuilder() {
        return new Builder( this.data );
    }

    @Override
    public String toString() {
        return this.data.toString();
    }

    public static class Builder {

        private  List<String> data;

        public Builder() {
        }

        private Builder( List<String> data ) {
            this.data( data );
        }

        public Builder data( List<String> data ) {
            this.data = List.copyOf( data );
            return this;
        }

        public ObjectForMessage build() {
            return new ObjectForMessage( data );
        }
    }


}
