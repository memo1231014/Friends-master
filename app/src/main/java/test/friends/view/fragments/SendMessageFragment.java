package test.friends.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import test.friends.MUT;
import test.friends.R;

public class SendMessageFragment extends Fragment {
    View rootView;
    @BindView(R.id.tv_send_message_friend_name)
    TextView friendName;
    @BindView(R.id.et_send_message_details)
    EditText messageDetails;
    @BindView(R.id.btn_send_message_send)
    Button sendMessage;

    ViewPager friendsPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_send_message, container, false);
        ButterKnife.bind(this, rootView);
        friendName.setText(getResources().getString(R.string.message_to)+" "+getArguments().getString("friendName"));
        events();
        return rootView;
    }

    private boolean validate() {
        if (messageDetails.getText().length() < 1) {
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_message), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void events() {
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    MUT.lToast(getActivity(), getResources().getString(R.string.message_sent));
                    messageDetails.setText("");
                }
            }
        });
    }


}
