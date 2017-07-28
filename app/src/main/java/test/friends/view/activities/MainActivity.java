package test.friends.view.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import test.friends.MUT;
import test.friends.R;
import test.friends.SqlLiteHelper;
import test.friends.model.FriendsModel;
import test.friends.presenter.FriendsPresenter;
import test.friends.view.FriendsView;
import test.friends.view.fragments.FriendsFragment;
import test.friends.view.fragments.HomeFragment;
import test.friends.view.fragments.SendMessageFragment;

import static test.friends.MUT.*;

public class MainActivity extends AppCompatActivity implements FriendsView {
    @BindView(R.id.iv_main_page_change_style)
    ImageView changeStyle;
    @BindView(R.id.iv_main_page_refresh)
    ImageView refresh;
    private FriendsPresenter friendsPresenter;
    private Dialog loadingBar;
    private ArrayList<FriendsModel> friends;
    private int pageType = 1;
    private SqlLiteHelper sqlLiteHelper;
    private boolean doubleBackToExitPressedOnce = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sqlLiteHelper = new SqlLiteHelper(this);
        loadingBar = initializeLoading(this);
        friendsPresenter = new FriendsPresenter(this);
        friendsPresenter.getFriends();
        setEvents();
    }

    @Override
    public void showLoading(boolean state) {
        if (state) {
            loadingBar.show();
        } else {
            loadingBar.cancel();
        }
    }

    @Override
    public void onRequestSuccess(Object friends) {
        this.friends = (ArrayList<FriendsModel>)friends;

        if(this.friends==null||this.friends.size()<1){
            //try to get from offline
            getOfflineData();
        }

        //if friends list still empty so he no have friends
        if(this.friends==null||this.friends.size()<1){
            lToast(MainActivity.this,getResources().getString(R.string.no_friends));
            return;
        }

        moveToPage(pageType);
        showLoading(false);
    }

    @Override
    public void onRequestError(Object object) {
        showErrorMessages(MainActivity.this, (VolleyError) object);
        showLoading(false);
        //try to get from offline
        getOfflineData();


        moveToPage(pageType);

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }


    private void setEvents() {
        changeStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                moveToPage(pageType);
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageType=pageType==1?2:1;
                friendsPresenter.getFriends();
            }
        });
    }


    private void moveToPage(int pageType) {

        //to avoid adding many fragments when he go to sendMessage from listPage then press to switch button then go to the other page and add send message fragment from pager also on
        getSupportFragmentManager().popBackStack();

        if(this.friends==null||this.friends.size()<1){
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("friendsList", friends);
        Fragment fragment ;
        if (pageType == 1) {
            fragment = new FriendsFragment();
            this.pageType = 2;
        } else {
            fragment = new HomeFragment();
            this.pageType = 1;
        }
        fragment.setArguments(bundle);
        addFragment(MainActivity.this, fragment, "");
    }

    private void getOfflineData(){
        this.friends = sqlLiteHelper.getFriends();
    }
    @Override
    public void onBackPressed() {


            if (!doubleBackToExitPressedOnce) {
                finish();
            } else {
                if (getSupportFragmentManager().getBackStackEntryCount() < 1) {
                    this.doubleBackToExitPressedOnce = false;
                    Toast.makeText(this, getResources().getString(R.string.double_click_to_close), Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = true;
                        }
                    }, 2000);
                } else {
                    super.onBackPressed();
                }

            }
    }


}