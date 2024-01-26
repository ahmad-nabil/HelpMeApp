package com.ahmad.helpmeapp.UserSection;

import static android.graphics.Color.TRANSPARENT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ahmad.helpmeapp.R;
import com.ahmad.helpmeapp.customData.CustomSpinnerList;
import com.ahmad.helpmeapp.databinding.CustomSpinnerItemBinding;

import java.util.ArrayList;

public class CustomSpinnerAdapterLocation extends ArrayAdapter<CustomSpinnerList> {
    public CustomSpinnerAdapterLocation(@NonNull Context context, ArrayList<CustomSpinnerList> spinnerLists) {
        super(context, 0, spinnerLists);
    }
//creating the view that is displayed when the spinner is closed.
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }
//displayed when the spinner is open and hide the position 0
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        CustomSpinnerItemBinding binding;
        if (convertView == null) {
            binding = CustomSpinnerItemBinding.inflate(LayoutInflater.from(getContext()), parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (CustomSpinnerItemBinding) convertView.getTag();
        }
        //hide the position 0
        if (position == 0) {
            convertView.setVisibility(View.GONE);
        }
        //hide the button when the position not 0
        CustomSpinnerList currentItem = getItem(position);
        if (position != 0) {
            binding.morebtn.setVisibility(View.INVISIBLE);
        }

        binding.icon.setImageResource(currentItem.getId_Img());
        binding.locationAddress.setText(currentItem.getAddress());
        return convertView;
    }
//helper method to get custom view
    private View getCustomView(int position, View convertView, ViewGroup parent) {
        CustomSpinnerItemBinding binding;

        if (convertView == null) {
            binding = CustomSpinnerItemBinding.inflate(LayoutInflater.from(getContext()), parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (CustomSpinnerItemBinding) convertView.getTag();
        }
        //hide the button when the position not 0

        if (position != 0) {
            binding.morebtn.setVisibility(View.INVISIBLE);
        }
        CustomSpinnerList currentItem = getItem(position);
        binding.locationAddress.setText(currentItem.getAddress());
        binding.locationAddress.setBackgroundColor(TRANSPARENT);
        binding.icon.setImageDrawable(getContext().getDrawable(R.drawable.baseline_location_on_24));

        return convertView;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
