package com.vkochenkov.weatherfromopenapis;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

public class AddCityDialog extends Dialog {

    private Context context;

    public AddCityDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_city);

        findViewById(R.id.apply_add_city_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo логика добавления в базу
                onBackPressed();
            }
        });

        findViewById(R.id.cancel_add_city_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
