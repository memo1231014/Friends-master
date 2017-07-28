package test.friends.presenter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.android.volley.VolleyError;

import test.friends.SqlLiteHelper;
import test.friends.model.FriendsModel;
import test.friends.view.FriendsView;


public class FriendsPresenter {
    private FriendsView friendsView;
    private RequestQueue queue;
    private SqlLiteHelper sqlLiteHelper;
    public FriendsPresenter(FriendsView friendsView) {
        this.friendsView = friendsView;
    }

    public void getFriends() {
        sqlLiteHelper = new SqlLiteHelper(friendsView.getContext());
        queue = Volley.newRequestQueue(friendsView.getContext());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "https://www.digi-worx.com/friends.txt", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<FriendsModel> friends = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("friends");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                FriendsModel friendsModel = new FriendsModel();
                                friendsModel.setFirstName(jsonObject.getString("first_name"));
                                friendsModel.setLastName(jsonObject.getString("last_name"));
                                friendsModel.setEmail(jsonObject.getString("email"));
                                friendsModel.setCall(jsonObject.getString("call"));
                                //creating fake ID to avoid repeating items when add to offline (just fast solution until add unique IDS)
                                friendsModel.setId(jsonObject.getString("id")+jsonObject.getString("first_name"));
                                friendsModel.setLatitude(jsonObject.getJSONArray("location").getDouble(0));
                                friendsModel.setLongitude(jsonObject.getJSONArray("location").getDouble(1));
                                //add friend to DB to use later if the mobile is offline
                                sqlLiteHelper.addFriend(friendsModel);
                                friends.add(friendsModel);
                            }
                                friendsView.onRequestSuccess(friends);
                            sqlLiteHelper.closeDbConnection();
                        } catch (Exception e) {
                            friendsView.onRequestError(null);
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                friendsView.onRequestError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        friendsView.showLoading(true);
        queue.add(jsonObjReq);
    }



}
