package test.friends.view.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.VolleyError;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import test.friends.adapters.FriendsListAdapter;
import test.friends.MUT;
import test.friends.R;
import test.friends.model.FriendsModel;
import test.friends.presenter.FriendsPresenter;
import test.friends.presenter.HomePresenter;
import test.friends.view.HomeView;
import test.friends.view.listeners.RecyclerItemClickListener;
import static test.friends.MUT.lToast;
import static test.friends.MUT.showErrorMessages;


public class HomeFragment extends Fragment implements OnMapReadyCallback, HomeView {

    @BindView(R.id.rv_home_friends_list)
    RecyclerView friendsList;
    @BindView(R.id.btn_home_call)
    Button call;
    @BindView(R.id.btn_home_mail)
    Button mail;
    @BindView(R.id.btn_home_message)
    Button message;
    public final static int LOCATION_PERMISSION = 105;
    public final static int PHONE_PERMISSION = 108;
    private Dialog loadingBar;
    View rootView;
    private MapView mapView;
    private GoogleMap googleMap;
    FriendsListAdapter mAdapter;
    private int selectedPosition = 1;
    private ArrayList<FriendsModel> friends;
    private int friendsNumbers = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        loadingBar = MUT.initializeLoading(getActivity());
        initializaMap(rootView, savedInstanceState);
        setupFriendsList();
        setEvents();
        return rootView;
    }


    private void setupFriendsList() {

        //call by value to avoid the effect of modification to the main list which within MainActivity
        //about null not handle because i won't move from the previous page until make sure that there's a data
        friends = new ArrayList(getArguments().getParcelableArrayList("friendsList"));

        //adding two fake items to handle (center) the first and the last item
        friends.add(0, new FriendsModel());
        friends.add(new FriendsModel());

        //save how many friends to save it instead of recount every time i am not sure that it's complexity o(1) or o(n)
        friendsNumbers = friends.size();


        //set first item as selected
        friends.get(1).setSelected(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mAdapter = new FriendsListAdapter(getActivity(), friends);
        friendsList.setHasFixedSize(true);
        friendsList.setLayoutManager(layoutManager);
        friendsList.setItemAnimator(new DefaultItemAnimator());
        friendsList.setAdapter(mAdapter);


        friendsList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //ignore if the user selected on the fake items
                if (position == 0 || position == friendsNumbers) {
                    return;
                }


                //pass selected position to use later when user press on the marker and also to send mail , message ....
                selectedPosition = position;

                //set all items as not selected then set the selected one as selected (color it with yellow)
                for (int i = 0; i < friends.size(); i++) {
                    if (i == position) {
                        friends.get(position).setSelected(true);
                    } else {
                        friends.get(i).setSelected(false);
                    }
                }
                mAdapter.notifyDataSetChanged();
                layoutManager.scrollToPositionWithOffset(position - 1, 0);
                addMarker(new LatLng(friends.get(position).getLatitude(), friends.get(position).getLongitude()));
            }
        }));


    }

    private void setEvents() {
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_PERMISSION);
                    return;
                }
                MUT.makeCall(getActivity(), friends.get(selectedPosition).getCall());
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MUT.sendMail(getActivity(), friends.get(selectedPosition).getEmail());
                } catch (Exception e) {
                    lToast(getActivity(), getResources().getString(R.string.wrong_mail));
                    e.getStackTrace();
                }
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        this.googleMap = googleMap;
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //anyway there's only one item added so any click to item map will be the current item while selected position have the last selected position
                getUserLocation();
                return false;
            }
        });

        //set default marker for the first user
        addMarker(new LatLng(friends.get(selectedPosition).getLatitude(), friends.get(selectedPosition).getLongitude()));
    }

    private void getUserLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION);
            return;
        }
        MUT.turnGPSOn(getActivity());
        LocationManager service = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        Location location = service.getLastKnownLocation(provider);
        if(location!=null) {
            getDistance(new LatLng(location.getLatitude(), location.getLongitude()));
        }else {
            lToast(getActivity(),getResources().getString(R.string.check_gps));
        }
    }

    private void getDistance(LatLng userLocation){
        HomePresenter homePresenter = new HomePresenter(this);
        homePresenter.getDistance(userLocation.latitude,userLocation.longitude,friends.get(selectedPosition).getLatitude(),friends.get(selectedPosition).getLongitude());
    }


    private void showFriendDetails(String distance){
        new BottomDialog.Builder(getActivity())
                .setContent(distance+" "+getResources().getString(R.string.distance_message))
                .setTitle(friends.get(selectedPosition).getFirstName() + " " + friends.get(selectedPosition).getLastName())
                .show();
    }

    private void addMarker(LatLng location) {
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                .position(location));
        animateToLocation(location);
    }

    private void animateToLocation(LatLng location) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(17).bearing(90).tilt(40).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    private void initializaMap(View rootView, Bundle savedInstanceState) {
        MapsInitializer.initialize(getActivity());

        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        if (mapView != null) {
            mapView.getMapAsync(this);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull  int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission was granted,
            if (requestCode == PHONE_PERMISSION) {
                MUT.makeCall(getActivity(), friends.get(selectedPosition).getCall());
            } else if (requestCode == LOCATION_PERMISSION) {
                getUserLocation();
            }

        }


        return;
        // other 'case' lines to check for other
        // permissions this app might request

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
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
    public void onRequestSuccess(Object object) {
        if(object!=null) {
            showFriendDetails((String) object);
        }else{
            lToast(getActivity(),getResources().getString(R.string.cant_get_distance));
        }
        showLoading(false);
    }

    @Override
    public void onRequestError(Object object) {
        showErrorMessages(getActivity(), (VolleyError) object);
        showLoading(false);
    }
}
