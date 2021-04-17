package ru.otus.dataprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import ru.otus.model.Measurement;

public class FileLoader implements Loader {

    private final String fileName;

    public FileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        List<Measurement> result = new ArrayList<>();
        try (var jsonReader = Json.createReader(FileLoader.class.getClassLoader().getResourceAsStream( this.fileName ))) {
            JsonStructure jsonFromTheFile = jsonReader.read();
            if ( jsonFromTheFile.getValueType() == ValueType.ARRAY ) {
                for (JsonValue val : (JsonArray)jsonFromTheFile) {
                    if ( val.getValueType() == ValueType.OBJECT ) {
                        result.add( convertJsonObjectToMeasurement( (JsonObject)val ) );
                    }
                }
            }
            else {
                throw new FileProcessException( new RuntimeException( String.format("File '%s' should contain a json array", this.fileName ) ) );
            }
        }
        return result;
    }

    private Measurement convertJsonObjectToMeasurement( JsonObject jsonObject ) {
        Measurement result = null;
         String name = null;
         Double value = null;
        for (Map.Entry<String, JsonValue> entry : jsonObject.entrySet()) {
            switch ( entry.getKey() )  {
                case "name": 
                    name = ((JsonString)entry.getValue()).getString();
                    break;
                case "value": 
                    value = ((JsonNumber)entry.getValue()).doubleValue();
                    break;
                default:
                    throw new FileProcessException( new RuntimeException( String.format("Object '%s' contains an unsupported field: '%s'", jsonObject, entry.getKey()) ) );
            }
        }
        result = new Measurement( name, value );
        return result;
    }

}
