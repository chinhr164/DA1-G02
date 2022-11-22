package com.zrapp.warehouse.Fragment.FragStaff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.zrapp.warehouse.DAO.StaffDAO;
import com.zrapp.warehouse.Model.Staff;
import com.zrapp.warehouse.R;
import com.zrapp.warehouse.databinding.FragStaffBinding;

import java.util.ArrayList;

public class FragStaff extends Fragment {

    FragStaffBinding binding;
    ArrayList<Staff> list;
    StaffDAO dao;

    public FragStaff() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragStaffBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = new ArrayList<>();
        dao = new StaffDAO();
        list = dao.getAll();

        if (list.isEmpty()){
            loadFrag(new FragStaff());
        }else{
            loadFrag(new FragStaffList());
        }

        binding.btnAddnv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFrag(new FragStaffAdd());
            }
        });
    }

    public void loadFrag(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}