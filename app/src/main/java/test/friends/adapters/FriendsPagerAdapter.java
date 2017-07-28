package test.friends.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import java.util.ArrayList;
import test.friends.view.customviews.CarouselRelativeLayout;
import test.friends.R;
import test.friends.model.FriendsModel;
import test.friends.view.fragments.ItemFragment;

public class FriendsPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    private Context context;
    private FragmentManager fragmentManager;
    private ArrayList<FriendsModel> friends;
    private ViewPager viewPager;
    private float scale;

    public FriendsPagerAdapter(ViewPager viewPager, Context context, FragmentManager fm, ArrayList<FriendsModel> friends) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
        this.friends = friends;
        this.viewPager = viewPager;
    }

    @Override
    public Fragment getItem(int position) {
        scale = SMALL_SCALE;
        position = position % friends.size();
        return ItemFragment.newInstance(context, position, scale, friends.get(position));
    }

    @Override
    public int getCount() {

        return friends.size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        try {
            if (positionOffset >= 0f && positionOffset <= 1f) {
                CarouselRelativeLayout cur = getRootView(position);
                CarouselRelativeLayout next = getRootView(position + 1);
                cur.setScaleBoth(BIG_SCALE - DIFF_SCALE * positionOffset);
                next.setScaleBoth(SMALL_SCALE + DIFF_SCALE * positionOffset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @SuppressWarnings("ConstantConditions")
    private CarouselRelativeLayout getRootView(int position) {
        return (CarouselRelativeLayout) fragmentManager.findFragmentByTag(this.getFragmentTag(position))
                .getView().findViewById(R.id.tv_friend_circle_root_container);
    }


    private String getFragmentTag(int position) {
        return "android:switcher:" + viewPager.getId() + ":" + position;
    }
}