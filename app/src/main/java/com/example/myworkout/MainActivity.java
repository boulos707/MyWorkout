package com.example.myworkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText etId, etName, etPhone, etDuration;
    CheckBox cbYoga, cbBoxing, cbPilates, cbRegular;
    Button btnSave, btnFetchAllClients, btnDelete, btnTotalSalary;
    String insertUrl = "http://10.0.2.2/fitness_app/insert.php";
    String fetchUrl = "http://10.0.2.2/fitness_app/select_all.php";

    String deleteUrl = "http://10.0.2.2/fitness_app/delete.php";


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
        btnDelete = findViewById(R.id.btnDelete);
        btnTotalSalary = findViewById(R.id.btnTotalSalary);


        // Save Client Logic
        btnSave.setOnClickListener(view -> saveClient());
        // Fetch All Clients Logic
        btnFetchAllClients.setOnClickListener(view -> fetchAllClients());
        // Delete Clients
        btnDelete.setOnClickListener(view -> deleteClient());
        // Calculate All Salary
        btnTotalSalary.setOnClickListener(v -> calculateTotalSalary());
    }

    private void saveClient() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, insertUrl,
                response -> {
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    clearFields();
                },
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
                    Intent intent = new Intent(MainActivity.this, ClientList.class);
                    intent.putExtra("clients", response);
                    startActivity(intent);
                },
                error -> Toast.makeText(MainActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show());

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void deleteClient() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, deleteUrl,
                response -> Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(MainActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", etId.getText().toString());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void calculateTotalSalary() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, fetchUrl,
                response -> {
                    try {
                        JSONArray clients = new JSONArray(response);
                        int totalSalary = 0;
                        int pricePerMonth = 50;

                        for (int i = 0; i < clients.length(); i++) {
                            JSONObject client = clients.getJSONObject(i);
                            int duration = client.getInt("duration");
                            totalSalary += duration * pricePerMonth;
                        }

                        Toast.makeText(MainActivity.this, "Total Salary: $" + totalSalary, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(MainActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show());

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void clearFields() {
        etId.setText("");
        etName.setText("");
        etPhone.setText("");
        etDuration.setText("");

        cbYoga.setChecked(false);
        cbBoxing.setChecked(false);
        cbPilates.setChecked(false);
        cbRegular.setChecked(false);
    }

}