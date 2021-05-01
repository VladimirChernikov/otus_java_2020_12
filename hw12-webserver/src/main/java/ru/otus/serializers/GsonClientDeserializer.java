package ru.otus.serializers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

public class GsonClientDeserializer implements JsonDeserializer<Client>
{
    private static final Logger log = LoggerFactory.getLogger(GsonClientDeserializer.class);

    @Override
    public Client deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();

        log.trace("deserialize object = {}", obj);
        Client client = new Client();
        for ( var entry : obj.entrySet() )  {
            log.trace("deserialize entry = {}", entry);
            switch (entry.getKey()) {
                case "name": 
                    client.setName( entry.getValue().getAsString() );
                    break;
                case "address": 
                    client.setAddress( new Address( entry.getValue().getAsString() ) );
                    break;
                case "phone": 
                    final List<Phone> phones = new ArrayList<>();
                    entry.getValue().getAsJsonArray().forEach(phone -> phones.add( new Phone(phone.getAsString()) ));
                    client.setPhone( phones );
                    break;
                default:
                    break;
            }
        }
        return client;
    }
}

