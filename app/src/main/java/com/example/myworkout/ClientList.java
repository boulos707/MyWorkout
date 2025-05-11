package com.example.myworkout;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.*;

import java.util.ArrayList;

public class ClientList extends AppCompatActivity {
    ListView clientList;
    String url = "http://10.0.2.2/fitness_app/select_all.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);

        clientList = findViewById(R.id.lvClients);

        fetchClients();
    }

    private void fetchClients() {
        ArrayList<String> clients = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        if (jsonArray.length() == 0) {
                            clients.add("No clients found");
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject client = jsonArray.getJSONObject(i);
                                String name = client.getString("full_name");
                                String phone = client.getString("phone_number");
                                String duration = client.getString("duration");
                                clients.add(name + " | " + phone + " | " + duration);
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, clients);
                        clientList.setAdapter(adapter);

                    } catch (JSONException e) {
                        Toast.makeText(this, "JSON Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Volley Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
