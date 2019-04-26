package com.example.cse.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class BakeWidget extends AppWidgetProvider {
    static String str;
    static SharedPreferences sp;

   /* static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        sp=context.getSharedPreferences("getstr",Context.MODE_PRIVATE);
        str=sp.getString("widgettext","No Data");




        Intent intent=new Intent(context,MainActivity.class);
        PendingIntent pending=PendingIntent.getActivity(context,1,intent,0);
        RemoteViews rv=new RemoteViews(context.getPackageName(),R.layout.bake_widget);
        rv.setOnClickPendingIntent(R.id.appwidget_text,pending);
        rv.setTextViewText(R.id.appwidget_text,str);
        *//*CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bake_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);
*//*
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }*/

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
           // updateAppWidget(context, appWidgetManager, appWidgetId);
            sp=context.getSharedPreferences("getstr",Context.MODE_PRIVATE);
            str=sp.getString("widgettext","No Data");




            Intent intent=new Intent(context,MainActivity.class);
            PendingIntent pending=PendingIntent.getActivity(context,1,intent,0);
            RemoteViews rv=new RemoteViews(context.getPackageName(),R.layout.bake_widget);
            rv.setOnClickPendingIntent(R.id.appwidget_text,pending);
            rv.setTextViewText(R.id.appwidget_text,str);
        /*CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bake_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);
*/
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, rv);

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

