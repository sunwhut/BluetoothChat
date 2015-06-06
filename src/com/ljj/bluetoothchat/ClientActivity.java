package com.ljj.bluetoothchat;

import java.util.Date;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ljj.bluetoothUtil.BluetoothClientService;
import com.ljj.bluetoothUtil.BluetoothTools;
import com.ljj.bluetoothUtil.TransmitBean;

public class ClientActivity extends Activity {
	private TextView serversText;
	private EditText chatEditText;
	private EditText sendEditText;
	private Button sendBtn;
	private Button weight;

	//private List<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();
	int count = 1;

	// �㲥������
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (BluetoothTools.ACTION_NOT_FOUND_SERVER.equals(action)) {
				// δ�����豸
				serversText.append("δ�����豸" + count + "��\r\n");
				count++;
			} else if (BluetoothTools.ACTION_FOUND_DEVICE.equals(action)) {
				// ��ȡ���豸����
				BluetoothDevice device = (BluetoothDevice) intent.getExtras()
						.get(BluetoothTools.DEVICE);

				String address = device.getAddress();
				String name = device.getName();
				Log.e("add+name2", "address="+address+",name="+name);
				
//				if (address.equals(BluetoothTools.MEIZU_M9)
//						|| address.equals(BluetoothTools.HTC_Wildfire)
//						|| address.equals(BluetoothTools.Nexus_S)) {
//					deviceList.add(device);
//					serversText.append(device.getName() + "\r\n");
//				}
			} else if (BluetoothTools.ACTION_CONNECT_SUCCESS.equals(action)) {
				// ���ӳɹ�
				serversText.append("���ӳɹ�");
//				sendBtn.setEnabled(true);

			} else if (BluetoothTools.ACTION_DATA_TO_GAME.equals(action)) {
				// ��������
				TransmitBean data = (TransmitBean) intent.getExtras()
						.getSerializable(BluetoothTools.DATA);
				String msg = "����ˣ�" +"  "+ new Date().toLocaleString()
						+ " :\r\n" + data.getMsg() + "\r\n";
				chatEditText.append(msg);

			}else if(BluetoothTools.ACTION_CONNECT_ERROR.equals(action)){
				chatEditText.append("����ʧ��");
			}
		}
	};

	@Override
	protected void onStart() {
		// ����豸�б�
		//deviceList.clear();

		// ������̨service
		Intent startService = new Intent(ClientActivity.this,
				BluetoothClientService.class);
		startService(startService);

		// ע��BoradcasrReceiver
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothTools.ACTION_NOT_FOUND_SERVER);
		intentFilter.addAction(BluetoothTools.ACTION_FOUND_DEVICE);
		intentFilter.addAction(BluetoothTools.ACTION_DATA_TO_GAME);
		intentFilter.addAction(BluetoothTools.ACTION_CONNECT_SUCCESS);
		intentFilter.addAction(BluetoothTools.ACTION_CONNECT_ERROR);
		
		registerReceiver(broadcastReceiver, intentFilter);
		super.onStart();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client);
		ActionBar actionBar = this.getActionBar();
		actionBar.setTitle("�ͻ���������");
		actionBar.setIcon(getResources().getDrawable(R.drawable.launcher));		
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		serversText = (TextView) findViewById(R.id.clientServersText);
		chatEditText = (EditText) findViewById(R.id.clientChatEditText);
		weight = (Button)findViewById(R.id.weight);
		weight.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMess("����666666kg");
			}
			
		});
/*		sendEditText = (EditText) findViewById(R.id.clientSendEditText);
		sendBtn = (Button) findViewById(R.id.clientSendMsgBtn);

		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ������Ϣ
				if ("".equals(sendEditText.getText().toString().trim())) {
					Toast.makeText(ClientActivity.this, "���벻��Ϊ��",
							Toast.LENGTH_SHORT).show();
				} else {
					// ������Ϣ
					TransmitBean data = new TransmitBean();
					String msg = "�ͻ��ˣ�" + "  " +new Date().toLocaleString()
							+ " :\r\n" + sendEditText.getText().toString() + "\r\n";
					chatEditText.append(msg);
					data.setMsg(sendEditText.getText().toString());
					Intent sendDataIntent = new Intent(
							BluetoothTools.ACTION_DATA_TO_SERVICE);
					sendDataIntent.putExtra(BluetoothTools.DATA, data);
					sendBroadcast(sendDataIntent);
				}
			}
		});*/
	}

	@Override
	protected void onStop() {
		// �رպ�̨Service
		Intent startService = new Intent(BluetoothTools.ACTION_STOP_SERVICE);
		sendBroadcast(startService);

		unregisterReceiver(broadcastReceiver);
		super.onStop();
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
	
	//������Ϣ
	public void sendMess(String str){
		TransmitBean data = new TransmitBean();
		data.setMsg(str);
		Intent sendDataIntent = new Intent(
				BluetoothTools.ACTION_DATA_TO_SERVICE);
		sendDataIntent.putExtra(BluetoothTools.DATA, data);
		sendBroadcast(sendDataIntent);
	}
}
