package tk.mohithaiyappa.wallela;

import android.database.Cursor;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<UrlDataStorage> arrayList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private String TAG = "homeActivity";
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private boolean inFavorites;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Flat-Art/");
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        inFavorites = false;

        adapter = new RecyclerAdapter(arrayList, HomeActivity.this);
        recyclerView.setAdapter(adapter);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                recyclerView.scrollTo(0, 0);
                switch (menuItem.getItemId()) {
                    case R.id.flat_art: {
                        databaseReference = firebaseDatabase.getReference("Flat-Art/");
                        loadDataSet();
                        toolbar.setTitle("Flat-Art");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        inFavorites = false;
                        recyclerView.smoothScrollToPosition(0);
                        break;
                    }
                    case R.id.superhero: {
                        databaseReference = firebaseDatabase.getReference("SuperHero/");
                        loadDataSet();
                        toolbar.setTitle("SuperHero");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        inFavorites = false;
                        recyclerView.smoothScrollToPosition(0);
                        break;
                    }
                    case R.id.photography: {
                        databaseReference = firebaseDatabase.getReference("Photography/");
                        loadDataSet();
                        toolbar.setTitle("Photography");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        inFavorites = false;
                        recyclerView.smoothScrollToPosition(0);
                        break;
                    }
                    case R.id.nature: {
                        databaseReference = firebaseDatabase.getReference("nature/");
                        loadDataSet();
                        toolbar.setTitle("Nature");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        inFavorites = false;
                        recyclerView.smoothScrollToPosition(0);
                        break;
                    }
                    case R.id.favorites: {
                        recyclerView.scrollToPosition(0);
                        loadFavorites();
                        toolbar.setTitle("Favorites");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        inFavorites = true;
                        break;
                    }
                }
                freeMemory();
                return true;
            }
        });
        loadDataSet();
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void loadDataSet() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    UrlDataStorage urlDataStorage = new UrlDataStorage(data.child("midResUrl").getValue(String.class),
                            data.child("Url").getValue(String.class),
                            data.child("lowResUrl").getValue(String.class));
                    arrayList.add(urlDataStorage);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }


    public void loadFavorites() {
        new loadAsycFavorites().execute();
    }


    public class loadAsycFavorites extends AsyncTask {

        private boolean aBoolean;

        @Override
        protected Object doInBackground(Object[] objects) {
            DataBaseHelper db = new DataBaseHelper(HomeActivity.this);
            aBoolean = false;
            Cursor cursor = db.getData();
            arrayList.clear();
            if (cursor == null) {
                aBoolean = true;
            } else {
                while (cursor.moveToNext()) {
                    UrlDataStorage urlDataStorage = new UrlDataStorage(cursor.getString(2),
                            cursor.getString(3), cursor.getString(1));
                    arrayList.add(urlDataStorage);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            adapter.notifyDataSetChanged();
            if (aBoolean) {
                Toast.makeText(HomeActivity.this, "Add something to Favorites", Toast.LENGTH_SHORT).show();
            }

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (inFavorites) {
            new loadAsycFavorites().execute();
        }
    }


}
