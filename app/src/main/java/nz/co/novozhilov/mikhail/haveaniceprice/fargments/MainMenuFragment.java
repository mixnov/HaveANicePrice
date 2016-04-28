package nz.co.novozhilov.mikhail.haveaniceprice.fargments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import nz.co.novozhilov.mikhail.haveaniceprice.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainMenuFragment extends Fragment implements View.OnClickListener {

    public MainMenuFragment() {
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

        getActivity().setTitle(R.string.app_name);

        // get main menu fragment layout
        View fragment_view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        // get buttons from fragment's layout
        Button btnMainShops = (Button) fragment_view.findViewById(R.id.btn_main_shops);
        Button btnMainWishList = (Button) fragment_view.findViewById(R.id.btn_main_wish_list);
        Button btnMainStatistics = (Button) fragment_view.findViewById(R.id.btn_main_statistics);
        Button btnMainAbout = (Button) fragment_view.findViewById(R.id.btn_main_about);
        Button btnMainFeedback = (Button) fragment_view.findViewById(R.id.btn_main_feedback);

        // set implemented method onClick as the onClickListener
        btnMainShops.setOnClickListener(this);
        btnMainWishList.setOnClickListener(this);
        btnMainStatistics.setOnClickListener(this);
        btnMainAbout.setOnClickListener(this);
        btnMainFeedback.setOnClickListener(this);

        return fragment_view;
    }

    @Override
    public void onClick(View v) {
        // action depends on the view's id
        switch (v.getId()) {
            case R.id.btn_main_shops:
                // start java test menu fragment
//                startFragment(new MenuJavaFragment());
                break;
            case R.id.btn_main_wish_list:
                // open menu for a c test
//                startFragment(new MenuCFragment());
                break;
            case R.id.btn_main_statistics:
                // show statistics by groups (java/c):
//                startFragment(new StatisticsFragment());
                break;
            case R.id.btn_main_about:
                // show statistics by groups (java/c):
                startFragment(new AboutFragment());
                break;
            case R.id.btn_main_feedback:
                // show statistics by groups (java/c):
//                startFragment(new StatisticsFragment());
                break;
        }

    }

    /**
     * Replace current fragment with the next one
     *
     * @param fragment - next fragment
     */
    private void startFragment(android.app.Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mmf, fragment);
        transaction.addToBackStack(null); //add to stack for proper back navigation
        transaction.commit();
    }

}
