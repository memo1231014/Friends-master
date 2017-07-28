package test.friends.presenter;

import com.android.volley.AuthFailureError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import test.friends.model.FriendsModel;
import test.friends.view.FriendsView;
import test.friends.view.HomeView;


public class HomePresenter {
    private HomeView homeView;
    private RequestQueue queue;
    public HomePresenter(HomeView loginView) {
        this.homeView = loginView;
    }

    public void getDistance(double latitude, double longitude,double toLatitude, double toLongitude) {

        queue = Volley.newRequestQueue(homeView.getContext());
        String url = "http://maps.google.com/maps/api/directions/json?origin="
                + latitude + "," + longitude + "&destination=" + toLatitude
                + "," + toLongitude + "&mode=driving&sensor=false&units=metric";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response;
                            JSONArray array = jsonObject.getJSONArray("routes");
                            JSONObject routes = array.getJSONObject(0);
                            JSONArray legs = routes.getJSONArray("legs");
                            JSONObject steps = legs.getJSONObject(0);
                            JSONObject distance = steps.getJSONObject("distance");
                            homeView.onRequestSuccess(distance.getString("text"));
                        }
                        catch (JSONException e){
                            homeView.onRequestError(null);
                            e.getStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                homeView.onRequestError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        homeView.showLoading(true);
        queue.add(jsonObjReq);
    }





}
