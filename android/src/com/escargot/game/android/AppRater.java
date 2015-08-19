package com.escargot.game.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class AppRater {
	private final static String APP_PNAME = "com.escargot.game.android";// Package
																		// Name

	private final static int DAYS_UNTIL_PROMPT = 3;// Min number of days
	private final static int LAUNCHES_UNTIL_PROMPT = 3;// Min number of launches

	public static void app_launched(Context mContext) {
		SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
		if (prefs.getBoolean("dontshowagain", false)) {
			return;
		}

		SharedPreferences.Editor editor = prefs.edit();

		// Increment launch counter
		long launch_count = prefs.getLong("launch_count", 0) + 1;
		editor.putLong("launch_count", launch_count);

		// Get date of first launch
		Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
		if (date_firstLaunch == 0) {
			date_firstLaunch = System.currentTimeMillis();
			editor.putLong("date_firstlaunch", date_firstLaunch);
		}

		// Wait at least n days before opening
		if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
			if (System.currentTimeMillis() >= date_firstLaunch
					+ (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
				showRateDialog(mContext, editor);
			}
		}

		editor.commit();
	}

	public static void showRateDialog(final Context mContext,
			final SharedPreferences.Editor editor) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(R.string.rta_dialog_title);

		dialog.setMessage(R.string.rta_dialog_message);
		dialog.setPositiveButton(R.string.rta_dialog_ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("https://play.google.com/store/apps/details?id="
								+ APP_PNAME)));
				if (editor != null) {
					editor.putBoolean("dontshowagain", true);
					editor.commit();
				}
			}
		});
		dialog.setNeutralButton(R.string.rta_dialog_cancel,
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (editor != null) {
							editor.clear();
							editor.commit();
						}
					}
				});
		dialog.setNegativeButton(R.string.rta_dialog_no, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (editor != null) {
					editor.putBoolean("dontshowagain", true);
					editor.commit();
				}
			}
		});
		dialog.create().show();
	}

	public static void showRateDialog(final Context mContext) {
		SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
		final SharedPreferences.Editor editor = prefs.edit();
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(R.string.rta_dialog_title);

		dialog.setMessage(R.string.rta_dialog_message);
		dialog.setPositiveButton(R.string.rta_dialog_ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("https://play.google.com/store/apps/details?id="
								+ APP_PNAME)));
				if (editor != null) {
					editor.putBoolean("dontshowagain", true);
					editor.commit();
				}
			}
		});
		dialog.setNeutralButton(R.string.rta_dialog_cancel,
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (editor != null) {
							editor.clear();
							editor.commit();
						}
					}
				});
		dialog.setNegativeButton(R.string.rta_dialog_no, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (editor != null) {
					editor.putBoolean("dontshowagain", true);
					editor.commit();
				}
			}
		});
		dialog.create().show();
	}
}