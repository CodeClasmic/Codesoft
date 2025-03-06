package com.projects.wicketiq;

import android.app.Activity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthManager {
    private final FirebaseAuth firebaseAuth;

    public AuthManager() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void registerUser(String email, String password, Activity activity, AuthCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful())
                    {
                        FirebaseUser user= firebaseAuth.getCurrentUser();
                        if (user!=null)
                        {
                            user.sendEmailVerification().addOnCompleteListener(emailTask -> {
                               if(emailTask.isSuccessful())
                               {
                                   callback.onSuccess(user);
                               }
                               else
                               {
                                   callback.onFailure(emailTask.getException());
                               }
                            });
                        }

                    }
                    else
                    {
                        callback.onFailure(task.getException());
                    }
                });
    }

    public void loginUser(String email,String password,Activity activity,AuthCallback callback)
    {
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(activity,task -> {
            if(task.isSuccessful())
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null && user.isEmailVerified())
                {
                    callback.onSuccess(user);
                }
                else
                {
                    logoutUser();
                    callback.onFailure(new Exception("Email not verified"));
                }
            }
            else {
                callback.onFailure(task.getException());
            }
        });
    }

    public  void logoutUser()
    {
        firebaseAuth.signOut();
    }

    public FirebaseUser getCurrentUser(){
        return firebaseAuth.getCurrentUser();
    }

    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception e);
    }
}
