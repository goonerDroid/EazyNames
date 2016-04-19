package com.project.sublime.eazynames.utils;

import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.project.sublime.eazynames.model.Events;

public class ErrorHandlers {

	/**
	 * @param events  event from response
	 * @param view1   container of no network
	 * @param view2   container of server error
	 */
	public static void errorView(Events events, View view1, View view2) {
			if (events.getValue() instanceof NoConnectionError || events.getValue() instanceof NetworkError) {
				view1.setVisibility(View.VISIBLE);
			} else if (events.getValue() instanceof ServerError || events.getValue() instanceof AuthFailureError ||
					events.getValue() instanceof ParseError || events.getValue() instanceof TimeoutError) {
				view2.setVisibility(View.VISIBLE);
			}
	}

}