package com.example.lx.daygram;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by lx on 2016/9/23.
 */
public class DayGramJSONSerializer {
    private Context mContext;
    private String mFileName;

    public DayGramJSONSerializer(Context c, String f) {
        mContext = c;
        mFileName = f;
    }

    public ArrayList<Diary> loadDiaries() throws IOException, JSONException {
        ArrayList<Diary> diaries = new ArrayList<Diary>();
        BufferedReader reader = null;
        try {
            //
            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //
                jsonString.append(line);
            }
            //
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue();
            //
            for (int i = 0; i < array.length(); i++)
                diaries.add(new Diary(array.getJSONObject(i)));
        } catch (FileNotFoundException e) {
            //
        } finally {
            if (reader != null)
                reader.close();
        }
        return diaries;
    }

    public void saveDiaries(ArrayList<Diary> diaries) throws JSONException, IOException {
        // Build an array in JSON
        JSONArray array = new JSONArray();
        for (Diary d : diaries)
            array.put(d.toJSON());

        // Write the file to disk
        Writer writer = null;
        try {
            OutputStream out = mContext
                    .openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
