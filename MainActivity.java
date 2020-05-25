package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    UserListAdapter adapter;
    ListView listView;
    ArrayList<User> users = new ArrayList<>();


    protected void addUsers () {
        AssetManager text = getResources().getAssets();
        try (InputStreamReader instream = new InputStreamReader(text.open("names.json"))) {
            BufferedReader buff = new BufferedReader(instream);
            String text_lines = "", line = "";

            while ((line = buff.readLine()) != null) {
                text_lines = text_lines + line;
            }

            String data[] = text_lines.split("\"user\":");

            for (int i = 1; i < data.length; i++) {
                Gson gson_names = new Gson();
                data[i] = data[i].trim();
                String str = data[i].substring(0, data[i].length() - 1);
                Name names = gson_names.fromJson(str, Name.class);

                Sex sex;
                switch (names.sex) {
                    case "m":
                        sex = Sex.MAN; break;
                    case "w":
                        sex = Sex.WOMAN; break;
                    default:
                        sex = Sex.UNKNOWN; break;
                }

                users.add(new User (names.name, names.number, sex));
            }

        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);

        addUsers();

        adapter = new UserListAdapter(this, users);

        listView.setAdapter(adapter);
    }

    public void onClick(View v) {
        ArrayList<User> sorted = new ArrayList<>();


        switch (v.getId()) {
            case R.id.b_sort:
                sorted.addAll(users);
                users.clear();
                users.addAll(sortToBig(sorted));
                break;

            case R.id.s_sort:
                sorted.addAll(users);
                users.clear();
                users.addAll(sortToSmall(sorted));
                break;

            case R.id.ok:
                Spinner spinner = findViewById(R.id.spinner);
                String selected_sex = spinner.getSelectedItem().toString();
                sorted.clear();
                users.clear();
                addUsers();
                sorted.addAll(users);
                users.clear();

                switch (selected_sex) {
                    case "man":
                        users.addAll(sortByMen(sorted));
                        break;
                    case "woman":
                        users.addAll(sortByWoman(sorted));
                        break;
                    case "unknown":
                        users.addAll(sortByUnknown(sorted));
                        break;
                    default:
                        users.addAll(sorted);
                        break;
                }
                break;

        }

        adapter.notifyDataSetChanged();

    }

    private ArrayList<User> sortToBig(ArrayList<User> users) {
        ArrayList<User> sorted = new ArrayList<>();
        sorted.addAll(sortToSmall(users));
        Collections.reverse(sorted);
        return sorted;
    }

    private ArrayList<User> sortToSmall(ArrayList<User> users) {
        ArrayList<User> sorted = new ArrayList<>();
        sorted.addAll(users);
        Collections.sort(sorted, new Comparator<User>() {
            public int compare(User o1, User o2) {
                return o1.name.compareTo(o2.name);
            }
        });
        return sorted;
    }

    private ArrayList<User> sortByMen(ArrayList<User> users) {
        ArrayList<User> sorted = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i). sex == Sex.MAN) {
                sorted.add(users.get(i));
            }
        }
        return sorted;
    }

    private ArrayList<User> sortByWoman(ArrayList<User> users) {
        ArrayList<User> sorted = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i). sex == Sex.WOMAN) {
                sorted.add(users.get(i));
            }
        }
        return sorted;
    }

    private ArrayList<User> sortByUnknown(ArrayList<User> users) {
        ArrayList<User> sorted = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i). sex == Sex.UNKNOWN) {
                sorted.add(users.get(i));
            }
        }

        return sorted;
    }
}