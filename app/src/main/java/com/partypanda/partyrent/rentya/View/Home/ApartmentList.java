package com.partypanda.partyrent.rentya.View.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.partypanda.partyrent.rentya.Adapters.LoadingScreen;
import com.partypanda.partyrent.rentya.Adapters.ResultAdapter;
import com.partypanda.partyrent.rentya.MainActivity;
import com.partypanda.partyrent.rentya.Model.Rental;
import com.partypanda.partyrent.rentya.Model.SavedAd;
import com.partypanda.partyrent.rentya.R;
import com.partypanda.partyrent.rentya.View.Search.EmptyResultList;

import java.util.ArrayList;
import java.util.List;

public class ApartmentList extends AppCompatActivity {


    private TextView back;
    private RecyclerView resultLists;
    private FirebaseFirestore rentalsDB;
    private ArrayList<Rental> rentalArrayList;
    private ArrayList<String> rentalIdList;
    private ResultAdapter resultAdapter;
    private int type;
    String area = "";
    final char KEY  = 'M';
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_list);
        back = findViewById(R.id.back);
        rentalArrayList = new ArrayList<>();
        rentalIdList = new ArrayList<>();
        area = getIntent().getStringExtra("area");
        resultLists = findViewById(R.id.resultlist_recycleview);
        resultLists.setLayoutManager(new LinearLayoutManager(this));
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        resultLists.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(resultLists);
        rentalsDB = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        //-Select nearest university
        //-Select nearest metro-do not allow this option to be selected
        type = getIntent().getIntExtra("themePos",0);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ApartmentList.this, MainActivity.class);
                startActivity(intent);
            }
        });
        uploadResults();
    }

    private boolean verify(Rental rental){

        if(area!=null&&area!=""){
            if(area.equals("kyiv")){
                return true;
            }else
                if(area.equals("sofia"))
                {
                return false;
            }
                else{
                    return false;
                }
        }
        else {
            if (type == 0) {
                if (rental.getTerm().equals("Short-term contract")) {
                    return true;
                } else {
                    return false;
                }
            } else if (type == 1) {

                if (rental.getType().equals("Long-term contract")) {
                    return true;
                } else {
                    return false;
                }
            } else if (type == 2) {
                if (rental.getType().equals("Apartment/House")) {
                    return true;
                } else {
                    return false;
                }
            } else if (type == 3) {

                if (rental.getType().equals("Room in Apartment")) {
                    return true;
                } else {
                    return false;
                }
            } else if (type ==4 ) {

                if (rental.getType().equals("Hostel")) {
                    return true;
                } else {
                    return false;
                }
            } else if (type == -1) {
                return true;
            } else {
                return false;
            }
        }
    }

    private void uploadResults(){
        final  LoadingScreen loadingScreen = new LoadingScreen(this);
        loadingScreen.startLoadingDialog();
        rentalsDB.collection("ListRental").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d:list){
                        String id = d.getId();
                        Rental rental = d.toObject(Rental.class);
                        if(verify(rental)){
                            rentalIdList.add(id);
                             rentalArrayList.add(rental);
                        }
                    }
                    loadingScreen.dismiss();
                    if(rentalArrayList.size()!=0) {
                        resultAdapter = new ResultAdapter(getApplicationContext(), rentalArrayList, rentalIdList, type, KEY);
                        resultLists.setAdapter(resultAdapter);
                    }else{
                        Intent intent = new Intent(ApartmentList.this, EmptyResultList.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == ItemTouchHelper.LEFT) {

                SavedAd savedAd = new SavedAd(userId, rentalIdList.get(viewHolder.getAdapterPosition()));
                FirebaseFirestore savedAdsDB = FirebaseFirestore.getInstance();
                savedAdsDB.collection("SavedRentals").document().set(savedAd).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ApartmentList.this, "Ad has been saved successfully.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ApartmentList.this.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(ApartmentList.this, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(ApartmentList.this, R.color.salmon))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_favorite_24)
                    .setActionIconTint(ContextCompat.getColor(recyclerView.getContext(), android.R.color.white))
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    };
    // Swipe Gesture integration


    public class VerticalSpacingItemDecorator extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpacingItemDecorator(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {

            outRect.bottom = verticalSpaceHeight;
        }


    }
}