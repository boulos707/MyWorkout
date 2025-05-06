package com.example.myworkout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText id, fullName, phoneNumber;
    CheckBox yoga, boxing, pilates, regular;
    RadioButton oneMonth, threeMonths, sixMonths;
    Button insert, selectOne, selectAll, delete, calculate;
    RadioGroup radioGroupDuration;

    SQLiteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Binding
        id = findViewById(R.id.etId);
        fullName = findViewById(R.id.etFullName);
        phoneNumber = findViewById(R.id.etPhoneNumber);

        yoga = findViewById(R.id.cbYoga);
        boxing = findViewById(R.id.cbBoxing);
        pilates = findViewById(R.id.cbPilates);
        regular = findViewById(R.id.cbRegular);

        oneMonth = findViewById(R.id.rbOneMonth);
        threeMonths = findViewById(R.id.rbThreeMonths);
        sixMonths = findViewById(R.id.rbSixMonths);

        insert = findViewById(R.id.btnInsert);
        selectOne = findViewById(R.id.btnSelectOne);
        selectAll = findViewById(R.id.btnSelectAll);
        delete = findViewById(R.id.btnDelete);
        calculate = findViewById(R.id.btnCalculate);
        radioGroupDuration = findViewById(R.id.rgRadioGroupDuration);

        mydb = openOrCreateDatabase("gymDb",MODE_PRIVATE,null);
        mydb.execSQL("CREATE TABLE IF NOT EXISTS gym(id VARCHAR, fullName VARCHAR, phoneNumber VARCHAR, classes VARCHAR, duration VARCHAR)");

        //======Insert=======//
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = id.getText().toString().trim();
                String name = fullName.getText().toString().trim();
                String phone = phoneNumber.getText().toString().trim();

                if (userId.isEmpty() || name.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
                    return;
                }

                // Collect selected classes
                StringBuilder selectedClasses = new StringBuilder();
                if (yoga.isChecked()) selectedClasses.append("Yoga ");
                if (boxing.isChecked()) selectedClasses.append("Boxing ");
                if (pilates.isChecked()) selectedClasses.append("Pilates ");
                if (regular.isChecked()) selectedClasses.append("Regular ");

                if (selectedClasses.toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Select at least one class", Toast.LENGTH_LONG).show();
                    return;
                }

                // Get selected duration from RadioGroup

                int selectedId = radioGroupDuration.getCheckedRadioButtonId();
                RadioButton selectedRadio = findViewById(selectedId);
                String duration = selectedRadio.getText().toString();

                // Insert into database
                String insertQuery = "INSERT INTO gym (id, fullName, phoneNumber, classes, duration) VALUES (?, ?, ?, ?, ?)";
                mydb.execSQL(insertQuery, new String[]{userId, name, phone, selectedClasses.toString().trim(), duration});

                Toast.makeText(MainActivity.this, "Inserted successfully", Toast.LENGTH_LONG).show();
                clear();
            }
        });
        //======SelectAll=====//
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = mydb.rawQuery("SELECT * FROM gym",null);
                if(c.getCount()==0){
                    Toast.makeText(MainActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<String> ClientsList = new ArrayList<>();
                while (c.moveToNext()){
                    String client = "ID: "+c.getString(0)+
                            "\nFull Name: "+c.getString(1)+
                            "\nPhone number: "+c.getString(2)+
                            "\nClasses: "+c.getString(3)+
                            "\nDuration: "+c.getString(4);
                    ClientsList.add(client);
                }
                Intent i = new Intent(MainActivity.this, ClientList.class);
                i.putStringArrayListExtra("clients",ClientsList);
                startActivity(i);
            }
        });
    }
    public void clear(){
        // clear fields
        id.setText("");
        fullName.setText("");
        phoneNumber.setText("");
        yoga.setChecked(false);
        boxing.setChecked(false);
        pilates.setChecked(false);
        regular.setChecked(false);
        radioGroupDuration.check(R.id.rbOneMonth); // reset to default
    }
}