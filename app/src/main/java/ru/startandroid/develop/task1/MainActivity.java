package ru.startandroid.develop.task1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private AppCompatEditText inputText;
    private AppCompatEditText inputText2;
    private AppCompatEditText inputText3;
    private AppCompatButton confirmButton;
    private AppCompatTextView resultText;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "myKey";

    private final Period[] arrayOfPeriods = new Period[4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.input_text);
        inputText2 = findViewById(R.id.input_text2);
        inputText3 = findViewById(R.id.input_text3);
        confirmButton = findViewById(R.id.confirm_button);
        resultText = findViewById(R.id.result_text);

        init();

        confirmButton.setOnClickListener(v -> {
            String day = Objects.requireNonNull(inputText.getText()).toString();
            String month = Objects.requireNonNull(inputText2.getText()).toString();
            String year = Objects.requireNonNull(inputText3.getText()).toString();
            Date inputDate = createDate(day, month, year);
            if (inputDate == null) {
                String error = "ВВЕДИТЕ КОРРЕКТНОЕ ЧИСЛО";
                resultText.setText(error);
            } else {
                String period = getPeriod(inputDate, arrayOfPeriods);
                resultText.setText(period);
                saveData(this, period);
            }
            hideKeyBoard();
        });
    }

    private Date createDate(String day, String month, String year) {
        int dayInt;
        int monthInt;
        int yearInt;
        try {
            dayInt = Integer.parseInt(day);
            monthInt = Integer.parseInt(month);
            yearInt = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            return null;
        }
        return new Date(yearInt, monthInt, dayInt);
    }

    private String getPeriod(Date inputDate, Period[] arrayOfPeriods) {
        String period = "";
        for (Period currentPeriod : arrayOfPeriods) {
            if (checkPeriod(inputDate, currentPeriod.dateOfStart, currentPeriod.dateOfEnd)) {
                period = currentPeriod.nameOfPeriod;
                break;
            }
        }
        if (period.isEmpty()) {
            period = "ПЕРИОД НЕ НАЙДЕН";
        }
        return period;
    }

    private Boolean checkPeriod(Date current, Date min, Date max) {
        boolean result;
        result = current.after(min) && current.before(max);
        return result;

    }

    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void saveData(Context context, String text) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY, text);
        editor.apply();
    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(KEY, "");
    }

    private void init() {
        arrayOfPeriods[0] = new Period("бэби-бумеры", new Date(1946, 1, 1), new Date(1964, 12, 31));
        arrayOfPeriods[1] = new Period("поколение X", new Date(1965, 1, 1), new Date(1980, 12, 31));
        arrayOfPeriods[2] = new Period("поколение Y или миллениалы", new Date(1981, 1, 1), new Date(1996, 12, 31));
        arrayOfPeriods[3] = new Period("поколение Z или зумеры", new Date(1997, 1, 1), new Date(2012, 12, 31));
        resultText.setText(loadData(this));
    }

    public static class Period {

        public Period(String nameOfPeriod, Date dateOfStart, Date dateOfEnd) {
            this.nameOfPeriod = nameOfPeriod;
            this.dateOfStart = dateOfStart;
            this.dateOfEnd = dateOfEnd;
        }

        String nameOfPeriod;
        Date dateOfStart;
        Date dateOfEnd;
    }
}