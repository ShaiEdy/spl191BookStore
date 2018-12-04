package bgu.spl.mics;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class JsonReader {

    public JsonReader() {
    }

    public JsonObject readFile(){
        JsonParser jsonParser = new JsonParser();
        //InputStream inputStream = BookStoreRunner.class.getClassLoader().getResourceAsStream("src/main/input.json");
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("input.json");
        Reader reader = new InputStreamReader(inputStream);
        JsonElement element = jsonParser.parse(reader);
        JsonObject jsonObject = element.getAsJsonObject();
        //JsonObject booksArray = jsonObject.getAsJsonObject("initialInventory");
        return jsonObject;
    }
}
