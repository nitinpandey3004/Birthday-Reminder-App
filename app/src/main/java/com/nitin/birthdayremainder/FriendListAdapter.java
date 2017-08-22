package com.nitin.birthdayremainder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nitin on 1/10/2017.
 */
public class FriendListAdapter extends BaseAdapter {

    Context context ;
    ArrayList<Friend> friendList;
    LayoutInflater inflater;


    public FriendListAdapter(Context context , ArrayList<Friend> friendList){
        this.context = context ;
        this.friendList = friendList ;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vi = view ;

        if(view == null){
            vi = inflater.inflate(R.layout.list_item_friend,null);
            TextView name = (TextView) vi.findViewById(R.id.tvName);
            TextView dob = (TextView) vi.findViewById(R.id.tvDOB);
            TextView phone = (TextView) vi.findViewById(R.id.tvPhone);
            ImageView photo = (ImageView) vi.findViewById(R.id.ivPhoto);
            //ImageButton imageButton2 = (ImageButton) vi.findViewById(R.id.imageButton2);

            Friend friend = friendList.get(i);
            name.setText(friend.getName());
            dob.setText(friend.getDob());
            phone.setText(friend.getPhone());

            String photoPath = friend.getPhoto();
            Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
            photo.setImageBitmap(bitmap);
        }

        return vi;
    }
}
