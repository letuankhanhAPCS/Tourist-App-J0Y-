package com.example.mobileproject;

import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class getDataFromServer<service, DatabaseReference> {
    private FirebaseFirestore db; //kaz
    private static final String KEY_NAME = "name";
    private static final String KEY_ADD = "address";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_TIMEZONE = "timeZone";
    private static final String KEY_NOOFRATING = "numberOfRatings";
    private static final String KEY_RATING = "rating";
    private static final String KEY_LONGLAT = "latlng";
    private static final String KEY_IMG1 = "img1";
    private static final String KEY_IMG2 = "img2";
    private static final String KEY_IMG3 = "img3";

    private Context context;

    public getDataFromServer(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    public interface MyCallback {
        void onCallback(ArrayList<MyLocation> myLocations);
    }

    public void loadLocation(String type, final MyCallback myCallback) {
        final ArrayList<MyLocation> myLocations = new ArrayList<>();
        db.collection(type).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d : list){
                        myLocations.add(initLocation(d));
                    }
                    myCallback.onCallback(myLocations);
                }
            }
        });
    }

    private MyLocation initLocation(DocumentSnapshot d){
        String name = d.getString(KEY_NAME);
        String address = d.getString(KEY_ADD);
        String description = d.getString(KEY_DESCRIPTION);

        int noOfRating = Objects.requireNonNull(d.getLong(KEY_NOOFRATING)).intValue();;

        double rating = Objects.requireNonNull(d.getDouble(KEY_RATING));

        GeoPoint latLng = d.getGeoPoint(KEY_LONGLAT);
        double lat = Objects.requireNonNull(latLng).getLatitude();
        double lng = latLng.getLongitude();

        String timeZone = d.getString(KEY_TIMEZONE);
        String[] split = Objects.requireNonNull(timeZone).split("–", 2);
        MyTimeZone tz = new MyTimeZone(split[0], split[1]);

        String img1 = d.getString(KEY_IMG1);
        String img2 = d.getString(KEY_IMG2);
        String img3 = d.getString(KEY_IMG3);

        String[] img= {img1, img2, img3};

        return new MyLocation(name, address, description, noOfRating, rating, lat, lng, img, tz);
    }

    /*public void loadLocation(ArrayList<MyLocation> location, String type) {
        for(int i = 0; i < 5; i++) {
            String tmp = type + i;
            MyLocation myLocation = new MyLocation(tmp, tmp, "description",
                    12, 5.0, 10.7 + (double)i/100, 106.6 + (double)i/100,
                    new String[0], new MyTimeZone("7:00", "22:00"));
            location.add(myLocation);
        }
    }*/
}
