package test.friends.view.fragments;

import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.friends.adapters.FriendsPagerAdapter;
import test.friends.MUT;
import test.friends.R;
import test.friends.model.FriendsModel;

public class FriendsFragment extends Fragment {
    View rootView;
    @BindView(R.id.vp_friends_friends)
    ViewPager friendsPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, rootView);
        setupFriendsPager();
        return rootView;
    }


    private void setupFriendsPager() {
        FriendsPagerAdapter adapter;
        BroadcastReceiver clickedReceiver;
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //padding between pager items
        double pageMargin = ((metrics.widthPixels / 2.90) * 2);
        friendsPager.setPageMargin(-((int) pageMargin));
        final ArrayList<FriendsModel> friends = getArguments().getParcelableArrayList("friendsList");
        adapter = new FriendsPagerAdapter(friendsPager, getActivity(), getChildFragmentManager(), friends);
        friendsPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        friendsPager.addOnPageChangeListener(adapter);

        //clicker to pager items
        clickedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int position = intent.getIntExtra("position", 0);
                if(position==friendsPager.getCurrentItem()) {
                    MUT.sendMessage(getActivity(),friends.get(position).getFirstName()+" "+friends.get(position).getLastName());
                }else {
                    friendsPager.setCurrentItem(position);
                }
            }
        };

        getActivity().registerReceiver(clickedReceiver, new IntentFilter("pagerItemClicker"));
        friendsPager.setCurrentItem(1);
        friendsPager.setOffscreenPageLimit(3);
    }
}
