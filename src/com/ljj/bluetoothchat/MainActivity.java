package com.ljj.bluetoothchat;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button startServerBtn;
	private Button startClientBtn;
	private ButtonClickListener btnClickListener = new ButtonClickListener();

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionBar = this.getActionBar();
		actionBar.setTitle("主界面");
		actionBar.setIcon(getResources().getDrawable(R.drawable.ic_launcher));		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		startServerBtn = (Button) findViewById(R.id.startServerBtn);
		startClientBtn = (Button) findViewById(R.id.startClientBtn);

		startServerBtn.setOnClickListener(btnClickListener);
		startClientBtn.setOnClickListener(btnClickListener);
	}

	class ButtonClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.startServerBtn:
				// 打开服务器
				Intent serverIntent = new Intent(MainActivity.this,
						ServerActivity.class);
				serverIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(serverIntent);
				break;

			case R.id.startClientBtn:
				// 打开客户端
				Intent clientIntent = new Intent(MainActivity.this,
						ClientActivity.class);
				clientIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(clientIntent);
				break;
			}
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case android.R.id.home:
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
