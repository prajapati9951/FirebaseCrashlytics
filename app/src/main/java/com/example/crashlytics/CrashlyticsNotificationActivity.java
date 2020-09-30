package com.example.crashlytics;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;

public class CrashlyticsNotificationActivity extends AppCompatActivity implements View.OnClickListener{
	private static final String TAG = CrashlyticsNotificationActivity.class.getSimpleName();
	private FirebaseCrashlytics crashlytics;
	public static final String CHANNEL_1_ID = "channel1";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crashlytics_notification);
		createNotification();
		subscribeTopic();
		bindWidget();
		crashlytics = FirebaseCrashlytics.getInstance();
		crashlytics.log("Start logging!");
	}
	private void subscribeTopic() {
		FirebaseMessaging.getInstance().subscribeToTopic("genral")
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						String msg = "Successful";
						if (!task.isSuccessful()) {
							msg = "Failed";
						}
						Log.d(TAG, msg);
						Toast.makeText(CrashlyticsNotificationActivity.this, msg, Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void createNotification() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel1 = new NotificationChannel(
					CHANNEL_1_ID,
					"Channel 1",
					NotificationManager.IMPORTANCE_HIGH
			);
			channel1.setDescription("This is Channel 1");
		}
	}

	private void bindWidget() {
		findViewById(R.id.btn_force).setOnClickListener(this);
		findViewById(R.id.btn_exception).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_force:
				crashlytics.log("Log some message before a crash happen");
				throw new RuntimeException("Test Crash");
			case R.id.btn_exception:
				Button btn = findViewById(R.id.btn_exception);
				btn.setTextColor(null);
				break;
		}
	}
}