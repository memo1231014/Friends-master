package test.friends.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import test.friends.MUT;
import test.friends.R;
import test.friends.model.FriendsModel;


public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.MyViewHolder> {

    private ArrayList<FriendsModel> friends;
    private Context context;

     class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

         MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_friend_square_name);
        }
    }


    public FriendsListAdapter(Context context, ArrayList<FriendsModel> friends) {
        this.friends = friends;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend_square_style, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FriendsModel friend = friends.get(position);
        holder.name.setText(friend.getFirstName());
        holder.name.setWidth(MUT.getScreenWidth(context) / 3);
        if (position == 0 || position == getItemCount() - 1) {
            //make fake items transparent
            holder.name.setBackgroundResource(R.color.transparent);
        } else {
            if (friend.isSelected()) {
                holder.name.setBackgroundResource(R.drawable.tv_border_selected_background);
            } else {
                holder.name.setBackgroundResource(R.drawable.tv_border_unselected_background);
            }
        }
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

}
