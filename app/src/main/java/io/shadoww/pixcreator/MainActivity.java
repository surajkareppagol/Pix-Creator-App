package io.shadoww.pixcreator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.graphics.Color;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public int currentColor = Color.RED;
    public int eraseColor = Color.LTGRAY;
    public int selectedColor = Color.rgb(34, 50, 130);
    public int toolColor = Color.rgb(25,23 ,22 );
    public String currentTool = "";
    public String templateLine = "";
    public String templateFillLine = "";
    public String svgFileData = "";
    public int lineCounter = 0;

    public String[] svgPixelColors = {"#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc", "#ffcccccc"};

    public void setSelectedColor(int id) {
        System.out.println("Inside setColor");
        findViewById(R.id.brushTool).setBackgroundColor(toolColor);
        findViewById(R.id.paintTool).setBackgroundColor(toolColor);
        findViewById(R.id.eraserTool).setBackgroundColor(toolColor);
        findViewById(R.id.saveTool).setBackgroundColor(toolColor);
        findViewById(id).setBackgroundColor(selectedColor);
    }

    public void createSvgData() {
        try {
            DataInputStream textFileStream = new DataInputStream(getAssets().open("template.svg"));
            Scanner scan = new Scanner(textFileStream);
            while (scan.hasNextLine()) {
                templateLine = scan.nextLine();
                if (templateLine.contains("fill")) {
                    for (int i = 0; i < 19; i++)
                        templateFillLine += templateLine.charAt(i);
                    templateFillLine += svgPixelColors[lineCounter];
                    lineCounter++;
                    for (int i = 26; i < 76; i++)
                        templateFillLine += templateLine.charAt(i);
                    svgFileData += templateFillLine + "\n";
                    templateFillLine = "";
                } else
                    svgFileData += templateLine + "\n";
            }
            exportFile(svgFileData);
            lineCounter = 0;
            templateFillLine = "";
            svgFileData = "";
            for (int i = 0; i < 36; i++)
                svgPixelColors[i] = "#ffcccccc";
            scan.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void exportFile(String svgFileData) {
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "output");
            values.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/svg");
            Uri uri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
            OutputStream outputStream = getContentResolver().openOutputStream(uri);
            outputStream.write(svgFileData.getBytes());
            outputStream.close();

            Toast.makeText(this, "File created successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Fail to create file", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertColor(int index) {
        if (currentColor == Color.BLACK) {
            svgPixelColors[index] = "#000000ff";
        } else if (currentColor == Color.WHITE) {
            svgPixelColors[index] = "#ffffffff";
        } else if (currentColor == Color.RED) {
            svgPixelColors[index] = "#ff0000ff";
        } else if (currentColor == Color.GREEN) {
            svgPixelColors[index] = "#008000ff";
        } else if (currentColor == Color.rgb(150, 75, 0)) {
            svgPixelColors[index] = "#964b00ff";
        } else if (currentColor == Color.BLUE) {
            svgPixelColors[index] = "#0000ffff";
        } else if (currentColor == Color.YELLOW) {
            svgPixelColors[index] = "#ffff00ff";
        } else if (currentColor == Color.rgb(255, 165, 0)) {
            svgPixelColors[index] = "#ffa500ff";
        } else if (currentColor == Color.rgb(160, 32, 240)) {
            svgPixelColors[index] = "#a020f0ff";
        } else if (currentColor == Color.rgb(0, 255, 255)) {
            svgPixelColors[index] = "##00ffffff";
        } else if (currentColor == Color.rgb(255, 0, 255)) {
            svgPixelColors[index] = "#ff00ffff";
        } else if (currentColor == Color.rgb(128, 128, 128)) {
            svgPixelColors[index] = "#808080ff";
        } else if (currentColor == Color.LTGRAY) {
            svgPixelColors[index] = "#ffcccccc";
        }
    }

    public void paintPixel(int id, int index) {
        if (currentTool.equals("brush"))
            findViewById(id).setBackgroundColor(currentColor);
        if (currentTool.equals("eraser")) {
            findViewById(id).setBackgroundColor(eraseColor);
            currentColor = Color.LTGRAY;
        }
        insertColor(index - 1);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        /* COLORS */
        if (id == R.id.colorBlack) {
            if (currentTool.equals("paint"))
                currentColor = Color.BLACK;
        } else if (id == R.id.colorWhite) {
            if (currentTool.equals("paint"))
                currentColor = Color.WHITE;
        } else if (id == R.id.colorRed) {
            if (currentTool.equals("paint"))
                currentColor = Color.RED;
        } else if (id == R.id.colorGreen) {
            if (currentTool.equals("paint"))
                currentColor = Color.GREEN;
        } else if (id == R.id.colorBrown) {
            if (currentTool.equals("paint"))
                currentColor = Color.rgb(150, 75, 0);
        } else if (id == R.id.colorBlue) {
            if (currentTool.equals("paint"))
                currentColor = Color.BLUE;
        } else if (id == R.id.colorYellow) {
            if (currentTool.equals("paint"))
                currentColor = Color.YELLOW;
        } else if (id == R.id.colorOrange) {
            if (currentTool.equals("paint"))
                currentColor = Color.rgb(255, 165, 0);
        } else if (id == R.id.colorPurple) {
            if (currentTool.equals("paint"))
                currentColor = Color.rgb(160, 32, 240);
        } else if (id == R.id.colorCyan) {
            if (currentTool.equals("paint"))
                currentColor = Color.rgb(0, 255, 255);
        } else if (id == R.id.colorMagenta) {
            if (currentTool.equals("paint"))
                currentColor = Color.rgb(255, 0, 255);
        } else if (id == R.id.colorGray) {
            if (currentTool.equals("paint"))
                currentColor = Color.rgb(128, 128, 128);
        }

        /* TOOLS */

        else if (id == R.id.brushTool) {
            currentTool = "brush";
            setSelectedColor(R.id.brushTool);
        } else if (id == R.id.paintTool) {
            currentTool = "paint";
            setSelectedColor(R.id.paintTool);
        } else if (id == R.id.eraserTool) {
            currentTool = "eraser";
            setSelectedColor(R.id.eraserTool);
        } else if (id == R.id.saveTool) {
            currentTool = "save";
            setSelectedColor(R.id.saveTool);
            createSvgData();
        }

        /* PIXELS */

        /* ROW 1 */
        else if (id == R.id.colorBox1) {
            paintPixel(R.id.colorBox1, 1);
        } else if (id == R.id.colorBox2) {
            paintPixel(R.id.colorBox2, 2);
        } else if (id == R.id.colorBox3) {
            paintPixel(R.id.colorBox3, 3);
        } else if (id == R.id.colorBox4) {
            paintPixel(R.id.colorBox4, 4);
        } else if (id == R.id.colorBox5) {
            paintPixel(R.id.colorBox5, 5);
        } else if (id == R.id.colorBox6) {
            paintPixel(R.id.colorBox6, 6);
        }

        /* ROW 2 */

        else if (id == R.id.colorBox7) {
            paintPixel(R.id.colorBox7, 7);
        } else if (id == R.id.colorBox8) {
            paintPixel(R.id.colorBox8, 8);
        } else if (id == R.id.colorBox9) {
            paintPixel(R.id.colorBox9, 9);
        } else if (id == R.id.colorBox10) {
            paintPixel(R.id.colorBox10, 10);
        } else if (id == R.id.colorBox11) {
            paintPixel(R.id.colorBox11, 11);
        } else if (id == R.id.colorBox12) {
            paintPixel(R.id.colorBox12, 12);
        }

        /* ROW 3 */

        else if (id == R.id.colorBox13) {
            paintPixel(R.id.colorBox13, 13);
        } else if (id == R.id.colorBox14) {
            paintPixel(R.id.colorBox14, 14);
        } else if (id == R.id.colorBox15) {
            paintPixel(R.id.colorBox15, 15);
        } else if (id == R.id.colorBox16) {
            paintPixel(R.id.colorBox16, 16);
        } else if (id == R.id.colorBox17) {
            paintPixel(R.id.colorBox17, 17);
        } else if (id == R.id.colorBox18) {
            paintPixel(R.id.colorBox18, 18);
        }

        /* ROW 4 */

        else if (id == R.id.colorBox19) {
            paintPixel(R.id.colorBox19, 19);
        } else if (id == R.id.colorBox20) {
            paintPixel(R.id.colorBox20, 20);
        } else if (id == R.id.colorBox21) {
            paintPixel(R.id.colorBox21, 21);
        } else if (id == R.id.colorBox22) {
            paintPixel(R.id.colorBox22, 22);
        } else if (id == R.id.colorBox23) {
            paintPixel(R.id.colorBox23, 23);
        } else if (id == R.id.colorBox24) {
            paintPixel(R.id.colorBox24, 24);
        }

        /* ROW 5 */

        else if (id == R.id.colorBox25) {
            paintPixel(R.id.colorBox25, 25);
        } else if (id == R.id.colorBox26) {
            paintPixel(R.id.colorBox26, 26);
        } else if (id == R.id.colorBox27) {
            paintPixel(R.id.colorBox27, 27);
        } else if (id == R.id.colorBox28) {
            paintPixel(R.id.colorBox28, 28);
        } else if (id == R.id.colorBox29) {
            paintPixel(R.id.colorBox29, 29);
        } else if (id == R.id.colorBox30) {
            paintPixel(R.id.colorBox30, 30);
        }

        /* ROW 6 */

        else if (id == R.id.colorBox31) {
            paintPixel(R.id.colorBox31, 31);
        } else if (id == R.id.colorBox32) {
            paintPixel(R.id.colorBox32, 32);
        } else if (id == R.id.colorBox33) {
            paintPixel(R.id.colorBox33, 33);
        } else if (id == R.id.colorBox34) {
            paintPixel(R.id.colorBox34, 34);
        } else if (id == R.id.colorBox35) {
            paintPixel(R.id.colorBox35, 35);
        } else if (id == R.id.colorBox36) {
            paintPixel(R.id.colorBox36, 36);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        /* COLORS */

        Button btnColorBlack = findViewById(R.id.colorBlack);
        btnColorBlack.setOnClickListener(this);

        Button btnColorWhite = findViewById(R.id.colorWhite);
        btnColorWhite.setOnClickListener(this);

        Button btnColorRed = findViewById(R.id.colorRed);
        btnColorRed.setOnClickListener(this);

        Button btnColorGreen = findViewById(R.id.colorGreen);
        btnColorGreen.setOnClickListener(this);

        Button btnColorBrown = findViewById(R.id.colorBrown);
        btnColorBrown.setOnClickListener(this);

        Button btnColorBlue = findViewById(R.id.colorBlue);
        btnColorBlue.setOnClickListener(this);

        Button btnColorYellow = findViewById(R.id.colorYellow);
        btnColorYellow.setOnClickListener(this);

        Button btnColorOrange = findViewById(R.id.colorOrange);
        btnColorOrange.setOnClickListener(this);

        Button btnColorPurple = findViewById(R.id.colorPurple);
        btnColorPurple.setOnClickListener(this);

        Button btnColorCyan = findViewById(R.id.colorCyan);
        btnColorCyan.setOnClickListener(this);

        Button btnColorMagenta = findViewById(R.id.colorMagenta);
        btnColorMagenta.setOnClickListener(this);

        Button btnColorGray = findViewById(R.id.colorGray);
        btnColorGray.setOnClickListener(this);

        /* TOOLS */

        ImageButton btnBrushTool = findViewById(R.id.brushTool);
        btnBrushTool.setOnClickListener((this));

        ImageButton btnPaintTool = findViewById(R.id.paintTool);
        btnPaintTool.setOnClickListener((this));

        ImageButton btnEraseTool = findViewById(R.id.eraserTool);
        btnEraseTool.setOnClickListener((this));

        ImageButton btnSaveTool = findViewById(R.id.saveTool);
        btnSaveTool.setOnClickListener((this));

        /* PIXELS */

        Button btnPixel1 = findViewById(R.id.colorBox1);
        btnPixel1.setOnClickListener(this);

        Button btnPixel2 = findViewById(R.id.colorBox2);
        btnPixel2.setOnClickListener(this);

        Button btnPixel3 = findViewById(R.id.colorBox3);
        btnPixel3.setOnClickListener(this);

        Button btnPixel4 = findViewById(R.id.colorBox4);
        btnPixel4.setOnClickListener(this);

        Button btnPixel5 = findViewById(R.id.colorBox5);
        btnPixel5.setOnClickListener(this);

        Button btnPixel6 = findViewById(R.id.colorBox6);
        btnPixel6.setOnClickListener(this);

        Button btnPixel7 = findViewById(R.id.colorBox7);
        btnPixel7.setOnClickListener(this);

        Button btnPixel8 = findViewById(R.id.colorBox8);
        btnPixel8.setOnClickListener(this);

        Button btnPixel9 = findViewById(R.id.colorBox9);
        btnPixel9.setOnClickListener(this);

        Button btnPixel10 = findViewById(R.id.colorBox10);
        btnPixel10.setOnClickListener(this);

        Button btnPixel11 = findViewById(R.id.colorBox11);
        btnPixel11.setOnClickListener(this);

        Button btnPixel12 = findViewById(R.id.colorBox12);
        btnPixel12.setOnClickListener(this);

        Button btnPixel13 = findViewById(R.id.colorBox13);
        btnPixel13.setOnClickListener(this);

        Button btnPixel14 = findViewById(R.id.colorBox14);
        btnPixel14.setOnClickListener(this);

        Button btnPixel15 = findViewById(R.id.colorBox15);
        btnPixel15.setOnClickListener(this);

        Button btnPixel16 = findViewById(R.id.colorBox16);
        btnPixel16.setOnClickListener(this);

        Button btnPixel17 = findViewById(R.id.colorBox17);
        btnPixel17.setOnClickListener(this);

        Button btnPixel18 = findViewById(R.id.colorBox18);
        btnPixel18.setOnClickListener(this);

        Button btnPixel19 = findViewById(R.id.colorBox19);
        btnPixel19.setOnClickListener(this);

        Button btnPixel20 = findViewById(R.id.colorBox20);
        btnPixel20.setOnClickListener(this);

        Button btnPixel21 = findViewById(R.id.colorBox21);
        btnPixel21.setOnClickListener(this);

        Button btnPixel22 = findViewById(R.id.colorBox22);
        btnPixel22.setOnClickListener(this);

        Button btnPixel23 = findViewById(R.id.colorBox23);
        btnPixel23.setOnClickListener(this);

        Button btnPixel24 = findViewById(R.id.colorBox24);
        btnPixel24.setOnClickListener(this);

        Button btnPixel25 = findViewById(R.id.colorBox25);
        btnPixel25.setOnClickListener(this);

        Button btnPixel26 = findViewById(R.id.colorBox26);
        btnPixel26.setOnClickListener(this);

        Button btnPixel27 = findViewById(R.id.colorBox27);
        btnPixel27.setOnClickListener(this);

        Button btnPixel28 = findViewById(R.id.colorBox28);
        btnPixel28.setOnClickListener(this);

        Button btnPixel29 = findViewById(R.id.colorBox29);
        btnPixel29.setOnClickListener(this);

        Button btnPixel30 = findViewById(R.id.colorBox30);
        btnPixel30.setOnClickListener(this);

        Button btnPixel31 = findViewById(R.id.colorBox31);
        btnPixel31.setOnClickListener(this);

        Button btnPixel32 = findViewById(R.id.colorBox32);
        btnPixel32.setOnClickListener(this);

        Button btnPixel33 = findViewById(R.id.colorBox33);
        btnPixel33.setOnClickListener(this);

        Button btnPixel34 = findViewById(R.id.colorBox34);
        btnPixel34.setOnClickListener(this);

        Button btnPixel35 = findViewById(R.id.colorBox35);
        btnPixel35.setOnClickListener(this);

        Button btnPixel36 = findViewById(R.id.colorBox36);
        btnPixel36.setOnClickListener(this);
    }
}