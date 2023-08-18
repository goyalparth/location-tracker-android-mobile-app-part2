package com.example.sampleassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    dbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        EditText editTextU1 = findViewById(R.id.editTextU1);
        EditText editTextU2 = findViewById(R.id.editTextU2);
        EditText editTextU3 = findViewById(R.id.editTextU3);



        dbHelper = new dbHelper(MainActivity2.this, "user");
        if (dbHelper != null) {

            Cursor cursor = dbHelper.getData();
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_NAME));
                    switch (cursor.getPosition()) {
                        case 0:
                            editTextU1.setText(name);
                            break;
                        case 1:
                            editTextU2.setText(name);
                            break;
                        case 2:
                            editTextU3.setText(name);
                            break;
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }


        RadioButton radioButton1 = findViewById(R.id.radioButtonU1);
        RadioButton radioButton2 = findViewById(R.id.radioButtonU2);
        RadioButton radioButton3 = findViewById(R.id.radioButtonU3);

        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonStart = findViewById(R.id.buttonStart);

        TextView textSelect = findViewById(R.id.selectUserText);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user1 = editTextU1.getText().toString();
                String user2 = editTextU2.getText().toString();
                String user3 = editTextU3.getText().toString();

                if (!user1.isEmpty() && !user2.isEmpty() && !user3.isEmpty()){
                    if (dbHelper == null){
                        long id1 = dbHelper.insertColour(user1);
                        long id2 = dbHelper.insertColour(user2);
                        long id3 = dbHelper.insertColour(user3);
                    }
                    else {
                        dbHelper.deleteAllColours();
                        long id1 = dbHelper.insertColour(user1);
                        long id2 = dbHelper.insertColour(user2);
                        long id3 = dbHelper.insertColour(user3);
                    }
                    radioButton1.setVisibility(View.VISIBLE);
                    radioButton2.setVisibility(View.VISIBLE);
                    radioButton3.setVisibility(View.VISIBLE);
                    buttonStart.setVisibility(View.VISIBLE);
                    textSelect.setVisibility(View.VISIBLE);

                    radioButton1.setText(editTextU1.getText().toString());
                    radioButton2.setText(editTextU2.getText().toString());
                    radioButton3.setText(editTextU3.getText().toString());

                    radioButton1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            radioButton1.setChecked(true);
                            radioButton2.setChecked(false);
                            radioButton3.setChecked(false);
                            int id1 = dbHelper.updateColour(1, user1);
                            int id2 = dbHelper.updateColour(2, user2);
                            int id3 = dbHelper.updateColour(3, user3);
                        }
                    });

                    radioButton2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            radioButton2.setChecked(true);
                            radioButton1.setChecked(false);
                            radioButton3.setChecked(false);
                            int id1 = dbHelper.updateColour(2, user1);
                            int id2 = dbHelper.updateColour(1, user2);
                            int id3 = dbHelper.updateColour(3, user3);
                        }
                    });

                    radioButton3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            radioButton3.setChecked(true);
                            radioButton2.setChecked(false);
                            radioButton1.setChecked(false);
                            int id1 = dbHelper.updateColour(3, user1);
                            int id2 = dbHelper.updateColour(2, user2);
                            int id3 = dbHelper.updateColour(1, user3);
                        }
                    });
                }}


        });


        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }




}