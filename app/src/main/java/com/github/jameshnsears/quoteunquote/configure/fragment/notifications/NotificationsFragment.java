package com.github.jameshnsears.quoteunquote.configure.fragment.notifications;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.github.jameshnsears.quoteunquote.R;
import com.github.jameshnsears.quoteunquote.configure.fragment.FragmentCommon;
import com.github.jameshnsears.quoteunquote.databinding.FragmentNotificationsBinding;

@Keep
public class NotificationsFragment extends FragmentCommon {
    @Nullable
    public FragmentNotificationsBinding fragmentNotificationsBinding;

    @Nullable
    public NotificationsPreferences notificationsPreferences;

    public NotificationsFragment() {
        // dark mode support
    }

    public NotificationsFragment(final int widgetId) {
        super(widgetId);
    }

    @NonNull
    public static NotificationsFragment newInstance(final int widgetId) {
        final NotificationsFragment fragment = new NotificationsFragment(widgetId);
        fragment.setArguments(null);
        return fragment;
    }

    @Override
    @NonNull
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @NonNull final ViewGroup container,
            @NonNull final Bundle savedInstanceState) {
        notificationsPreferences = new NotificationsPreferences(this.widgetId, this.getContext());

        fragmentNotificationsBinding = FragmentNotificationsBinding.inflate(getLayoutInflater());
        return fragmentNotificationsBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentNotificationsBinding = null;
    }

    @Override
    public void onViewCreated(
            @NonNull final View view, @NonNull final Bundle savedInstanceState) {
        setNext();
        setDisplay();
        setDeviceUnlock();
        setDaily();
        setDailyTime();
        setBihourly();

        createListenerNextRandom();
        createListenerNextSequential();
        createListenerDisplayWidget();
        createListenerDisplayNotificationAndWidget();
        createListenerDeviceUnlock();
        createListenerDaily();
        createListenerDailyTime();
        createListenerBihourly();

        disableNotificationFunctionalityIfNecessary();
    }

    private void disableNotificationFunctionalityIfNecessary() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    getContext(), Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                fragmentNotificationsBinding.radioButtonWhereInWidget.setChecked(true);
                notificationsPreferences.setEventDisplayWidget(true);

                fragmentNotificationsBinding.radioButtonWhereAsNotification.setEnabled(false);
                fragmentNotificationsBinding.radioButtonWhereAsNotification.setChecked(false);
                fragmentNotificationsBinding.textViewNotificationSizeWarning.setEnabled(false);
                notificationsPreferences.setEventDisplayWidgetAndNotification(false);

                Toast.makeText(
                        this.getContext(),
                        this.getContext().getString(R.string.notification_permission_not_allowed),
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    private void setDaily() {
        final boolean booleanDaily = notificationsPreferences.getEventDaily();

        fragmentNotificationsBinding.checkBoxDailyAt.setChecked(booleanDaily);

        final TimePicker timePicker = fragmentNotificationsBinding.timePickerDailyAt;

        timePicker.setEnabled(false);
        if (booleanDaily) {
            timePicker.setEnabled(true);
        }
    }

    private void setNext() {
        fragmentNotificationsBinding.radioButtonNextRandom.setChecked(notificationsPreferences.getEventNextRandom());
        fragmentNotificationsBinding.radioButtonNextSequential.setChecked(notificationsPreferences.getEventNextSequential());
    }

    private void setDisplay() {
        fragmentNotificationsBinding.radioButtonWhereInWidget.setChecked(notificationsPreferences.getEventDisplayWidget());
        fragmentNotificationsBinding.radioButtonWhereAsNotification.setChecked(notificationsPreferences.getEventDisplayWidgetAndNotification());

        if (fragmentNotificationsBinding.radioButtonWhereAsNotification.isChecked()) {
            fragmentNotificationsBinding.textViewNotificationSizeWarning.setEnabled(true);
        } else {
            fragmentNotificationsBinding.textViewNotificationSizeWarning.setEnabled(false);
        }
    }

    private void setDeviceUnlock() {
        fragmentNotificationsBinding.checkBoxDeviceUnlock.setChecked(notificationsPreferences.getEventDeviceUnlock());
    }

    private void createListenerNextRandom() {
        final RadioButton radioButtonNextRandom = fragmentNotificationsBinding.radioButtonNextRandom;
        radioButtonNextRandom.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (notificationsPreferences.getEventNextRandom() != isChecked) {
                notificationsPreferences.setEventNextRandom(isChecked);
            }
        });
    }

    private void createListenerNextSequential() {
        final RadioButton radioButtonNextSequential = fragmentNotificationsBinding.radioButtonNextSequential;
        radioButtonNextSequential.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (notificationsPreferences.getEventNextSequential() != isChecked) {
                notificationsPreferences.setEventNextSequential(isChecked);
            }
        });
    }

    private void createListenerDisplayWidget() {
        final RadioButton radioButtonWhereInWidget = fragmentNotificationsBinding.radioButtonWhereInWidget;
        radioButtonWhereInWidget.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (notificationsPreferences.getEventDisplayWidget() != isChecked) {
                notificationsPreferences.setEventDisplayWidget(isChecked);
                fragmentNotificationsBinding.textViewNotificationSizeWarning.setEnabled(true);
            }
        });
    }

    private void createListenerDisplayNotificationAndWidget() {
        final RadioButton radioButtonWhereAsNotification = fragmentNotificationsBinding.radioButtonWhereAsNotification;
        radioButtonWhereAsNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (notificationsPreferences.getEventDisplayWidgetAndNotification() != isChecked) {
                notificationsPreferences.setEventDisplayWidgetAndNotification(isChecked);
                fragmentNotificationsBinding.textViewNotificationSizeWarning.setEnabled(false);
            }
        });
    }

    private void createListenerDeviceUnlock() {
        final CheckBox checkBoxDeviceUnlock = fragmentNotificationsBinding.checkBoxDeviceUnlock;
        checkBoxDeviceUnlock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (notificationsPreferences.getEventDeviceUnlock() != isChecked) {
                notificationsPreferences.setEventDeviceUnlock(isChecked);
            }
        });
    }

    private void createListenerDaily() {
        final CheckBox checkBoxDailyAt = fragmentNotificationsBinding.checkBoxDailyAt;
        checkBoxDailyAt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (notificationsPreferences.getEventDaily() != isChecked) {
                notificationsPreferences.setEventDaily(isChecked);
            }

            final TimePicker timePicker = fragmentNotificationsBinding.timePickerDailyAt;

            timePicker.setEnabled(false);
            if (isChecked) {
                timePicker.setEnabled(true);
            }
        });
    }

    private void createListenerDailyTime() {
        final TimePicker timePicker = fragmentNotificationsBinding.timePickerDailyAt;
        timePicker.setOnTimeChangedListener((view1, hourOfDay, minute) -> {
                    int h = timePicker.getHour();
                    if (notificationsPreferences.getEventDailyTimeHour() != h) {
                        notificationsPreferences.setEventDailyTimeHour(h);
                    }

                    int m = timePicker.getMinute();
                    if (notificationsPreferences.getEventDailyTimeMinute() != m) {
                        notificationsPreferences.setEventDailyTimeMinute(m);
                    }
                }
        );
    }

    private void createListenerBihourly() {
        final CheckBox checkBoxBihourly = fragmentNotificationsBinding.checkBoxBihourly;
        checkBoxBihourly.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (notificationsPreferences.getEventBihourly() != isChecked) {
                notificationsPreferences.setEventBihourly(isChecked);
            }
        });
    }

    protected void setDailyTime() {
        final TimePicker timePicker = fragmentNotificationsBinding.timePickerDailyAt;

        final int hourOfDay = notificationsPreferences.getEventDailyTimeHour();
        if (hourOfDay == -1) {
            notificationsPreferences.setEventDailyTimeHour(6);
            timePicker.setHour(6);
        } else {
            timePicker.setHour(hourOfDay);
        }

        final int minute = notificationsPreferences.getEventDailyTimeMinute();
        if (minute == -1) {
            notificationsPreferences.setEventDailyTimeMinute(0);
            timePicker.setMinute(0);
        } else {
            timePicker.setMinute(minute);
        }

        timePicker.setIs24HourView(false);
    }

    private void setBihourly() {
        fragmentNotificationsBinding.checkBoxBihourly.setChecked(notificationsPreferences.getEventBihourly());
    }
}
