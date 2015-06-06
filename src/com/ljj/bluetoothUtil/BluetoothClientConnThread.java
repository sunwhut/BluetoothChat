package com.ljj.bluetoothUtil;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

/**
 * �����ͻ��������߳�
 */
public class BluetoothClientConnThread  extends Thread {
	private Handler serviceHandler; // ������ͻ���Service�ش���Ϣ��handler
	private BluetoothDevice serverDevice; // �������豸
	private BluetoothSocket socket; // ͨ��Socket

	/**
	 * ���캯��
	 */
	public BluetoothClientConnThread(Handler handler,
			BluetoothDevice serverDevice) {
		this.serviceHandler = handler;
		this.serverDevice = serverDevice;
	}

	@Override
	public void run() {
		BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
		try {
			//��������
			socket = serverDevice
					.createRfcommSocketToServiceRecord(BluetoothTools.PRIVATE_UUID);
			BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
			socket.connect();
		} catch (Exception ex) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("BluetoothClientConnThread��e=" + ex.getMessage());
			// ��������ʧ����Ϣ
			serviceHandler.obtainMessage(BluetoothTools.MESSAGE_CONNECT_ERROR)
					.sendToTarget();
			return;
		}

		// �������ӳɹ���Ϣ����Ϣ��obj����Ϊ���ӵ�socket
		Message msg = serviceHandler.obtainMessage();
		msg.what = BluetoothTools.MESSAGE_CONNECT_SUCCESS;
		msg.obj = socket;
		msg.sendToTarget();
	}

}
