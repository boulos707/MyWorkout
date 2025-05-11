package com.example.myworkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText etId, etName, etPhone, etDuration;
    CheckBox cbYoga, cbBoxing, cbPilates, cbRegular;
    Button btnSave, btnFetchAllClients;
    String insertUrl = "http://10.0.2.2/fitness_app/insert.php"; // use 10.0.2.2 for emulator
    String fetchUrl = "http://10.0.2.2/fitness_app/select_all.php"; // The URL for fetching all clients

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etId = findViewById(R.id.etId);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etDuration = findViewById(R.id.etDuration);

        cbYoga = findViewById(R.id.cbYoga);
        cbBoxing = findViewById(R.id.cbBoxing);
        cbPilates = findViewById(R.id.cbPilates);
        cbRegular = findViewById(R.id.cbRegular);

        btnSave = findViewById(R.id.btnSave);
        btnFetchAllClients = findViewById(R.id.btnFetch);

        // Save Client Logic
        btnSave.setOnClickListener(view -> saveClient());

        // Fetch All Clients Logic
        btnFetchAllClients.setOnClickListener(view -> fetchAllClients());
    }

    private void saveClient() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, insertUrl,
                response -> Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(MainActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", etId.getText().toString());
                params.put("full_name", etName.getText().toString());
                params.put("phone_number", etPhone.getText().toString());
                params.put("yoga", cbYoga.isChecked() ? "1" : "0");
                params.put("boxing", cbBoxing.isChecked() ? "1" : "0");
                params.put("pilates", cbPilates.isChecked() ? "1" : "0");
                params.put("regular", cbRegular.isChecked() ? "1" : "0");
                params.put("duration", etDuration.getText().toString());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void fetchAllClients() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, fetchUrl,
                response -> {
                    // This is where you would process the response from the server.
                    // You can start the ClientList activity and pass the data to it.
                    Intent intent = new Intent(MainActivity.this, ClientList.class);
                    intent.putExtra("clients", response); // Assuming the response is a list of clients in JSON or plain text format
                    startActivity(intent);
                },
                error -> Toast.makeText(MainActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show());

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
