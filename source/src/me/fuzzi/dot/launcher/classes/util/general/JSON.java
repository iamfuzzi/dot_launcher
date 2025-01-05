package me.fuzzi.dot.launcher.classes.util.general;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSON {

    // Получить значение параметра
    public String getValue(String text, String parameter) {
        JSONObject jsonObject = new JSONObject(text); // Объект JSON из текста

        if (jsonObject.has(parameter)) { // Если содержит parameter, то
            return jsonObject.getString(parameter); // Вернуть его значение
        } else {
            return null;
        }
    }

    public String extractUrl(String text, String object, String parameter) {
        JSONObject jsonObject = new JSONObject(text); // Объект JSON из текста

        JSONObject assetIndex = jsonObject.getJSONObject(object);

        return assetIndex.getString(parameter);
    }


    // Получить значение параметра из объекта
    public String getValueFromObject(String text, String object1, String object2, String parameter) {
        JSONObject jsonObject = new JSONObject(text); // Объект JSON из текста

        if (jsonObject.has(object1)) { // Если объект имеет object1
            JSONObject json1 = jsonObject.getJSONObject(object1);

            if (json1.has(object2)) { // Если объект имеет object2
                JSONObject json2 = json1.getJSONObject(object2);

                if (json2.has(parameter)) { // Если объект имеет параметр, то
                    return json2.getString(parameter); // Вернуть его значение
                }
            }
        }
        return null;
    }

    // Получить значение параметра из массива
    public String getValueArray(String text, String array, String parameter) {
        JSONObject jsonObject = new JSONObject(text); // Объект JSON из текста

        if (jsonObject.has(array)) {
            JSONArray jsonarray = jsonObject.getJSONArray(array); // Создаем массив array для поиска в нем parameter

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject property = jsonarray.getJSONObject(i);

                if (property.has(parameter)) { // Если содержит parameter, то
                    return property.getString(parameter); // Вернуть его значение
                }
            }
        }
        return null;
    }

    // Получить значение Minecraft-версии
    public String getVersion(String text, String id) {
        JSONObject jsonObject = new JSONObject(text); // Объект JSON из текста
        JSONArray versions = jsonObject.getJSONArray("versions"); // Массив версий

        for (int i = 0; i < versions.length(); i++) { // Перебрать каждую версию
            JSONObject version = versions.getJSONObject(i);
            if (version.getString("id").equals(id)) { // Если версия равна нужной, то
                return version.getString("url"); // Вернуть значение url
            }
        }
        return null;
    }
}