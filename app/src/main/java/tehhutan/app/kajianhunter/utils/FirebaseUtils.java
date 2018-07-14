package tehhutan.app.kajianhunter.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static tehhutan.app.kajianhunter.utils.Constants.MY_PREFS_NAME;

/**
 * Created by brad on 2017/02/05.
 */

public class FirebaseUtils {
    //I'm creating this class for similar reasons as the Constants class, and to make my code a bit
    //cleaner and more well managed.

    public static DatabaseReference getUserRef(String wa){
        return FirebaseDatabase.getInstance()
                .getReference(Constants.USERS_KEY).child("Regular")
                .child(wa);
    }

    public static DatabaseReference getPostRef(){
        return FirebaseDatabase.getInstance()
                .getReference(Constants.POST_KEY);
    }

    public static DatabaseReference getPostLikedRef(){
        return FirebaseDatabase.getInstance()
                .getReference(Constants.POST_LIKED_KEY);
    }

    public static DatabaseReference getPostLikedRef(String postId, Context c){
        return getPostLikedRef().child(getUserID(c)
        .replace(".",","))
                .child(postId);
    }
    public static String getUserID(Context c){
        SharedPreferences prefs = c.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String id = prefs.getString("user", null);
        Toast.makeText(c, id, Toast.LENGTH_SHORT).show();
        return  id;
    }

    public static FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static String getUid(){
        String path = FirebaseDatabase.getInstance().getReference().push().toString();
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public static StorageReference getImageSRef(){
        return FirebaseStorage.getInstance().getReference(Constants.POST_IMAGES);
    }

    public static DatabaseReference getMyPostRef(Context c){
        return FirebaseDatabase.getInstance().getReference(Constants.MY_POSTS)
                .child(getUserID(c).replace(".",","));
    }

    public static DatabaseReference getCommentRef(String postId){
        return FirebaseDatabase.getInstance().getReference(Constants.COMMENTS_KEY)
                .child(postId);
    }

    public static DatabaseReference getMyRecordRef(Context c){
        return FirebaseDatabase.getInstance().getReference(Constants.USER_RECORD)
                .child(getUserID(c).replace(".",","));
    }

    public static void addToMyRecord(String node, final String id, Context c){
        getMyRecordRef(c).child(node).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                ArrayList<String> myRecordCollection;
                if(mutableData.getValue() == null){
                    myRecordCollection = new ArrayList<String>(1);
                    myRecordCollection.add(id);
                }else{
                    myRecordCollection = (ArrayList<String>) mutableData.getValue();
                    myRecordCollection.add(id);
                }

                mutableData.setValue(myRecordCollection);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

}
