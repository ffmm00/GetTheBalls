package com.fm_example.upupup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends Activity {
    public static SharedPreferences preftest2;
    private TextView mRuleText;
    private ImageView mExample;
    private Bitmap mBitmapExample;
    public static  Display disp;
    private WindowManager wm;
    private Button mBtnNext;
    private Button mEndButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Resources rsc = getResources();


        wm = getWindowManager();
        disp = wm.getDefaultDisplay();

        mExample = (ImageView) findViewById(R.id.example);
        mBitmapExample = BitmapFactory.decodeResource(rsc, R.drawable.explanation);
        mBitmapExample = Bitmap.createScaledBitmap(mBitmapExample, disp.getWidth() / 3 * 2, disp.getHeight() / 4, false);
        mExample.setImageBitmap(mBitmapExample);

        preftest2 = getSharedPreferences("DataSave", Context.MODE_PRIVATE);


        mBtnNext = (Button) this.findViewById(R.id.button);
        mBtnNext.setTextSize(TextAdjust(24));
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CharaMove.class);
                startActivity(intent);
            }
        });

        mEndButton=(Button)this.findViewById(R.id.endButton);
        mEndButton.setTextSize(TextAdjust(24));
        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        int getScore = preftest2.getInt("save", 0);

        mRuleText = (TextView) findViewById(R.id.ruleView);
        mRuleText.setTextSize(TextAdjust(36));

        if (getScore != 700) {
            mRuleText.setText("　　箱を指で操作し\r\n上から落ちてくる玉を\r\n箱の中に入れれば得点獲得\r\n\r\n　　最高得点　" + getScore + "");
        } else {
            mRuleText.setText("　　箱を指で操作し\r\n上から落ちてくる玉を\r\n箱の中に入れれば得点獲得\r\n\r\n　　最高得点　" + getScore + "" + "☆");
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int TextAdjust(int base){
        return base*(int)Adjust.getWidthAdjust(disp.getWidth());
    }
}
