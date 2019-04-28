package com.udacity.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.RecipeActivity;
import com.udacity.bakingapp.vo.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {
    public static final String EXTRA_ITEM = "bakingAppWidget.EXTRA_ITEM";
    public static final String ACTIVITY_ACTION = "bakingAppWidget.ACTIVITY_ACTION";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        if (recipe != null) {
            views.setTextViewText(R.id.recipe_name_widget, recipe.getName());

            Intent intent = new Intent(context, BakingAppWidgetService.class);
            views.setRemoteAdapter(R.id.list_view_widget, intent);

            views.setEmptyView(R.id.list_view_widget, android.R.id.empty);

            Intent activityIntent = new Intent(context, RecipeActivity.class);
            activityIntent.putExtra(RecipeActivity.EXTRA_RECIPE_ID, recipe.getId());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widgetContainer, pendingIntent);

            Intent toastIntent = new Intent(context, BakingAppWidget.class);
            toastIntent.setAction(BakingAppWidget.ACTIVITY_ACTION);
            toastIntent.putExtra(RecipeActivity.EXTRA_RECIPE_ID, recipe.getId());
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.list_view_widget, toastPendingIntent);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, null);
        }
    }

    public static void updateBakingAppWidgets(Context context,
                                              AppWidgetManager appWidgetManager, int[] appWidgetIds,
                                              Recipe recipe) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view_widget);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(ACTIVITY_ACTION)) {
            int recipeId = intent.getIntExtra(RecipeActivity.EXTRA_RECIPE_ID,
                    0);

            Intent intentAct = new Intent(context, RecipeActivity.class);
            intentAct.putExtra(RecipeActivity.EXTRA_RECIPE_ID, recipeId);

            context.startActivity(intentAct);
        }
        super.onReceive(context, intent);
    }
}

