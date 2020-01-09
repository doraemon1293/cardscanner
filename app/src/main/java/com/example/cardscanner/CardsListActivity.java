package com.example.cardscanner;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CardsListActivity extends AppCompatActivity {
    String Tag="CardsListActivity_custom";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cards_list);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("cards")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<String> cards=new ArrayList<>();

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String token = SharedUtil.getIntance(CardsListActivity.this).readShared("token", "");
                                Log.d(Tag, token);

                                if (token.equals(document.getData().get("token"))) {
                                    String s;
                                    s = "Card NO: " + document.getData().get("cardNumber").toString() + " Exp: " + document.getData().get("expireDate").toString() + "\nadded: " + document.get("Added").toString();
//                                Log.d("query", document.getId() + " => " + document.getData());
//                                Log.d("query",document.getData().get("Added").toString());
                                    Log.d(Tag, s);
                                    cards.add(s);
                                }
                            }
                        } else {
                            Log.d(Tag, "Error getting documents.", task.getException());
                        }

                        ListView listview = (ListView) findViewById(R.id.listview);
                        ArrayAdapter adapter = new ArrayAdapter(CardsListActivity.this,android.R.layout.simple_list_item_1,cards);
                        listview.setAdapter(adapter);


                    }
                });

    }
}