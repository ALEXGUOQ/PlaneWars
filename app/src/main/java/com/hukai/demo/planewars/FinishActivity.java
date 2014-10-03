package com.hukai.demo.planewars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class FinishActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        Button restartBtn = (Button) findViewById(R.id.btn_restart);
        restartBtn.setOnClickListener(this);

        Button quitBtn = (Button) findViewById(R.id.btn_quit);
        quitBtn.setOnClickListener(this);

        TextView scoreText = (TextView) findViewById(R.id.finish_text);
        scoreText.setText("Score:" + getIntent().getIntExtra("Score", 0));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_restart:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.btn_quit:
                finish();
                break;
            default:
                break;
        }
    }

}
