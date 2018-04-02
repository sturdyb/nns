package com.example.takomar.nospoilersnba;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.takomar.nospoilersnba.component.DatePickerFragment;
import com.example.takomar.nospoilersnba.component.GameInfo;
import com.example.takomar.nospoilersnba.component.GamesByDayTask;
import com.example.takomar.nospoilersnba.component.IGamesViewUpdater;
import com.example.takomar.nospoilersnba.component.OnSwipeTouchListener;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.takomar.nospoilersnba.Helper.getEndPeriod;
import static com.example.takomar.nospoilersnba.Helper.openHighlights;
import static com.example.takomar.nospoilersnba.Helper.openWatchNbaSite;

/**
 * Created by takomar on 18/03/17.
 */

public abstract class GamesFragment extends Fragment
                                    implements MainFragmentActivity.XmlClickable,
                                               IGamesViewUpdater {
    protected GamesAdaptor mGamesAdaptor;
    private OnSwipeTouchListener swipeTouchListener;
    private Context mContext;

    @Override
    public void fillGames(List<GameInfo> games, Date date) {
        MainFragmentActivity activity = (MainFragmentActivity) mContext;
        activity.addGamesByDate(games, date);
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void displayTodayGames(boolean showDate) {
        MainFragmentActivity activity = (MainFragmentActivity) getGamesContext();
        mGamesAdaptor.showGames(activity.retrieveGamesByDate(mCurrentDate), showDate);
    }

    @Override
    public Context getGamesContext() {
        return mContext;
    }

    @Override
    public View getNoGamesPanel() {
        return mRootView.findViewById(R.id.noGamesPanel);
    }

    @Override
    public View getProgressBar() {
        return mRootView.findViewById(R.id.linlaHeaderProgress);
    }

    @Override
    public void stopRefreshing() {
    }

    protected View mRootView;
    protected Date mCurrentDate;

    public GamesFragment() {
    }

    protected abstract Date getInitialDate();
    protected abstract String formatDisplayDate(Date date);
    protected abstract Date parseToDate(String dateText) throws ParseException;
    protected abstract void incrementCalendar(Calendar cal);
    protected abstract void decrementCalendar(Calendar cal);
    protected abstract void loadGames(Date date);

    private void createGamesAdaptor(View rootView) {
        RecyclerView recViewList = (RecyclerView) rootView.findViewById(R.id.cardList);
        mGamesAdaptor = new GamesAdaptor(getGamesContext());
        recViewList.setAdapter(mGamesAdaptor);

        mGamesAdaptor.SetOnItemClickListener(new GamesAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Context context, GamesAdaptor.GameInfoHolder gameInfo) {
                if (view.getId() == R.id.buttonWatch) {
                    openWatchNbaSite(getGamesContext(), gameInfo.gameCode);
                    return;
                }
                if (view.getId() == R.id.buttonSearch) {
                    openHighlights(getGamesContext(),
                            gameInfo.homeTeam.getText().toString(),
                            gameInfo.visitorTeam.getText().toString());

                    return;
                }
                if (view.getId() == R.id.custom) {
                    Helper.chooseTimeDialog(getGamesContext(), gameInfo);
                    return;
                }

                Helper.showGameDetails(context, gameInfo,
                        getEndPeriod(context, view.getId()));
            }

            @Override
            public void onItemSwipeRight(View v) {
                goToPrevDate(v);
            }

            @Override
            public void onItemSwipeLeft(View v) {
                goToNextDate(v);

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.games_fragment, container, false);

        swipeTouchListener = new OnSwipeTouchListener(getGamesContext())
        {
            @Override
            public void onSwipeRight() {
                goToPrevDate(mRootView);
            }

            @Override
            public void onSwipeLeft() {
                goToNextDate(mRootView);
            }
        };

        final RecyclerView recViewList = (RecyclerView) mRootView.findViewById(R.id.cardList);
        recViewList.setHasFixedSize(true);

        recViewList.setOnTouchListener(swipeTouchListener);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recViewList.setLayoutManager(llm);
        createGamesAdaptor(mRootView);

        Button datePicker = (Button) getActivity().findViewById(R.id.pickDate);
        mCurrentDate = getInitialDate();
        datePicker.setText(formatDisplayDate(mCurrentDate));
        loadGames(mCurrentDate);
        return mRootView;
    }

    @Override
    public void pickDate(View v, AppCompatActivity activity) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(activity.getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void refreshGames(View v) {
        new GamesByDayTask(this).execute(mCurrentDate);
    }

    @Override
    public void treatDate(Date date) {
        mCurrentDate = date;
        Button datePick = (Button) getActivity().findViewById(R.id.pickDate);
        datePick.setText(formatDisplayDate(mCurrentDate));
        loadGames(mCurrentDate);
    }

    @Override
    public void dispatchEvent(MotionEvent evt) {
        swipeTouchListener.getGestureDetector().onTouchEvent(evt);
    }

    @Override
    public void goToNextDate(View v) {
        if (mRootView.findViewById(R.id.linlaHeaderProgress).getVisibility() == View.GONE) {
            Button pickDate = ((Button) getActivity().findViewById(R.id.pickDate));
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
            treatDate(calendar.getTime());
        }
    }

    @Override
    public void goToPrevDate(View v) {
        if (mRootView.findViewById(R.id.linlaHeaderProgress).getVisibility() == View.GONE) {
            Button pickDate = ((Button) getActivity().findViewById(R.id.pickDate));
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

            treatDate(calendar.getTime());
        }
    }
    @Override
    public void goToInitialDate(View v) {
        treatDate(getInitialDate());
    }
    @Override
    public void goToCurrentStandings(View v) throws ParseException {

    }
}
