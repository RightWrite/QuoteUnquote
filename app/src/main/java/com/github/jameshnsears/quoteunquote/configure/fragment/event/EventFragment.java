package com.github.jameshnsears.quoteunquote.configure.fragment.event;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.jameshnsears.quoteunquote.configure.fragment.FragmentCommon;
import com.github.jameshnsears.quoteunquote.databinding.FragmentEventBinding;

public class EventFragment extends FragmentCommon {
    @Nullable
    public FragmentEventBinding fragmentEventBinding;

    @Nullable
    public EventPreferences eventPreferences;

    protected EventFragment(final int widgetId) {
        super(widgetId);
    }

    @NonNull
    public static EventFragment newInstance(final int widgetId) {
        final EventFragment fragment = new EventFragment(widgetId);
        fragment.setArguments(null);
        return fragment;
    }

    @Override
    @NonNull
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {
        eventPreferences = new EventPreferences(this.widgetId, this.getContext());

        fragmentEventBinding = FragmentEventBinding.inflate(getLayoutInflater());
        return fragmentEventBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentEventBinding = null;
    }

    @Override
    public void onViewCreated(
            @NonNull final View view, final Bundle savedInstanceState) {
        setNext();
        setDisplay();
        setDeviceUnlock();
        setDaily();
        setDailyTime();

        createListenerNextRandom();
        createListenerNextSequential();
        createListenerDisplayWidget();
        createListenerDisplayWidgetAndNotification();
        createListenerDeviceUnlock();
        createListenerDaily();
        createListenerDailyTime();
    }

    private void setDaily() {
        final boolean booleanDaily = eventPreferences.getEventDaily();

        fragmentEventBinding.checkBoxDailyAt.setChecked(booleanDaily);

        final TimePicker timePicker = fragmentEventBinding.timePickerDailyAt;

        timePicker.setEnabled(false);
        if (booleanDaily) {
            timePicker.setEnabled(true);
        }
    }

    private void setNext() {
        fragmentEventBinding.radioButtonNextRandom.setChecked(eventPreferences.getEventNextRandom());
        fragmentEventBinding.radioButtonNextSequential.setChecked(eventPreferences.getEventNextSequential());
    }

    private void setDisplay() {
        fragmentEventBinding.radioButtonWhereInWidget.setChecked(eventPreferences.getEventDisplayWidget());
        fragmentEventBinding.radioButtonWhereAsNotification.setChecked(eventPreferences.getEventDisplayWidgetAndNotification());
    }

    private void setDeviceUnlock() {
        fragmentEventBinding.checkBoxDeviceUnlock.setChecked(eventPreferences.getEventDeviceUnlock());
    }

    private void createListenerNextRandom() {
        final RadioButton radioButtonNextRandom = fragmentEventBinding.radioButtonNextRandom;
        radioButtonNextRandom.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (eventPreferences.getEventNextRandom() != isChecked) {
                eventPreferences.setEventNextRandom(isChecked);
            }
        });
    }

    private void createListenerNextSequential() {
        final RadioButton radioButtonNextSequential = fragmentEventBinding.radioButtonNextSequential;
        radioButtonNextSequential.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (eventPreferences.getEventNextSequential() != isChecked) {
                eventPreferences.setEventNextSequential(isChecked);
            }
        });
    }

    private void createListenerDisplayWidget() {
        final RadioButton radioButtonWhereInWidget = fragmentEventBinding.radioButtonWhereInWidget;
        radioButtonWhereInWidget.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (eventPreferences.getEventDisplayWidget() != isChecked) {
                eventPreferences.setEventDisplayWidget(isChecked);
            }
        });
    }

    private void createListenerDisplayWidgetAndNotification() {
        final RadioButton radioButtonWhereAsNotification = fragmentEventBinding.radioButtonWhereAsNotification;
        radioButtonWhereAsNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (eventPreferences.getEventDisplayWidgetAndNotification() != isChecked) {
                eventPreferences.setEventdisplayWidgetAndNotification(isChecked);
            }
        });
    }

    private void createListenerDeviceUnlock() {
        final CheckBox checkBoxDeviceUnlock = fragmentEventBinding.checkBoxDeviceUnlock;
        checkBoxDeviceUnlock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (eventPreferences.getEventDeviceUnlock() != isChecked) {
                eventPreferences.setEventDeviceUnlock(isChecked);
            }
        });
    }

    private void createListenerDaily() {
        final CheckBox checkBoxDailyAt = fragmentEventBinding.checkBoxDailyAt;
        checkBoxDailyAt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (eventPreferences.getEventDaily() != isChecked) {
                eventPreferences.setEventDaily(isChecked);
            }

            final TimePicker timePicker = fragmentEventBinding.timePickerDailyAt;

            timePicker.setEnabled(false);
            if (isChecked) {
                timePicker.setEnabled(true);
            }
        });
    }

    private void createListenerDailyTime() {
        final TimePicker timePicker = fragmentEventBinding.timePickerDailyAt;
        timePicker.setOnTimeChangedListener((view1, hourOfDay, minute) -> {
                    int h = timePicker.getHour();
                    if (eventPreferences.getEventDailyTimeHour() != h) {
                        eventPreferences.setEventDailyTimeHour(h);
                    }

                    int m = timePicker.getMinute();
                    if (eventPreferences.getEventDailyTimeMinute() != m) {
                        eventPreferences.setEventDailyTimeMinute(m);
                    }
                }
        );
    }

    protected void setDailyTime() {
        final TimePicker timePicker = fragmentEventBinding.timePickerDailyAt;

        final int hourOfDay = eventPreferences.getEventDailyTimeHour();
        if (hourOfDay == -1) {
            eventPreferences.setEventDailyTimeHour(6);
            timePicker.setHour(6);
        } else {
            timePicker.setHour(hourOfDay);
        }

        final int minute = eventPreferences.getEventDailyTimeMinute();
        if (minute == -1) {
            eventPreferences.setEventDailyTimeMinute(0);
            timePicker.setMinute(0);
        } else {
            timePicker.setMinute(minute);
        }

        timePicker.setIs24HourView(false);
    }
}
