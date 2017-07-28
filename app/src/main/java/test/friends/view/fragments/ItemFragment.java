package test.friends.view.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import test.friends.view.customviews.CarouselRelativeLayout;
import test.friends.MUT;
import test.friends.R;
import test.friends.model.FriendsModel;

public class ItemFragment extends Fragment {
    @BindView(R.id.tv_friend_circle_name)
    TextView friendName;
    @BindView(R.id.tv_friend_circle_root_container)
    CarouselRelativeLayout rootContainer;
    private int screenWidth;

    public static Fragment newInstance(Context context, int pos, float scale, FriendsModel friendsModel) {
        Bundle b = new Bundle();
        b.putInt("position", pos);
        b.putFloat("scale", scale);
        b.putParcelable("friendDetails", friendsModel);
        return Fragment.instantiate(context, ItemFragment.class.getName(), b);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        RelativeLayout linearLayout = (RelativeLayout) inflater.inflate(R.layout.item_friend_circle_style, container, false);
        ButterKnife.bind(this, linearLayout);
        setupFriendItem();
        return linearLayout;
    }


    private void setupFriendItem() {
        final int position = this.getArguments().getInt("position");
        float scale = this.getArguments().getFloat("scale");
        FriendsModel friendsModel = this.getArguments().getParcelable("friendDetails");
        friendName.setText(friendsModel.getFirstName());
        screenWidth = MUT.getScreenWidth(getActivity());
        //here i get screen width then divide it to 3 to make only 3 visible items on the screen i also added 5 to manage padding and margins
        friendName.getLayoutParams().height = (screenWidth / 3) + 5;
        friendName.getLayoutParams().width = (screenWidth / 3) + 5;
        friendName.setClickable(false);
        friendName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clickerIntent = new Intent("pagerItemClicker");
                clickerIntent.putExtra("position",position);
                getActivity().sendBroadcast(clickerIntent);
            }
        });
        rootContainer.setScaleBoth(scale);
    }


}
