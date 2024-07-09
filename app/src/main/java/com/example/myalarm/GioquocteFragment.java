package com.example.myalarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myalarm.Model.CityZone_Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GioquocteFragment extends Fragment {

    private TextView tvAddTime, tvSua, tvBack;
    private List<CityZone_Model> cityZoneList = new ArrayList<>();
    private RecyclerView recyclerView;
    private boolean isEditMode = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gioquocte, container , false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvAddTime = view.findViewById(R.id.tv_AddTime);
        tvSua = view.findViewById(R.id.tv_SuaBaoThuc);
        tvBack = view.findViewById(R.id.tv_back);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        setAdapter();

        tvAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), Selected_ItemClock_TimeActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        tvSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditMode = !isEditMode;
                setAdapter();
                tvBack.setVisibility(View.VISIBLE);
                tvSua.setVisibility(View.GONE);
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditMode = !isEditMode;
                setAdapter();
                tvBack.setVisibility(View.GONE);
                tvSua.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setAdapter() {
        RecyclerView.Adapter adapter;
        if (isEditMode) {
            adapter = new InternationalTimeCancelAdapter(requireContext(), cityZoneList);
        } else {
            adapter = new InternationalTimeAdapter(requireContext(), cityZoneList);
        }
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null) {
            String currentTime = data.getStringExtra("currentTime");
            if (currentTime != null) {
                try {
                    JSONObject jsonObject = new JSONObject(currentTime);
                    String time = jsonObject.getString("time");
                    String timeZone = jsonObject.getString("timeZone");

                    CityZone_Model cityZone = new CityZone_Model();
                    cityZone.setZone(timeZone);
                    cityZone.setTime(time);

                    cityZoneList.add(cityZone);
                    // Cập nhật lại adapter sau khi thêm thành phố mới
                    setAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Error parsing JSON", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
