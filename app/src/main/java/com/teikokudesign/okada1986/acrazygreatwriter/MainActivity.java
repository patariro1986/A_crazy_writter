package com.teikokudesign.okada1986.acrazygreatwriter;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int START_LEVEL = 1;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Read xml file
        setScreenMain();

        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();




    }
    private void setScreenSub(){
        //read layout
        setContentView(R.layout.activity_sub);

        final TextView textview1 = (TextView) findViewById(R.id.textview1);
        final TextView textview2 = (TextView) findViewById(R.id.textview2);
        textview1.setText("A Crazy writer");

        try
        {

            String finalText="";
            for(int i=0 ; i<50 ; i++){
                //Read CSV data.
                AssetManager assetManager = getResources().getAssets();
                InputStream inputWordsStream = assetManager.open("data.csv");
                InputStreamReader inputWordsStreamReader = new InputStreamReader(inputWordsStream);
                BufferedReader wordsBufferReader = new BufferedReader(inputWordsStreamReader);

                String line;
                String messageText="";
                while ((line = wordsBufferReader.readLine()) != null) {
                    //カンマ区切りで１つづつ配列に入れる
                    String[] RowData = line.split(",");
                    messageText=messageText+getRandomWords(RowData)+" ";
                }
                finalText=finalText+messageText;

            }
            textview2.setText(finalText);


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Button returnButton = findViewById(R.id.makeSentence_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setScreenMain();
            }
        });
    }

    private void setScreenMain(){

        setContentView(R.layout.activity_main);

        // Create the next level button, which tries to show an interstitial when clicked.
        final ImageButton imageButton = (ImageButton) findViewById(R.id.imageGoToMakeSentenceScene_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            /** ボタンをクリックした時に呼ばれる */
            @Override
            public void onClick(View v) {
                // ここに処理を記述する
                setScreenSub();
            }

        });

        final TextView textview1 = (TextView) findViewById(R.id.textview1);
        final TextView textview2 = (TextView) findViewById(R.id.textview2);
        textview1.setText("A Crazy writer");
        textview2.setText("Please push ”make a scentence” of the bottom then you will get norbel prise!!");

        //ImageView imageView2 = (ImageView)findViewById(R.id.image_view_2);
        ImageView imageView2 = findViewById(R.id.imageview1);
        imageView2.setImageResource(R.drawable.main1);

    }

    public String getRandomWords(String args[]){
        Random rnd = new Random();
        int ran = rnd.nextInt(args.length);

        return args[ran];

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
                goToNextLevel();
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            goToNextLevel();
        }
    }

    private void loadInterstitial() {
        // Disable the next level button and load the ad.

        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void goToNextLevel() {
        // Show the next level and reload the ad to prepare for the level after.
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }
}
