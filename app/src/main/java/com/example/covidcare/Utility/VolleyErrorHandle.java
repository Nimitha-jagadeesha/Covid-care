package com.example.covidcare.Utility;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class VolleyErrorHandle
{
    public static void handleError(VolleyError error, Context context)
    {
        if (error instanceof TimeoutError || error instanceof NoConnectionError)
        {
           showToast("Please check your internet",context);
        }
        else if (error instanceof AuthFailureError)
        {
            //dialobox for session has expired and take him to login
            Toast.makeText(context, "This session has expired please login again", Toast.LENGTH_LONG).show();

        }
        else if (error instanceof ServerError)
        {
            Toast.makeText(context, "Something went wrong please try after sometime", Toast.LENGTH_LONG).show();

        }
        else if (error instanceof NetworkError)
        {
            Toast.makeText(context, "Something went wrong please try after sometime", Toast.LENGTH_LONG).show();

        }
        else if (error instanceof ParseError)
        {
            //Add mechanism for user to repor error
            Toast.makeText(context, "Something went wrong please try after sometime", Toast.LENGTH_LONG).show();
        }
    }

    private static void showToast(String message,Context context)
    {
        Toast.makeText(context,message , Toast.LENGTH_LONG).show();

    }
}
