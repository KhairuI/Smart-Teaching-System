package com.example.smartteachingsystem.view.utils;


import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.jakewharton.rxbinding4.widget.RxTextView;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;

public class RxBindingHelper {

    public static Observable<String> getObservableFrom(TextInputEditText editText) {
        return RxTextView.textChanges(editText).skip(1).map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(CharSequence charSequence) throws Exception {
                        return charSequence.toString();
                    }
        });
    }
}
