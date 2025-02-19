package com.projects.taskmate;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;

public class TaskStorage {
    private static final String PREFS_NAME = "TaskPrefs";
    private static final String KEY_TASKS = "tasks";
    public static void saveTasks(Context context,List<task> tasks) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tasks);
        editor.putString(KEY_TASKS, json);
        editor.apply();
    }
    public static List<task> loadTasks(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson=new Gson();
        String json=sharedPreferences.getString(KEY_TASKS,null);
        Type type=new TypeToken<ArrayList<task>>(){}.getType();
        List<task> task=gson.fromJson(json,type);
        if(task==null){
            task=new ArrayList<>();
        }
        return task;
    }

}
