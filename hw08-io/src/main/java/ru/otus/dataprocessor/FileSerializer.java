package ru.otus.dataprocessor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;

public class FileSerializer implements Serializer {

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл
        JsonObject jsonObject = mapToJsonObject(data);
        try ( var fileOutputStream = new FileOutputStream( this.fileName ) ) {
            fileOutputStream.write( jsonObject.toString().getBytes( Charset.forName("UTF-8") ) );
        } catch (IOException e) {
            throw new FileProcessException( new RuntimeException( String.format("Could not write into file  '%s'", this.fileName ) ) );
		}
    }

    private JsonObject mapToJsonObject( Map<String, Double> data ) {
        var jsonObjectBuilder = Json.createObjectBuilder();
        for (var entry : data.entrySet() )  {
            jsonObjectBuilder.add( entry.getKey(), entry.getValue() );
        }
        var jsonObject = jsonObjectBuilder.build();
        return jsonObject;
    }

}
