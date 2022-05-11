package com.github.jameshnsears.quoteunquote.configure;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.github.jameshnsears.quoteunquote.R;
import com.github.jameshnsears.quoteunquote.configure.fragment.appearance.AppearanceFragment;
import com.github.jameshnsears.quoteunquote.configure.fragment.quotations.QuotationsFragment;
import com.github.jameshnsears.quoteunquote.configure.fragment.schedule.ScheduleFragment;
import com.github.jameshnsears.quoteunquote.configure.fragment.transfer.TransferFragment;
import com.github.jameshnsears.quoteunquote.database.DatabaseRepository;
import com.github.jameshnsears.quoteunquote.databinding.ActivityConfigureBinding;
import com.github.jameshnsears.quoteunquote.utils.IntentFactoryHelper;
import com.github.jameshnsears.quoteunquote.utils.audit.AuditEventHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import timber.log.Timber;

public class ConfigureActivity extends AppCompatActivity {
    public static boolean exportCalled;

    public int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Nullable
    public ActivityConfigureBinding activityConfigureBinding;

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = item -> {
        Fragment selectedFragment = getFragmentContentNewInstance();

        switch (item.getItemId()) {
            case R.id.navigationBarQuotations:
                selectedFragment = getFragmentContentNewInstance();
                break;

            case R.id.navigationBarAppearance:
                selectedFragment = AppearanceFragment.newInstance(widgetId);
                break;

            case R.id.navigationBarSchedule:
                selectedFragment = ScheduleFragment.newInstance(widgetId);
                break;

            case R.id.navigationBarBackupRestore:
                selectedFragment = TransferFragment.newInstance(widgetId);
                break;

            default:
                Timber.e("%d", item.getItemId());
        }

        String activeFragment = selectedFragment.getClass().getSimpleName();

        Timber.d("activeFragment=%s", activeFragment);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentPlaceholderContent, selectedFragment)
                .commit();

        activityConfigureBinding.scrollView.scrollTo(0, 0);

        return true;
    };

    public boolean broadcastFinishIntent = true;
    @Nullable

    private boolean finishCalled;

    ActivityResultLauncher<Intent> wikipediaActivityLancher = this.registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    this.finish();
                }
            });

    @Override
    public void finish() {
        // back pressed
        if (broadcastFinishIntent) {
            broadcastTheFinishIntent();
        }

        this.finishCalled = true;

        super.finish();
    }

    @Override
    public void onPause() {
        // back pressed | swipe up | export activity started
        if (!this.finishCalled && !ConfigureActivity.exportCalled) {
            this.finish();
        }

        super.onPause();
    }

    public void broadcastTheFinishIntent() {
        sendBroadcast(IntentFactoryHelper.createIntentAction(
                this, this.widgetId, IntentFactoryHelper.ACTIVITY_FINISHED_CONFIGURATION));

        setResult(Activity.RESULT_OK, IntentFactoryHelper.createIntent(this.widgetId));
    }

    @Override
    public void onBackPressed() {
        QuotationsFragment.ensureFragmentContentSearchConsistency(widgetId, getApplicationContext());

        super.onBackPressed();
    }

    @Override
    public void onCreate(@Nullable final Bundle bundle) {
        Timber.d("onCreate");
        super.onCreate(bundle);

        AuditEventHelper.createInstance(getApplication());
        final Intent intent = getIntent();
        final Bundle extras = intent.getExtras();

        final String wikipedia = extras.getString("wikipedia");
        if (wikipedia != null && !wikipedia.equals("?") && !wikipedia.equals("")) {
            this.linkToWikipedia(wikipedia);
        } else {
            widgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            broadcastFinishIntent = extras.getBoolean("broadcastFinishIntent", true);

            activityConfigureBinding = ActivityConfigureBinding.inflate(this.getLayoutInflater());
            this.setContentView(activityConfigureBinding.getRoot());

            final BottomNavigationView bottomNavigationView = findViewById(R.id.configureNavigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

            bottomNavigationView.setSelectedItemId(R.id.navigationBarQuotations);
        }
    }

    private void linkToWikipedia(@NonNull final String wikipedia) {
        Timber.d("wikipedia=%s", wikipedia);

        if (wikipedia.equals("r/quotes/")) {
            this.wikipediaActivityLancher.launch(
                    IntentFactoryHelper.createIntentActionView("https://www.reddit.com/" + wikipedia));
        } else {
            this.wikipediaActivityLancher.launch(
                    IntentFactoryHelper.createIntentActionView("https://en.wikipedia.org/wiki/" + wikipedia));
        }
    }

    @Override
    public void onDestroy() {
        Timber.d("onDestroy");

        DatabaseRepository.close(this.getApplicationContext());
        DatabaseRepository.databaseRepository = null;

        super.onDestroy();
    }

    @NonNull
    public QuotationsFragment getFragmentContentNewInstance() {
        return QuotationsFragment.newInstance(widgetId);
    }
}
