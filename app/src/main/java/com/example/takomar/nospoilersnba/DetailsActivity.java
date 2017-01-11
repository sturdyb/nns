package com.example.takomar.nospoilersnba;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private int getDpInPx(int dp){
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void createHeaders(String teamName, TableLayout namesCol, TableLayout body, boolean isAway)
    {
        TableRow nameHeader = (TableRow) LayoutInflater.from(this).inflate(R.layout.name_col, null);
        TextView headerName = ((TextView)nameHeader.findViewById(R.id.name));
        headerName.setTextColor(getResources().getColor(R.color.cardview_light_background));
        if(isAway)
            nameHeader.setBackgroundColor(getResources().getColor(R.color.red));
        else
            nameHeader.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        int dpAsPixels = getDpInPx(3);
        headerName.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
        headerName.setText(teamName);
        headerName.setTypeface(null, Typeface.BOLD);
        namesCol.addView(nameHeader);

        TableRow header = (TableRow) LayoutInflater.from(this).inflate(R.layout.header_line, null);
        if(isAway)
            header.setBackgroundColor(getResources().getColor(R.color.red));
        else
            header.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ((TextView)header.findViewById(R.id.min)).setText("MIN");
        ((TextView)header.findViewById(R.id.pts)).setText("PTS");
        ((TextView)header.findViewById(R.id.fgm)).setText("FG");
        ((TextView)header.findViewById(R.id.tgm)).setText("3PTS");
        ((TextView)header.findViewById(R.id.reb)).setText("REB");
        ((TextView)header.findViewById(R.id.stl)).setText("STL");
        ((TextView)header.findViewById(R.id.ass)).setText("ASS");
        ((TextView)header.findViewById(R.id.blk)).setText("BLK");
        ((TextView)header.findViewById(R.id.to)).setText("TO");
        ((TextView)header.findViewById(R.id.fouls)).setText("FLS");
        body.addView(header);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final String home = getIntent().getStringExtra("Home");
        final String away = getIntent().getStringExtra("Away");
        final String gameID= getIntent().getStringExtra("GameID");
        final String quarter= getIntent().getStringExtra("Quarter");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(home + " vs " + away);

        Button overtime = (Button)findViewById(R.id.overtime);
        overtime.setVisibility(View.GONE);
        overtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q = Integer.parseInt(quarter) + 3000;
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("GameID", gameID);
                intent.putExtra("Home", home);
                intent.putExtra("Away", away);
                intent.putExtra("Quarter", "" + q);

                startActivity(intent);
            }
        });

        TableLayout nameCol=(TableLayout)findViewById(R.id.namesTable1);
        TableLayout tl=(TableLayout)findViewById(R.id.boxScore1);
        createHeaders(home, nameCol, tl, false);

        TableLayout nameCol2=(TableLayout)findViewById(R.id.namesTable2);
        TableLayout tl2=(TableLayout)findViewById(R.id.boxScore2);
        createHeaders(away, nameCol2, tl2, true);

        new GameDetailRetriever(this, home, tl, nameCol, away, tl2, nameCol2).execute(gameID, quarter);

    }

}
