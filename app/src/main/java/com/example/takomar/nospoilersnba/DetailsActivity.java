package com.example.takomar.nospoilersnba;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private int getDpInPx(int dp){
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TableLayout nameCol=(TableLayout)findViewById(R.id.tableName);
        TableRow nameHeader = (TableRow) LayoutInflater.from(this).inflate(R.layout.name_col, null);
        TextView headerName = ((TextView)nameHeader.findViewById(R.id.name));

        int dpAsPixels = getDpInPx(2);
        headerName.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
        headerName.setText("Name");
        nameCol.addView(nameHeader);

        TableLayout tl=(TableLayout)findViewById(R.id.tableBox);
        TableRow header = (TableRow) LayoutInflater.from(this).inflate(R.layout.header_line, null);
        ((TextView)header.findViewById(R.id.min)).setText("Mins");
        ((TextView)header.findViewById(R.id.fgm)).setText("FGM");
        ((TextView)header.findViewById(R.id.fga)).setText("FGA");
        ((TextView)header.findViewById(R.id.tgm)).setText("3GM");
        ((TextView)header.findViewById(R.id.tga)).setText("3GA");
        ((TextView)header.findViewById(R.id.reb)).setText("REB");
        ((TextView)header.findViewById(R.id.stl)).setText("STL");
        ((TextView)header.findViewById(R.id.ass)).setText("ASS");
        ((TextView)header.findViewById(R.id.blk)).setText("BLK");
        ((TextView)header.findViewById(R.id.to)).setText("TO");
        ((TextView)header.findViewById(R.id.pts)).setText("PTS");
        tl.addView(header);

        String s= getIntent().getStringExtra("GameID");
        String q= getIntent().getStringExtra("Quarter");
        new GameDetailRetriever(this, tl, nameCol).execute(s, q);

    }

}
