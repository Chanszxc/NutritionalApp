package com.example.nutritional;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Recipe extends AppCompatActivity {
    ImageView imgSpinach;
    ImageView imgCelery;
    ImageView imgOatmeal;
    ImageView imgRasp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        imgSpinach = (findViewById(R.id.imgSpinach));
        imgCelery = (findViewById(R.id.imgCelery));
        imgOatmeal = (findViewById(R.id.imgOatmeal));
        imgRasp = (findViewById(R.id.imgRasp));


        imgSpinach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.simplyrecipes.com/recipes/spinach/"));
                startActivity(intent);
            }
        });

        imgCelery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.simplyrecipes.com/recipes/celery_stir_fry/"));
                startActivity(intent);
            }
        });

        imgOatmeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.simplyrecipes.com/baked-oatmeal-with-mixed-berries-recipe-5185886"));
                startActivity(intent);
            }
        });

        imgRasp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.simplyrecipes.com/raspberry-lime-rickey-recipe-5203810"));
                startActivity(intent);
            }
        });
    }
}