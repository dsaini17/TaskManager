package com.devesh.todolist;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.devesh.todolist.Models.Task;

public class Input extends AppCompatActivity {

    EditText et1;
    RatingBar ratingBar;
    Button submit;
    DatePicker datePicker;
    int date,month,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);


        et1 = (EditText) findViewById(R.id.edittext);
        submit = (Button) findViewById(R.id.submit);
        ratingBar = (RatingBar) findViewById(R.id.priority);
        datePicker = (DatePicker) findViewById(R.id.datepicker);

        final int a=datePicker.getDayOfMonth(),b=datePicker.getMonth(),c=datePicker.getYear();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date = datePicker.getDayOfMonth();
                month = datePicker.getMonth();
                year = datePicker.getYear();
                String time = String.valueOf(date)+"-"+String.valueOf(month)+"-"+String.valueOf(year);
                String text = et1.getText().toString();
                int prior = (int) ratingBar.getRating();
                if (text.isEmpty()){
                    Toast.makeText(Input.this,"Enter Task",Toast.LENGTH_SHORT).show();
                }
                else if(prior==0){
                    Toast.makeText(Input.this,"Choose Priority",Toast.LENGTH_SHORT).show();
                }
                else if((year<c)||(year==c&&month<b)||(year==c&&month==b&&date<a)){
                    Toast.makeText(Input.this,"Choose Present or Future Date",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Intent intent = new Intent();
                    intent.putExtra("TEXT",text);
                    intent.putExtra("DATE",time);
                    intent.putExtra("RATING",prior);
                    intent.putExtra("STATE",0);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });

    }
}
