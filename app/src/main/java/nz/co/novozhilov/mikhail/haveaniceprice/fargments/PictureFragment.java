package nz.co.novozhilov.mikhail.haveaniceprice.fargments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import nz.co.novozhilov.mikhail.haveaniceprice.R;
import nz.co.novozhilov.mikhail.haveaniceprice.services.HaveANicePriceService;

/**
 * A placeholder fragment containing a simple view.
 */
public class PictureFragment extends Fragment implements View.OnClickListener {

    public PictureFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Intent alarmIntent = new Intent(getActivity(), HaveANicePriceService.AlarmReceiver.class);
//
//        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
//        AlarmManager am = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
//
//        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000, pi);
//
//        Intent intent = new Intent(getActivity(), HaveANicePriceService.class);
//        getActivity().startService(intent);


        View rootView = inflater.inflate(R.layout.picture_fragment, container, false);

        return rootView;

    }

    @Override
    public void onClick(View v) {

    }
}
