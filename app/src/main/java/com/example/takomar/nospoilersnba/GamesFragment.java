package com.example.takomar.nospoilersnba;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.takomar.nospoilersnba.component.CacheExecutor;
import com.example.takomar.nospoilersnba.component.DatePickerFragment;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by takomar on 18/03/17.
 */

public class GamesFragment extends Fragment implements MainFragmentActivity.XmlClickable {
    protected SimpleGamesAdaptor mGamesAdaptor;
    protected View mRootView;

    public GamesFragment() {
    }

    protected Date getInitialDate() {
        return null;
    }

    protected String formatDisplayDate(Date date) {
        return null;
    }

    protected Date parseToDate(String dateText) throws ParseException {
        return null;
    }

    private void createGamesAdaptor(View rootView) {
        RecyclerView recViewList = (RecyclerView) rootView.findViewById(R.id.cardList);
        mGamesAdaptor = new SimpleGamesAdaptor(getActivity());
        recViewList.setAdapter(mGamesAdaptor);

        mGamesAdaptor.SetOnItemClickListener(new SimpleGamesAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(
                    View view, Context context,
                    SimpleGamesAdaptor.GameInfoHolder gameInfo) {
                if(view.getId() == R.id.buttonSearch) {
                    String query =
                            gameInfo.homeTeam.getText() + " " +
                            gameInfo.visitorTeam.getText() +
                            " ximo pierto final";

                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, query);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                    return;
                }
                Helper.showGameDetails(context, gameInfo, view.getId());
            }
        });
    }

    protected void loadCacheByDate(MainFragmentActivity activity, Date gameDate) {
        if (!activity.alreadyLoadedGames(gameDate))
            activity.addCacheTasks((SimpleGamesRetriever)
                    new SimpleGamesRetriever(activity, new CacheExecutor(activity)).
                            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gameDate));
    }

    protected void loadGames(Date date) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.games_fragment, container, false);
        RecyclerView recViewList = (RecyclerView) mRootView.findViewById(R.id.cardList);
        recViewList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recViewList.setLayoutManager(llm);
        createGamesAdaptor(mRootView);

        final SwipeRefreshLayout swipeContainer =
                (SwipeRefreshLayout) mRootView.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);
            }

        });

        Button datePicker = (Button) getActivity().findViewById(R.id.pickDate);
        Date initialDate = getInitialDate();
        datePicker.setText(formatDisplayDate(initialDate));
        loadGames(initialDate);
        return mRootView;
    }

    protected void incrementCalendar(Calendar cal) {}
    protected void decrementCalendar(Calendar cal) {}

    public void goToNextDate(View v) {
        Button pickDate = ((Button)getActivity().findViewById(R.id.pickDate));
        String text = (String) pickDate.getText();
        Date currentDate = null;
        try {
            currentDate = parseToDate(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        incrementCalendar(calendar);
        loadGames(calendar.getTime());
        pickDate.setText(formatDisplayDate(calendar.getTime()));
    }

    @Override
    public void pickDate(View v, AppCompatActivity activity) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(activity.getSupportFragmentManager(), "datePicker");
        Button pickDate = ((Button)getActivity().findViewById(R.id.pickDate));
        String text = (String) pickDate.getText();
        Date currentDate = null;
        try {
            currentDate = parseToDate(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        loadGames(calendar.getTime());
    }

    public void goToPrevDate(View v) {
        Button pickDate = ((Button)getActivity().findViewById(R.id.pickDate));
        String text = (String) pickDate.getText();
        Date currentDate = null;
        try {
            currentDate = parseToDate(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        decrementCalendar(calendar);
        loadGames(calendar.getTime());
        pickDate.setText(formatDisplayDate(calendar.getTime()));
    }
}
