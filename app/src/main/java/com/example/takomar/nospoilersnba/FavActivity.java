package com.example.takomar.nospoilersnba;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FavActivity extends MainActivity {

    private SimpleDateFormat dateFormatApp = new SimpleDateFormat("MMM d, yyyy");
    private FavGamesAdaptor gamesAdaptor;

    @Override
    protected Date getInitialDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        return  cal.getTime();
    }

    @Override
    protected void setContents() {
        setContentView(R.layout.activity_fav);
    }

    @Override
    protected void createGamesAdaptor() {
        RecyclerView recViewList = (RecyclerView) findViewById(R.id.cardList);
        gamesAdaptor = new FavGamesAdaptor(this);
        recViewList.setAdapter(gamesAdaptor);

        gamesAdaptor.SetOnItemClickListener(new FavGamesAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Context context, FavGamesAdaptor.GameInfoHolder gameInfo) {
                if(view.getId() == R.id.buttonSearch) {
                    String query =
                            gameInfo.homeTeam.getText() + " " +
                                    gameInfo.visitorTeam.getText() +
                                    " ximo pierto final";
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, query);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                    return;
                }
                Helper.showGameDetails(context, gameInfo, view.getId());
            }
        });
    }

    private void loadGames(Date startDate) {
        gamesAdaptor.changeDate(startDate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onCreateSuper(savedInstanceState);
        setContents();
        onCreateElements();
        checkDrawerListItem(1);
        createGamesAdaptor();

        Button datePicker = (Button) findViewById(R.id.pickDate);
        datePicker.setText("Week of " + dateFormatApp.format(getInitialDate()));
        loadGames(getInitialDate());
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        checkDrawerListItem(1);
    }
    @Override
    public void showDatePickerDialog(View v) {}

    @Override
    public void goToNextDate(View v) {
        if (findViewById(R.id.linlaHeaderProgress).getVisibility() == View.GONE)
        {
            Button pickDate = ((Button)findViewById(R.id.pickDate));
            String text = (String) pickDate.getText();

            String dateText = text.substring(8);//length of week of_
            try {
                Date currentDate = dateFormatApp.parse(dateText);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.add(Calendar.DATE, 7);
                String display = dateFormatApp.format(calendar.getTime());

                loadGames(calendar.getTime());

                pickDate.setText("Week of " + display);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void goToPrevDate(View v) {
        if (findViewById(R.id.linlaHeaderProgress).getVisibility() == View.GONE)
        {
            Button pickDate = ((Button)findViewById(R.id.pickDate));
            String text = (String) pickDate.getText();

            String dateText = text.substring(8); //length of week of_
            try {
                Date currentDate = dateFormatApp.parse(dateText);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.add(Calendar.DATE, -7);
                String display = dateFormatApp.format(calendar.getTime());

                loadGames(calendar.getTime());

                pickDate.setText("Week of " + display);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

}
