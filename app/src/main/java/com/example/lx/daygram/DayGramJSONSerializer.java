package com.example.lx.daygram;

import android.content.Context;
import android.util.Log;

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
import java.util.EnumMap;
import java.util.List;

/**
 * Created by lx on 2016/9/23.
 * class :JSONSerializer
 * This class can save diaries or load diaries in the file named by mFilename.
 */
public class DayGramJSONSerializer {
    private static final String TAG = "DayGramJSONSerializer";


    private Context mContext;
    private String mFileName;

    public DayGramJSONSerializer(Context c, String f) {
        mContext = c;
        mFileName = f;
    }

    public ArrayList<Diary> loadDiaries() throws IOException, JSONException {
        ArrayList<Diary> diaries = new ArrayList<Diary>();
        ArrayList<DiaryDot> dots = new ArrayList<DiaryDot>();
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
            for (int i = 0; i < array.length(); i++) {
                Diary d = new Diary(array.getJSONObject(i));
                if (d.getText() != null)
                    diaries.add(d);
            }
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
            Log.d(TAG, "diaries saved to file");

        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
