package com.example.wimm.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wimm.Helper.DataAccess;
import com.example.wimm.R;

import java.util.Map;

public class SettingsFragement extends Fragment implements View.OnClickListener {

    TextView userEmailTextView;
    EditText inputUpdateSalary;
    Button saveChangesBtn;
    Button changePasswordBtn;
    String salaryInDB;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings,container,false);
        userEmailTextView =  view.findViewById(R.id.userEmailTextView);
        inputUpdateSalary = view.findViewById(R.id.inputUpdateSalary);
        saveChangesBtn = view.findViewById(R.id.saveChangesBtn);
        saveChangesBtn.setOnClickListener(this);
        changePasswordBtn = view.findViewById(R.id.changePasswordBtn);
        changePasswordBtn.setOnClickListener(this);



        //Setup fields
        Map<String, Object> fields = DataAccess.GetUserFields();

        if (fields != null)
        {
            for (Map.Entry<String,Object> field : fields.entrySet())
            {
                if (field.getKey().equals("email"))
                    userEmailTextView.setText(field.getValue() + "");
                else if (field.getKey().equals("salary")) {
                    inputUpdateSalary.setText(field.getValue() + "");
                    salaryInDB = field.getValue() + "";
                }

            }
        }

        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.saveChangesBtn:

                if (String.valueOf(inputUpdateSalary.getText()).equals(salaryInDB))
                    return;

                DataAccess.UpdateSalary(Integer.parseInt(String.valueOf(inputUpdateSalary.getText())));
                break;

            case R.id.changePasswordBtn:
                //Do this later


                break;
        }



    }
}
