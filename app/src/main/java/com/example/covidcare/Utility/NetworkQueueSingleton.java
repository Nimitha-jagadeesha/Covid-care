package com.example.covidcare.Utility;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

//This class handles all network request.
public class NetworkQueueSingleton
{
    private static NetworkQueueSingleton mInstance;
    private  RequestQueue requestQueue;
    private static Context context;

    private NetworkQueueSingleton(Context context)
    {
        this.context=context;
        requestQueue=getRequestQueue();
    }

    public RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
            requestQueue=Volley.newRequestQueue(context);
        return requestQueue;
    }
    public static synchronized NetworkQueueSingleton geInstance(Context context)
    {
        if(mInstance==null)
            mInstance= new NetworkQueueSingleton(context);

        return mInstance;
    }

    public <T> void addToRequestQueue(Request<T> request)
    {
        requestQueue.add(request);
    }
}
