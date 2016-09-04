package com.fancytank.gamegen;

import com.google.gson.Gson;

import java.io.*;

public class DataManager {
    private static String absolutePath = "F:\\fancy\\inżynierka\\LuxProjector\\LuxProjector\\";

    public static SaveInstance loadFile(String projectName) {
        try {
            File file = new File(absolutePath, projectName);
            FileInputStream fis = new FileInputStream(file.getAbsoluteFile());
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            Gson gson = new Gson();
            SaveInstance si = gson.fromJson(json, SaveInstance.class);
            return si;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
