package com.example.facemaker2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup face class
        Face face = (Face)findViewById(R.id.faceview);

        //seekbar setup
        SeekBar rSeekBar = (SeekBar)findViewById(R.id.rSeekBar);
        rSeekBar.setOnSeekBarChangeListener(face);
        SeekBar gSeekBar = (SeekBar)findViewById(R.id.gSeekBar);
        gSeekBar.setOnSeekBarChangeListener(face);
        SeekBar bSeekBar = (SeekBar)findViewById(R.id.bSeekBar);
        bSeekBar.setOnSeekBarChangeListener(face);

        //randomize button setup
        Button randomize = (Button)findViewById(R.id.randomize);
        randomize.setOnClickListener(face);

        //setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.colorable_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(face);

        //setup radio button
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(face);
    }
}