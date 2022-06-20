package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class notesActivity extends AppCompatActivity {
    private RelativeLayout mbtnSignOut;
    private FloatingActionButton mcreateNotes;
    private FirebaseAuth firebaseAuth;
    RecyclerView mrecyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter<firebasemodel,NoteViewHolder> noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().setTitle("User's All Notes");
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#4EE4F0"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        mcreateNotes=findViewById(R.id.createNote);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mcreateNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(notesActivity.this,createNotes.class));
            }
        });


        Query query = firebaseFirestore.collection("Notes").document(firebaseUser.getUid()).collection("MyNotes").orderBy("title",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<firebasemodel> allUserNotes = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();
        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allUserNotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull firebasemodel model) {
                ImageView popUp =  holder.itemView.findViewById(R.id.menuPopUp);
                    holder.noteTitle.setText(model.getTitle());
                    holder.noteContent.setText(model.getContent());
                    String docId = noteAdapter.getSnapshots().getSnapshot(position).getId();
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // this will open the note
                            Intent intent = new Intent(view.getContext(),noteDetails.class);
                            intent.putExtra("title",model.getTitle());
                            intent.putExtra("content",model.getContent());
                            intent.putExtra("noteId",docId);
                            view.getContext().startActivity(intent);
                           // Toast.makeText(notesActivity.this, "This is clicked", Toast.LENGTH_SHORT).show();
                        }
                    });
                    popUp.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(View view) {
                            PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
                            popupMenu.setGravity(Gravity.END);
                            popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    Intent intent = new Intent(view.getContext(),editNotesActivity.class);
                                    intent.putExtra("title",model.getTitle());
                                    intent.putExtra("content",model.getContent());
                                    intent.putExtra("noteId",docId);
                                    view.getContext().startActivity(intent);
                                    return false;
                                }
                            });

                            popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    DocumentReference documentReference = firebaseFirestore.collection("Notes").document(firebaseUser.getUid()).collection("MyNotes").document(docId);
                                    documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(notesActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(notesActivity.this, "Note Couldn't be deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    return false;
                                }
                            });
                            popupMenu.show();
                        }
                    });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };

        mrecyclerView = findViewById(R.id.recyclearView);
        mrecyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mrecyclerView.setAdapter(noteAdapter);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView noteTitle;
        TextView noteContent;
        LinearLayout mNote;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteContent = itemView.findViewById(R.id.noteContent);
            mNote = itemView.findViewById(R.id.note);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.mbtnSignout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(notesActivity.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (noteAdapter!=null){
//            noteAdapter.stopListening();
//        }
//    }
}