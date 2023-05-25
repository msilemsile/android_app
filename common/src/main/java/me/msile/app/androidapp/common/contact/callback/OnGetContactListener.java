package me.msile.app.androidapp.common.contact.callback;

import androidx.annotation.Nullable;

import me.msile.app.androidapp.common.contact.model.ContactModel;

public interface OnGetContactListener {

    void onGetContact(@Nullable ContactModel contactModel);

    void onError(String message);
}
