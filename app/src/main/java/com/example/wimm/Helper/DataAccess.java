package com.example.wimm.Helper;

import androidx.annotation.NonNull;

import com.example.wimm.Modules.Category;
import com.example.wimm.Modules.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class DataAccess {

    static FirebaseFirestore database = FirebaseFirestore.getInstance();
    static CollectionReference usersCollection = database.collection("user");

    static DocumentReference userDBReference;
    static DocumentSnapshot userInstance;

    public static List<Category> categories = new ArrayList<>();
    public static List<Item> itemsInCurrentCategory = new ArrayList<>();



    //User DataAccess
    public static void SetUser(String email)
    {
        try
        {
            userDBReference = usersCollection.document(email);
            userDBReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful())
                            userInstance = task.getResult();
                    }
                });


        }catch (Exception e)
        {
            SetUser(email);
        }
    }


    public static Map<String, Object> GetUserFields()
    {
        if (!(userDBReference != null &&  userInstance != null && userInstance.exists()))
            return null;

        try {
            return userInstance.getData();
        }
        catch (Exception e)
        {
            return null;
        }
    }


    public static boolean UpdateSalary(int newSalary)
    {
        if (!(userDBReference != null &&  userInstance != null && userInstance.exists()))
            return false;

        try {
            userDBReference.update("salary",newSalary);
            return true;
        }
        catch (Exception e)
        {
            return false;

        }

    }



    //Category DataAccess
    public static boolean AddCategory(String categoryName)
    {

        if (!(userDBReference != null && userInstance != null && userInstance.exists()))
            return false;


        Map<String,Object> fields = new HashMap<>();
        fields.put("name",categoryName);

        try
        {
            userDBReference.collection("category").document(categoryName).set(fields).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    UpdateCategoriesList();
                }
            });

            return true;
        }
        catch (Exception e) {
            return false;
        }

    }

    //Category DataAccess
    public static boolean DeleteCategory(String categoryName)
    {
        if (!(userDBReference != null && userInstance != null && userInstance.exists()))
            return false;


       try {
           userDBReference.collection("category").document(categoryName).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   UpdateCategoriesList();
               }
           });
           return true;
       }
       catch (Exception e)
       {
           return false;
       }
    }


    public static boolean UpdateCategoriesList() {
        if (!(userDBReference != null && userInstance != null && userInstance.exists()))
            return false;

        try {
            userDBReference.collection("category")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Category> tempCategories = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    tempCategories.add(new Category(document.getString("name")));
                                    categories = tempCategories;

                                }
                            }
                        }
                    });

        } catch (Exception e) {
            return false;
        }finally {
            return true;
        }

    }


    //Items DataAccess
    public static boolean AddItem(String itemName, int itemPrice, String selectedCategoryName)
    {
        if (!(userDBReference != null && userInstance != null && userInstance.exists()))
            return false;

        try {

            Map<String,Object> fields = new HashMap<>();
            fields.put("name",itemName);
            fields.put("price",itemPrice);
            userDBReference.collection("category").document(selectedCategoryName).collection("item").document(itemName).set(fields);
            return true;

        }
        catch (Exception e)
        {
            return false;
        }

    }

    public static boolean DeleteItem(String itemName,String selectedCategoryName)
    {
        if (!(userDBReference != null && userInstance != null && userInstance.exists()))
            return false;


        try {
            userDBReference.collection("category").document(selectedCategoryName).collection("item").document(itemName).delete();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public static boolean UpdateItemPrice(String itemName,int newItemPrice,String selectedCategoryName)
    {
        if (!(userDBReference != null && userInstance != null && userInstance.exists()))
            return false;

        try {
            userDBReference.collection("category").document(selectedCategoryName).collection("item").document(itemName).update("price",newItemPrice);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

    }



    public static boolean UpdateItemsList(String selectedCategoryName)
    {
        if (!(userDBReference != null && userInstance != null && userInstance.exists()))
            return false;

        try {
            userDBReference.collection("category").document(selectedCategoryName).collection("item").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Item> tempItems = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    tempItems.add(new Item(document.getString("name"),document.getString("price")));
                                    itemsInCurrentCategory = tempItems;
                                }
                            }
                        }
                    });

        }
        catch (Exception e)
        {
            return false;
        }
        finally {
            return true;
        }

    }












}
