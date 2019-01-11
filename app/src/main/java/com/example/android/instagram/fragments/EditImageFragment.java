package com.example.android.instagram.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.android.instagram.Interface.EditImageFragmentListener;
import com.example.android.instagram.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditImageFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private EditImageFragmentListener listener;
    SeekBar seekbar_brightness,seekbar_constraint,seekbar_saturation;

    public EditImageFragmentListener getListener() {
        return listener;
    }

    public EditImageFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =  inflater.inflate(R.layout.fragment_edit_image, container, false);

        seekbar_brightness = (SeekBar) itemView.findViewById(R.id.seekbar_brightness);
        seekbar_constraint = (SeekBar) itemView.findViewById(R.id.seekbar_constraint);
        seekbar_saturation = (SeekBar) itemView.findViewById(R.id.seekbar_saturation);

        seekbar_brightness.setMax(200);
        seekbar_brightness.setProgress(100);

        seekbar_constraint.setMax(20);
        seekbar_constraint.setProgress(0);

        seekbar_saturation.setMax(30);
        seekbar_saturation.setProgress(10);

        seekbar_saturation.setOnSeekBarChangeListener(this);
        seekbar_constraint.setOnSeekBarChangeListener(this);
        seekbar_brightness.setOnSeekBarChangeListener(this);

        return itemView;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(listener != null){
            if(seekBar.getId() == R.id.seekbar_brightness){
                listener.onBrightnessChanged(progress - 100);
            }
            else if(seekBar.getId() == R.id.seekbar_constraint){
                progress+=10;
                float value = .10f*progress;
                listener.onConstraintChanged(value);
            }
            else if(seekBar.getId() == R.id.seekbar_saturation){

                float value = .10f*progress;
                listener.onSaturationChanged(value);
            }

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if(listener != null){
            listener.onEditStarted();
        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(listener != null)
        {
            listener.onEditCompleted();
        }
    }

    public void resetControls() {
        seekbar_brightness.setProgress(100);
        seekbar_constraint.setProgress(0);
        seekbar_saturation.setProgress(10);
    }


}
