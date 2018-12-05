package bgu.spl.mics;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

public class JsonReader {

    public JsonReader() {
    }

    public JsonObject readFile(){
        JsonParser jsonParser = new JsonParser();
        File file = new File("src/input.json");
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            inputStream = null;
        }
        
        Reader reader = new InputStreamReader(inputStream);
        JsonElement element = jsonParser.parse(reader);
        JsonObject jsonObject = element.getAsJsonObject();
        return jsonObject;
    }
}
