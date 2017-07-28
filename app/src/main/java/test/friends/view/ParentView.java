package test.friends.view;


import android.content.Context;

public interface ParentView {

    //parent view which will have methods that will be used with many others views

    void showLoading(boolean state);
    void onRequestSuccess(Object object);
    void onRequestError(Object object);
    Context getContext();

}
