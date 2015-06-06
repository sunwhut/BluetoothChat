package com.ljj.bluetoothchat;

import java.util.Date;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ljj.bluetoothUtil.BluetoothServerService;
import com.ljj.bluetoothUtil.BluetoothTools;
import com.ljj.bluetoothUtil.TransmitBean;

public class ServerActivity extends Activity {
	private TextView serverStateTextView;
	private EditText msgEditText;
	private EditText sendMsgEditText;
	private Button sendBtn;
	private Button target;
	private Button load;
	private Button keep;
	private Button zero;
	private Button netWeight;
	private Button turn;

	@Override
	protected void onStart() {
		// ������̨service
		Intent startService = new Intent(ServerActivity.this,
				BluetoothServerService.class);
		startService(startService);

		// ע��BoradcasrReceiver
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothTools.ACTION_DATA_TO_GAME);
		intentFilter.addAction(BluetoothTools.ACTION_CONNECT_SUCCESS);
		intentFilter.addAction(BluetoothTools.ACTION_CONNECT_ERROR);
		registerReceiver(broadcastReceiver, intentFilter);
		super.onStart();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView(R.layout.server);
		ActionBar actionBar = this.getActionBar();
		actionBar.setTitle("�����������");
		actionBar.setIcon(getResources().getDrawable(R.drawable.launcher));		
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		MyListener listener = new MyListener();
		target=(Button)findViewById(R.id.target);
		target.setOnClickListener(listener);
		load=(Button)findViewById(R.id.load);
		load.setOnClickListener(listener);
		keep=(Button)findViewById(R.id.keep);
		keep.setOnClickListener(listener);
		zero=(Button)findViewById(R.id.zero);
		zero.setOnClickListener(listener);
		netWeight=(Button)findViewById(R.id.netWeight);
		netWeight.setOnClickListener(listener);
		turn=(Button)findViewById(R.id.turn);
		turn.setOnClickListener(listener);
		serverStateTextView = (TextView) findViewById(R.id.serverStateTxt);
		serverStateTextView.setText("�ȴ�����...");
	
		msgEditText = (EditText) findViewById(R.id.serverEditText);

/*		sendMsgEditText = (EditText) findViewById(R.id.serverSendEditText);

		sendBtn = (Button) findViewById(R.id.serverSendMsgBtn);
		sendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("".equals(sendMsgEditText.getText().toString().trim())) {
					Toast.makeText(ServerActivity.this, "���벻��Ϊ��",
							Toast.LENGTH_SHORT).show();
				} else {
					// ������Ϣ
					sendMess(sendMsgEditText.getText().toString());
				}
			}
		});

		sendBtn.setEnabled(false);*/
	}

	@Override
	protected void onStop() {
		// �رպ�̨Service
		Intent startService = new Intent(BluetoothTools.ACTION_STOP_SERVICE);
		sendBroadcast(startService);
		unregisterReceiver(broadcastReceiver);
		super.onStop();
	}

	// �㲥������
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();

			if (BluetoothTools.ACTION_DATA_TO_GAME.equals(action)) {
				// ��������
				TransmitBean data = (TransmitBean) intent.getExtras()
						.getSerializable(BluetoothTools.DATA);
				String msg = "�ͻ��ˣ�" +"  " + new Date().toLocaleString()
						+ " :\r\n" + data.getMsg() + "\r\n";
				msgEditText.append(msg);

			} else if (BluetoothTools.ACTION_CONNECT_SUCCESS.equals(action)) {
				// ���ӳɹ�
				serverStateTextView.setText("���ӳɹ�");
//				sendBtn.setEnabled(true);
			}else if(BluetoothTools.ACTION_CONNECT_ERROR.equals(action)){
				//����ʧ��
				serverStateTextView.setText("����ʧ��");
			}

		}
	};
	
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
	
	//������Ϣ
	public void sendMess(String str){
		TransmitBean data = new TransmitBean();
		data.setMsg(str);
		Intent sendDataIntent = new Intent(
				BluetoothTools.ACTION_DATA_TO_SERVICE);
		sendDataIntent.putExtra(BluetoothTools.DATA, data);
		sendBroadcast(sendDataIntent);
	}
	
	class MyListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.target:
				sendMess("����Ŀ��");
				msgEditText.append("������\n");
				break;
			case R.id.load:
				sendMess("װ��/ж��");
				msgEditText.append("��װ��/ж��\n");
				break;
			case R.id.keep:
				sendMess("����");
				msgEditText.append("�ѱ���\n");
				break;
			case R.id.zero:
				sendMess("����");
				msgEditText.append("�ѹ���\n");
				break;
			case R.id.netWeight:
				sendMess("����");
				msgEditText.append("����Ϊ��\n");
				break;
			case R.id.turn:
				sendMess("��/��");
				msgEditText.append("�ѿ�/��\n");
				break;
			}
		}
		
	}
}
