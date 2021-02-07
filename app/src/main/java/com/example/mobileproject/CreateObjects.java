package com.example.mobileproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

public class CreateObjects {

    GridLayout _gridLayout;
    Context context;

    public CreateObjects(GridLayout _gridLayout, Context context) {
        this._gridLayout = _gridLayout;
        this.context = context;
    }

    public GridLayout get_gridLayout() {
        return _gridLayout;
    }

    public void set_gridLayout(GridLayout _gridLayout) {
        this._gridLayout = _gridLayout;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void createNumberOfImageBar(int numberOfImages) {
        GridLayout gridLayout = _gridLayout;
        int width = getScreenWidth();
        for (int i = 0; i < numberOfImages; i++) {
            Button button = new Button(context);
            button.setId(i + 100);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width/numberOfImages, ViewGroup.LayoutParams.MATCH_PARENT);
            button.setLayoutParams(layoutParams);
            gridLayout.addView(button);
        }
    }

//    @SuppressLint("ResourceType")
//    public void createRatingStars(double numberOfStars) {
//        GridLayout gridLayout = _gridLayout.findViewById(R.id.gridRatingStars);
//        int width = getScreenWidth();
//        int fullStar = (int)numberOfStars;
//        double halfStar = numberOfStars - fullStar;
//        for (int i = 0; i < fullStar; i++) {
//            ImageView imageView = new ImageView(context);
//            imageView.setId(i + 100);
//            imageView.setImageResource(R.drawable.rating_star);
//            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width/10, ViewGroup.LayoutParams.MATCH_PARENT);
//            imageView.setLayoutParams(layoutParams);
//            gridLayout.addView(imageView);
//        }
//        if(halfStar == 0.5) {
//            ImageView imageView = new ImageView(context);
//            imageView.setId(110);
//            imageView.setImageResource(R.drawable.rating_star_half);
//            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width/20, ViewGroup.LayoutParams.MATCH_PARENT);
//            imageView.setLayoutParams(layoutParams);
//            gridLayout.addView(imageView);
//        }
//    }
}
