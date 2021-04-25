package ru.otus.serializers;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.model.Client;

public class GsonClientSerializer implements JsonSerializer<Client>
{
    private static final Logger log = LoggerFactory.getLogger(GsonClientSerializer.class);

	@Override
	public JsonElement serialize(Client src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonClient = new JsonObject();

        jsonClient.addProperty("id", src.getId() );
        jsonClient.addProperty("name", src.getName() );
        jsonClient.addProperty("address", src.getAddress().getStreet() );

        final JsonArray jsonPhones = new JsonArray();
        src.getPhone().forEach( phone -> jsonPhones.add(phone.getNumber()) );
        jsonClient.add("phone", jsonPhones );

        return jsonClient;
	}
}

