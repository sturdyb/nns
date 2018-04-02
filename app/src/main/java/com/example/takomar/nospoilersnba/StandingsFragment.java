package com.example.takomar.nospoilersnba;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.takomar.nospoilersnba.component.DatePickerFragment;
import com.example.takomar.nospoilersnba.component.IStandingsViewUpdater;
import com.example.takomar.nospoilersnba.component.Retro.RetroApi;
import com.example.takomar.nospoilersnba.component.Retro.RetroInterface;
import com.example.takomar.nospoilersnba.component.Retro.Standings.Standings;
import com.example.takomar.nospoilersnba.component.Retro.Standings.StandingsCallback;
import com.example.takomar.nospoilersnba.component.TeamStanding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

/**
 * Created by takomar on 18/03/17.
 */

public class StandingsFragment
        extends Fragment
        implements MainFragmentActivity.XmlClickable, IStandingsViewUpdater
{

    private SimpleDateFormat mDailyFormat = new SimpleDateFormat("EEE, MMM d yyyy");
    private View mRootView;
    private TableLayout mWest;
    private TableLayout mEast;
    private LinearLayout mProgress;
    private Context mContext;

    public StandingsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        getActivity().findViewById(R.id.standingsDate).setVisibility(View.INVISIBLE);
    }

    private Date getInitialDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, -1);

        return cal.getTime();
    }

    void loadStandings(Date date) {
        SimpleDateFormat apiFormat = new SimpleDateFormat("yyyyMMdd");
        RetroInterface apiService = RetroApi.getClient().create(RetroInterface.class);
        Call<Standings> call = apiService.getStandings(apiFormat.format(date));
        call.enqueue(new StandingsCallback(this));


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.standings_fragment, container, false);
        mEast = (TableLayout) mRootView.findViewById(R.id.standingsEast);
        mWest = (TableLayout) mRootView.findViewById(R.id.standingsWest);
        mProgress = (LinearLayout)mRootView.findViewById(R.id.linlaHeaderProgress);

        Bundle dateToShow = this.getArguments();

        Date initialDate;
        initialDate = dateToShow != null ? getDateFromBundle(dateToShow) : getInitialDate();

        Button datePicker = (Button) getActivity().findViewById(R.id.pickDate);
        datePicker.setText(formatDisplayDate(initialDate));
        loadStandings(initialDate);
        return mRootView;
    }

    private Date getDateFromBundle(Bundle dateToShow) {
        Date initialDate;
        Long currDate = dateToShow.getLong("currentDate");
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(currDate);
        initialDate = (Date) c.getTime().clone();
        return initialDate;
    }

    protected String formatDisplayDate(Date date) {
        return mDailyFormat.format(date);
    }

    protected Date parseToDate(String dateText) throws ParseException {
        return mDailyFormat.parse(dateText);
    }

    protected void incrementCalendar(Calendar cal) {
        cal.add(Calendar.DATE, 1);
    }

    protected void decrementCalendar(Calendar cal) {
        cal.add(Calendar.DATE, -1);
    }

    @Override
    public void refreshGames(View v) {}

    @Override
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
        treatDate(calendar.getTime());
    }

    @Override
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
        treatDate(calendar.getTime());
    }

    @Override
    public void goToInitialDate(View v) {
        treatDate(getInitialDate());
    }

    @Override
    public void pickDate(View v, AppCompatActivity activity) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(activity.getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void treatDate(Date date) {
        Button datePick = (Button) getActivity().findViewById(R.id.pickDate);
        datePick.setText(formatDisplayDate(date));
        loadStandings(date);
    }

    @Override
    public void dispatchEvent(MotionEvent evt) {

    }

    @Override
    public void goToCurrentStandings(View v) {

    }

    @Override
    public void loading() {
        mEast.removeAllViews();
        mWest.removeAllViews();
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void loaded() {
        mProgress.setVisibility(View.GONE);
    }

    private void createHeaders(TableLayout body, String title)
    {
        TableRow header =
                (TableRow) LayoutInflater.from(mContext)
                                         .inflate(R.layout.standings_header, null);

        header.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        int color = mContext.getResources().getColor(R.color.cardview_light_background);

        TextView headerName = ((TextView)header.findViewById(R.id.conf));
        headerName.setTextColor(color);
        headerName.setText(title);
        headerName.setTypeface(null, Typeface.BOLD);

        ((TextView)header.findViewById(R.id.winLoss)).setText("W-L");
        ((TextView)header.findViewById(R.id.wlpct)).setText("Pct");

        body.addView(header);
    }

    private void fillStats(TeamStanding stats, TableLayout body, int line)
    {
        TableRow row =
                (TableRow) LayoutInflater.from(mContext)
                                         .inflate(R.layout.standings_detail, null);

        if(line == 9)
            body.addView((TableRow) LayoutInflater.from(mContext)
                .inflate(R.layout.standings_detail, null)); //separation of qualified

        int imageResource =
                mContext.getResources().getIdentifier(
                        Helper.getImageByTeam(stats.name),
                        null, mContext.getPackageName());

        ((ImageView)row.findViewById(R.id.logo)).setImageDrawable(mContext.getResources().getDrawable(imageResource));
        ((TextView)row.findViewById(R.id.team)).setText(stats.name);
        ((TextView)row.findViewById(R.id.winLoss)).setText(stats.wins + "-" + stats.losses);
        ((TextView)row.findViewById(R.id.wlpct)).setText(stats.pct);
        body.addView(row);
    }

    @Override
    public void fillEastStandings(List<TeamStanding> standings) {
        createHeaders(mEast, "East Conference");
        int i =0;
        for (TeamStanding tStanding : standings)
            fillStats(tStanding, mEast, ++i);

        mEast.requestLayout();
    }

    @Override
    public void fillWestStandings(List<TeamStanding> standings) {
        createHeaders(mWest, "West Conference");
        int i =0;
        for (TeamStanding tStanding : standings)
            fillStats(tStanding, mWest, ++i);

        mWest.requestLayout();
    }
}