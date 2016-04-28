package nz.co.novozhilov.mikhail.haveaniceprice.fargments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nz.co.novozhilov.mikhail.haveaniceprice.R;

/**
 * Dialog About this app
 *
 * @author Mikhail Novozhilov novomic@gmail.com
 */
public final class AboutFragment extends DialogFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_about, container, false);
        TextView aboutText = (TextView)fragmentView.findViewById(R.id.about_app);
        if (aboutText != null) {
            aboutText.setMovementMethod(LinkMovementMethod.getInstance());
        }

        return fragmentView;
    }

}
