package com.example.facemaker2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import androidx.annotation.RequiresApi;

import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;

public class Face extends SurfaceView implements SeekBar.OnSeekBarChangeListener, View.OnClickListener,
        AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {
    //colors
    public int skinColor;
    public int hairColor;
    public int eyeColor;

    //other variables
    public int hairStyle;
    public int selected;
    public int height;
    public int width;
    public boolean updated;

    //color changing paints
    public Paint irisPaint = new Paint();
    public Paint hairPaint = new Paint();
    public Paint skinPaint = new Paint();

    //color-independent paints
    public Paint pupilPaint = new Paint();
    public Paint whitePaint = new Paint();
    public Paint mouthPaint = new Paint();

    public Face(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        randomize();

        //color changing paints
        irisPaint.setColor(eyeColor);
        irisPaint.setStyle(Paint.Style.FILL);
        hairPaint.setColor(hairColor);
        hairPaint.setStyle(Paint.Style.FILL);
        skinPaint.setColor(skinColor);
        skinPaint.setStyle(Paint.Style.FILL);

        //color-independent paints
        pupilPaint.setColor(Color.BLACK);
        pupilPaint.setStyle(Paint.Style.FILL);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.FILL);
        mouthPaint.setColor(Color.MAGENTA);
        mouthPaint.setStyle(Paint.Style.FILL);

        //looks better than the default at least
        setBackgroundColor(Color.WHITE);
    }

    public void randomize() {
        skinColor = ((int)(Math.random()*16777215)) | (0xFF << 24);
        hairColor = ((int)(Math.random()*16777215)) | (0xFF << 24);
        eyeColor = ((int)(Math.random()*16777215)) | (0xFF << 24);
        hairStyle = (int)(Math.random()*3);
        selected = 0;
        updated = false;
    }

    //commits draw
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onDraw(Canvas canvas) {
        //gets the canvas dimensions
        height = canvas.getHeight();
        width = canvas.getWidth();

        //draw face and eyes first
        drawFace(canvas);
        drawEyes(canvas);

        //change hairstyle depending on the spinner
        switch (hairStyle) {
            case 0:
                drawHair1(canvas);
                break;
            case 1:
                drawHair2(canvas);
                break;
            case 2:
                drawHair3(canvas);
                break;
        }
    }

    //draws the basic face
    public void drawFace(Canvas canvas) {
        skinPaint.setColor(skinColor);
        int centerHeight = height/2 + 300;
        int centerWidth = width/2 - 300;
        //head
        canvas.drawCircle(centerHeight, centerWidth, 150, skinPaint);
        //right ear
        canvas.drawCircle(centerHeight+150, centerWidth, 50, skinPaint);
        //left ear
        canvas.drawCircle(centerHeight-150, centerWidth, 50, skinPaint);
        //mouth
        canvas.drawRect(centerHeight+50, centerWidth+50, centerHeight-50, centerWidth+60, pupilPaint);
    }

    //draws a single eye with X and Y parameters
    public void drawEyes(Canvas canvas) {
        int centerHeight = height/2 + 300;
        int centerWidth = width/2 - 300;

        //right eye
        drawEye(canvas, centerHeight+50, centerWidth);
        //left eye
        drawEye(canvas, centerHeight-50, centerWidth);
    }

    public void drawEye(Canvas canvas, int x, int y) {
        irisPaint.setColor(eyeColor);
        canvas.drawCircle(x, y, 20, whitePaint);
        canvas.drawCircle(x, y, 15, irisPaint);
        canvas.drawCircle(x, y, 10, pupilPaint);
    }

    //draws the hat hair option
    public void drawHair1(Canvas canvas) {
        int centerHeight = height/2 + 300;
        int centerWidth = width/2 - 300;
        int radius = 160;
        hairPaint.setColor(hairColor);

        canvas.drawRect(centerHeight+160, centerWidth-110, centerHeight-160,
                centerWidth-60, hairPaint);
        canvas.drawRect(centerHeight+100, centerWidth-210, centerHeight-100,
                centerWidth-110, hairPaint);
    }

    //draws the Wimpy hair option
    public void drawHair2(Canvas canvas) {
        int centerHeight = height/2 + 300;
        int centerWidth = width/2 - 300;
        hairPaint.setColor(hairColor);

        canvas.drawRect(centerHeight+5, centerWidth-150, centerHeight-5,
                centerWidth-200, hairPaint);
        canvas.drawRect(centerHeight+20, centerWidth-175, centerHeight-20,
                centerWidth-185, hairPaint);
    }

    //draws the Afro hair option
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void drawHair3(Canvas canvas) {
        int centerHeight = height/2 + 300;
        int centerWidth = width/2 - 300;
        hairPaint.setColor(hairColor);

        canvas.drawOval(centerHeight+160, centerWidth-50, centerHeight-160,
                centerWidth-250, hairPaint);
    }

    //onClick method for Random button
    @Override
    public void onClick(View v) {
        randomize();
        Log.d("Button", "Randomized");
        invalidate();
    }

    //onItemSelected for Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //switch case goes here
        hairStyle = position;
        Log.d("Spinner", "Position = "+position+", Stored Value = "+hairStyle);
        invalidate();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(hairStyle);
    }

    //onCheckedChanged for Radio Group
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //parse input for Radio Group
        switch(checkedId){
            case R.id.radioeye:
                selected = 0;
                break;
            case R.id.radiohair:
                selected = 1;
                break;
            case R.id.radioskin:
                selected = 2;
                break;
        }
        //triggers seekbar update
        updated = false;
        Log.d("Radio Group", "Updated = "+updated+", Selected = "+selected);
        invalidate();
    }

    //onProgressChanged for SeekBar
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //get RGB values from SeekBar
        int color = Color.BLACK;
        /*switch (seekBar.getId()) {
            case R.id.rSeekBar:
                color = -red(seekBar.getProgress());
                Log.d("SeekBar", "Red Progress: "+seekBar.getProgress());
                break;
            case R.id.gSeekBar:
                color = -green(seekBar.getProgress());
                Log.d("SeekBar", "Green Progress: "+seekBar.getProgress());
                break;
            case R.id.bSeekBar:
                color = -blue(seekBar.getProgress());
                Log.d("SeekBar", "Blue Progress: "+seekBar.getProgress());
                break;
        }*/
        int r = 0;
        int g = 0;
        int b = 0;
        switch (seekBar.getId()) {
            case R.id.rSeekBar:
                r = seekBar.getProgress();
                Log.d("SeekBar", "Red Progress: "+seekBar.getProgress());
                break;
            case R.id.gSeekBar:
                g = seekBar.getProgress();
                Log.d("SeekBar", "Green Progress: "+seekBar.getProgress());
                break;
            case R.id.bSeekBar:
                b = seekBar.getProgress();
                Log.d("SeekBar", "Blue Progress: "+seekBar.getProgress());
                break;
        }
        color = Color.argb(255, r, g, b);
        //set corresponding color value
        switch (selected) {
            case 0:
                eyeColor = color;
                break;
            case 1:
                hairColor = color;
                break;
            case 2:
                skinColor = color;
                break;
        }
        //redraw the SurfaceView
        updated = true;
        invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (updated == false) {
            int setColor = Color.BLACK;
            //get the color
            switch (selected) {
                case 0:
                    setColor = eyeColor;
                    break;
                case 1:
                    setColor = hairColor;
                    break;
                case 2:
                    setColor = skinColor;
                    break;
            }
            //sets seekbars to proper position
            switch (seekBar.getId()) {
                case R.id.rSeekBar:
                    seekBar.setProgress(red(setColor));
                    break;
                case R.id.gSeekBar:
                    seekBar.setProgress(green(setColor));;
                    break;
                case R.id.bSeekBar:
                    seekBar.setProgress(blue(setColor));;
                    break;
            }
            //sets the color as updated to avoid freezing
            updated = true;
            Log.d("SeekBar", "Updated = "+updated);
        }
        invalidate();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //set SeekBars to correct color if the selected item has changed
        if (updated == false) {
            int setColor = Color.BLACK;
            //get the color
            switch (selected) {
                case 0:
                    setColor = eyeColor;
                    break;
                case 1:
                    setColor = hairColor;
                    break;
                case 2:
                    setColor = skinColor;
                    break;
            }
            //sets seekbars to proper position
            switch (seekBar.getId()) {
                case R.id.rSeekBar:
                    seekBar.setProgress(red(setColor));
                    break;
                case R.id.gSeekBar:
                    seekBar.setProgress(green(setColor));;
                    break;
                case R.id.bSeekBar:
                    seekBar.setProgress(blue(setColor));;
                    break;
            }
            //sets the color as updated to avoid freezing
            updated = true;
            Log.d("SeekBar", "Updated = "+updated);
        }
        invalidate();
    }
}
