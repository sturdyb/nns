package com.example.takomar.nospoilersnba;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        TableLayout tl=(TableLayout)findViewById(R.id.tableBox);

        TableRow header = (TableRow) LayoutInflater.from(this).inflate(R.layout.header_line, null);
        ((TextView)header.findViewById(R.id.name)).setText("Name");
        ((TextView)header.findViewById(R.id.pos)).setText("Pos");
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
        new GameDetailRetriever(this, tl).execute(s, q);

    }

}
