package tehhutan.app.kajianhunter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import tehhutan.app.kajianhunter.model.Comment;
import tehhutan.app.kajianhunter.model.Post;
import tehhutan.app.kajianhunter.model.User;
import tehhutan.app.kajianhunter.utils.Constants;
import tehhutan.app.kajianhunter.utils.FirebaseUtils;

public class PostDanKomentar extends AppCompatActivity {
    private static final String BUNDLE_COMMENT = "comment";
    private Post mPost;
    private EditText komentar;
    private Comment mComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_dan_komentar);
        if (savedInstanceState != null) {
            mComment = (Comment) savedInstanceState.getSerializable(BUNDLE_COMMENT);
        }

        Intent intent = getIntent();
        mPost = (Post) intent.getSerializableExtra(Constants.EXTRA_POST);

        init();
        initPost();
        initCommentSection();
    }
    private void initPost() {
        ImageView photoProfilAdminPost = (ImageView)findViewById(R.id.gambar_admin_timeline);
        TextView namaProfilAdmin = (TextView)findViewById(R.id.nama_admin_timeline);
        LinearLayout postLikeLayout = (LinearLayout)findViewById(R.id.like_layout);
        LinearLayout postCommentLayout = (LinearLayout)findViewById(R.id.comment_layout);
        TextView jumlahLikes = (TextView)findViewById(R.id.tv_likes);
        TextView jumlahKomentar = (TextView)findViewById(R.id.tv_comments);
        TextView isiPost = (TextView)findViewById(R.id.deskripsi_pengumuman_timeline);

        namaProfilAdmin.setText(mPost.getUser().getNama());
        isiPost.setText(mPost.getPostText());
        jumlahLikes.setText(String.valueOf(mPost.getNumLikes()));
        jumlahKomentar.setText(String.valueOf(mPost.getNumComments()));

        if(mPost.getUser().getPhoto()!=null){
            Glide.with(PostDanKomentar.this)
                    .load(mPost.getUser().getPhoto())
                    .into(photoProfilAdminPost);
        }

        /*if (mPost.getPostImageUrl() != null) {
            postDisplayImageView.setVisibility(View.VISIBLE);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(mPost.getPostImageUrl());

            Glide.with(PostActivity.this)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .into(postDisplayImageView);
        } else {
            postDisplayImageView.setImageBitmap(null);
            postDisplayImageView.setVisibility(View.GONE);
        }*/
    }

    private void initCommentSection() {
        RecyclerView commentRecyclerView = (RecyclerView) findViewById(R.id.kolom_komentar);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(PostDanKomentar.this));

        FirebaseRecyclerAdapter<Comment, CommentHolder> commentAdapter = new FirebaseRecyclerAdapter<Comment, CommentHolder>(
                Comment.class,
                R.layout.segmen_komentar,
                CommentHolder.class,
                FirebaseUtils.getCommentRef(mPost.getPostId())
        ) {
            @Override
            protected void populateViewHolder(CommentHolder viewHolder, Comment model, int position) {
                viewHolder.setUsername(model.getUser().getNama());
                viewHolder.setComment(model.getComment());
                if(model.getUser().getPhoto()!=null){
                    Glide.with(PostDanKomentar.this)
                            .load(model.getUser().getPhoto())
                            .into(viewHolder.photoProfileKomentator);
                }
            }
        };

        commentRecyclerView.setAdapter(commentAdapter);
    }

    private void init() {
        komentar= (EditText)findViewById(R.id.edittext_komentar_timeline);
        findViewById(R.id.kirim_tombol).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
    }

    private void sendComment() {
        final ProgressDialog progressDialog = new ProgressDialog(PostDanKomentar.this);
        progressDialog.setMessage("Sending comment..");
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        mComment = new Comment();
        final String uid = FirebaseUtils.getUid();
        String strComment = komentar.getText().toString();

        mComment.setCommentId(uid);
        mComment.setComment(strComment);
        FirebaseUtils.getUserRef(FirebaseUtils.getUserID(getApplication()).replace(".", ","))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        mComment.setUser(user);
                        FirebaseUtils.getCommentRef(mPost.getPostId())
                                .child(uid)
                                .setValue(mComment);
                        FirebaseUtils.getPostRef().child(mPost.getPostId())
                                .child(Constants.NUM_COMMENTS_KEY)
                                .runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                        long num = (long) mutableData.getValue();
                                        mutableData.setValue(num + 1);
                                        return Transaction.success(mutableData);
                                    }

                                    @Override
                                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                        progressDialog.dismiss();
                                        FirebaseUtils.addToMyRecord(Constants.COMMENTS_KEY, uid, getApplication());
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressDialog.dismiss();
                    }
                });
    }

    public static class CommentHolder extends RecyclerView.ViewHolder {
        ImageView photoProfileKomentator;
        TextView namaKomentator;
        TextView isiKomentar;

        public CommentHolder(View itemView) {
            super(itemView);
            photoProfileKomentator = (ImageView) itemView.findViewById(R.id.gambar_komentator_timeline);
            namaKomentator = (TextView) itemView.findViewById(R.id.nama_komentator_timeline);
            isiKomentar = (TextView) itemView.findViewById(R.id.isi_komentator);
        }

        public void setUsername(String username) {
            namaKomentator.setText(username);
        }

        public void setComment(String comment) {
            isiKomentar.setText(comment);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(BUNDLE_COMMENT, mComment);
        super.onSaveInstanceState(outState);
    }
}