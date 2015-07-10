package com.cooshare.os.dev.localcontrol;

/**************************************************************************************************
Filename:       NioClientThread.java
Revised:        $Date: 2014-01-10 17:22:23
Revision:       $Revision: cos v1.0.1 $

Description:    


Copyright CooShare Inc. All rights reserved.

IMPORTANT: Your use of this Software is limited to those specific rights
granted under the terms of a software license agreement between the user
who downloaded the software, his/her employer (which must be your employer)
and CooShare Inc.  You may not use this Software unless you agree to abide 
by the terms of the License. The License limits your use, and you acknowledge, 
that the Software may not be modified, copied or distributed unless embedded 
on a CooShare Inc license .  Other than for the foregoing purpose, you may not 
use, reproduce, copy, prepare derivative works of, modify, distribute, perform, 
display or sell this Software and/or its documentation for any purpose.

Should you have any questions regarding your right to use this Software,
contact CooShare Inc. at www.cooshare.com. 

**************************************************************************************************/



import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/****

本JAVA文件是一个工具类，提供本地控制等相关方法，用户无需修改本类！

*/

public class NioClientThread extends HandlerThread {
	
	private Handler mHandler = null;
	private final String TAG = "NioClientThread";
	private String SEND_DATA = "send test from client";
	
	public NioClientThread() {
		super("NioClientThread");
	}
	
	public boolean init() {
		super.start();
		mHandler = new PrivateHandler(getLooper());
		return sendMessage(PrivateHandler.MSG_INIT, null);
	}
	
	public boolean uninit() {
		return sendMessage(PrivateHandler.MSG_UNINIT, null);
	}
	
	public boolean connect(String remoteIp) {
		return sendMessage(PrivateHandler.MSG_CONNECT, remoteIp);
	}
	
	public boolean sendString(String content) {
		SEND_DATA = content;
		return sendMessage(PrivateHandler.MSG_SEND_DATA, content);
	}
		
	private boolean sendMessage(int messageId, Object object) {
		if (mHandler != null) {
			Log.i(TAG,"sendMessage mHandler != null");
			Message message = Message.obtain();
			message.what = messageId;
			message.obj = object;
			mHandler.sendMessage(message);
			return true;
		}else {
			Log.i(TAG,"sendMessage mHandler != null");
		}
		return false;
	}
		
	private class PrivateHandler extends Handler {
		
		public final static int MSG_INIT = 1;
		public final static int MSG_UNINIT = 2;
		public final static int MSG_HANDLE_SELECTOR = 3;
		public final static int MSG_CONNECT = 4;
		public final static int MSG_SEND_DATA = 5;
		
		private Selector mSelector = null;
		private final int HANDLE_SELECTOR_INTERVAL = 500;
		private final int SELECTION_TIMEOUT = 50;
		private boolean mIsCirculing = false;
		private SocketChannel mSocketChannel = null;
		
		@SuppressLint("HandlerLeak")
		public PrivateHandler(Looper looper) {
			super(looper);
		}
		
		public final static int TEST_PORT = 9999;
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			//Log.i(TAG, "handleMessage msg=" + msg);
			switch (msg.what) {
			case MSG_INIT:
				init();
				break;
			case MSG_UNINIT:
				uninit();
				break;
			case MSG_HANDLE_SELECTOR:
				handleSelector();
				break;
			case MSG_CONNECT:
				connect((String)msg.obj, TEST_PORT);
				break;
			case MSG_SEND_DATA:
				sendData();
				break;
			default:
				break;
			}			
			super.handleMessage(msg);
		}
		
		private void init() {
			Log.i(TAG, "init");
			try {
				mSelector = Selector.open();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mIsCirculing = true;
			sendMessageDelayed(Message.obtain(this, MSG_HANDLE_SELECTOR), HANDLE_SELECTOR_INTERVAL);
		}
		
		private void uninit() {
			Log.i(TAG, "uninit");
			removeMessages(MSG_HANDLE_SELECTOR);
			mIsCirculing = false;
			getLooper().quit();
		}

		private void handleSelector() {
			if (mSelector != null && mSelector.isOpen()) {
				do {
					int keysCount = 0;
					try {
						keysCount = mSelector.select(SELECTION_TIMEOUT);
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
					if (keysCount < 1 || !mIsCirculing) {
						break;
					}
					
					Set<SelectionKey> set = mSelector.selectedKeys();
					Iterator<SelectionKey> it = set.iterator();
					while (it.hasNext()) {
						SelectionKey key = it.next();
						it.remove(); 
						try {
							if (key.isValid() && key.isReadable()) {
								doRead(key);
							}
							if (key.isValid() && key.isWritable()) {
								doWrite(key);
							}

							if (key.isValid()) {
								if (key.isAcceptable()) {
									doAccept(key);
								} else if (key.isConnectable()) {
									doConnect(key);
								}
							}
						} catch (CancelledKeyException e) {
							Log.w(TAG, "handleSelectMessage " + e.toString() + e.getCause());
							continue;
						} catch (IOException ioException) {
							Log.w(TAG, "handleSelectMessage ioException=" + ioException.toString() + ioException.getCause());
							continue;
						}
					}
				} while (true);

				if (mIsCirculing) {
					sendMessageDelayed(Message.obtain(this, MSG_HANDLE_SELECTOR), HANDLE_SELECTOR_INTERVAL);
				}
			}
		}
		
		private void connect(String remoteIp, int port) {
			Log.i(TAG, "connect");
			SocketChannel socketChannel;
			try {
				socketChannel = SocketChannel.open();
				socketChannel.configureBlocking(false);
				socketChannel.connect(new InetSocketAddress(remoteIp, port));
				socketChannel.register(mSelector, SelectionKey.OP_CONNECT);
				mSocketChannel = socketChannel;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	        
		}
		
		private void sendData() {
			
			try{
			
				if (mSocketChannel != null && mSelector != null) {
				SelectionKey key = mSocketChannel.keyFor(mSelector);
				int oldOpsSet = key.interestOps();
				key.interestOps(oldOpsSet | SelectionKey.OP_WRITE);							
				}
			
			}catch(Exception e){
				
				
			}
		}

		
		private void doConnect(SelectionKey key) throws IOException {
			Log.i(TAG, "doConnect");
			SocketChannel socketChannel = (SocketChannel) key.channel();
			socketChannel.finishConnect();
			setSocketOption(socketChannel.socket());
			key.interestOps(SelectionKey.OP_READ);
		}

		private void doAccept(SelectionKey key) {
			// TODO Auto-generated method stub
			Log.i(TAG, "doAccept");
		}
		
		private void doWrite(SelectionKey key) throws IOException {
			Log.i(TAG, "doWrite");
			SocketChannel socketChannel = (SocketChannel) key.channel();
			ByteBuffer buffer = ByteBuffer.wrap(SEND_DATA.getBytes());
			int i = socketChannel.write(buffer);
			Log.i(TAG, "doWrite  i="+ String.valueOf(i));
			int oldOpsSet = key.interestOps();
			key.interestOps(oldOpsSet & ~SelectionKey.OP_WRITE);
		}

		private void doRead(SelectionKey key) throws IOException {
			Log.i(TAG, "doRead");
			SocketChannel socketChannel = (SocketChannel) key.channel();
			ByteBuffer buffer = ByteBuffer.allocate(1024); 
			int readBytes = socketChannel.read(buffer);
			Log.i(TAG, "read data=" + new String(buffer.array()) + ", readBytes=" + readBytes);
		}
		
		private void setSocketOption(Socket socket) {
			try {
				socket.setTcpNoDelay(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
