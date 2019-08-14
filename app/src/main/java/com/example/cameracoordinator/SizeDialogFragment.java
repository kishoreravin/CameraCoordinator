package com.example.cameracoordinator;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SizeDialogFragment extends DialogFragment {

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        assert d != null;
        d.setCanceledOnTouchOutside(true);
    }

    private EditText ImageHeight, ImageWidth;
    private Button EnterButton;
    int height, width = 250;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.size_dialog_fragment,container,false);

        ImageHeight = view.findViewById(R.id.image_height);
        ImageWidth = view.findViewById(R.id.image_width);
        EnterButton = view.findViewById(R.id.enter_button);

        EnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ImageHeight.getText().toString() != null && ImageWidth.getText().toString() != null){
                    height = Integer.parseInt(ImageHeight.getText().toString().trim());
                    width = Integer.parseInt(ImageWidth.getText().toString().trim());
                    CameraActivity cameraActivity =  new CameraActivity();
                    cameraActivity.setSize(height,width);
                    SizeDialogFragment.this.dismiss();
                }
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
