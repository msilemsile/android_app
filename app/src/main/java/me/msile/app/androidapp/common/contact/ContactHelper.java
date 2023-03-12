package me.msile.app.androidapp.common.contact;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import me.msile.app.androidapp.common.contact.callback.OnGetContactListener;
import me.msile.app.androidapp.common.contact.model.ContactModel;
import me.msile.app.androidapp.common.core.ActivityMethodProxy;
import me.msile.app.androidapp.common.core.ActivityWeakRefHolder;
import me.msile.app.androidapp.common.core.ApplicationHolder;
import me.msile.app.androidapp.common.ui.activity.BaseActivity;

/**
 * 获取联系人
 */
public class ContactHelper extends ActivityWeakRefHolder implements ActivityMethodProxy {

    private static final int REQUEST_CODE_CONTACT_FROM_SYSTEM = 100;
    private OnGetContactListener onGetContactListener;

    public ContactHelper(@NonNull Activity activity) {
        super(activity);
        if (activity instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) activity;
            baseActivity.addActivityMethodCallback(this);
        }
    }

    public static ContactHelper with(Activity activity) {
        return new ContactHelper(activity);
    }

    public void getContact(OnGetContactListener onGetContactListener) {
        this.onGetContactListener = onGetContactListener;
        Activity holderActivityWithCheck = getHolderActivityWithCheck();
        if (holderActivityWithCheck == null) {
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            holderActivityWithCheck.startActivityForResult(intent, REQUEST_CODE_CONTACT_FROM_SYSTEM);
        } catch (Exception e) {
            Log.d("ContactHelper", "startGetContactFromSystem error");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CONTACT_FROM_SYSTEM) {
                handleContactFromSystem(data);
            }
        } else {
            if (onGetContactListener != null) {
                onGetContactListener.onError("取消选择");
            }
        }
    }

    /**
     * 处理获取联系人 从系统联系人APP
     */
    private void handleContactFromSystem(@Nullable Intent data) {
        Activity holderActivityWithCheck = getHolderActivityWithCheck();
        if (holderActivityWithCheck == null) {
            return;
        }
        if (data != null) {
            ContactModel contactModel = null;
            Uri contactUri = data.getData();
            if (contactUri != null) {
                Cursor cursor = null;
                try {
                    String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                    cursor = holderActivityWithCheck.getContentResolver().query(contactUri, projection,
                            null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        String number = cursor.getString(numberIndex);
                        String name = cursor.getString(nameIndex);
                        if (!TextUtils.isEmpty(number)) {
                            contactModel = new ContactModel();
                            contactModel.setName(name);
                            contactModel.setPhone(number);
                        }
                    }
                } catch (Exception e) {
                    Log.d("ContactFragment", "handleContactFromSystem error");
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
            if (onGetContactListener != null) {
                onGetContactListener.onGetContact(contactModel);
            }
        } else {
            if (onGetContactListener != null) {
                onGetContactListener.onError("获取失败");
            }
        }
    }

    @Override
    public void onClear() {
        onGetContactListener = null;
    }

    /**
     * 获取手机联系人 !!!(在子线程中调用，避免主线程阻塞)+(注意检查权限)
     *
     * @return 联系人列表
     */
    public static List<ContactModel> queryContactList() {
        Uri phoneContactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = null;
        List<ContactModel> contactModelList = new ArrayList<>();
        try {
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
            cursor = ApplicationHolder.getAppContext().getContentResolver().query(phoneContactUri, projection, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    String number = cursor.getString(numberIndex);
                    String name = cursor.getString(nameIndex);
                    if (!TextUtils.isEmpty(number)) {
                        number = number.replaceAll(" ", "")
                                .replace("+86", "")
                                .replace("-", "")
                                .trim();
                        Log.d("ContactHelper", "number = " + number);
                        ContactModel contactModel = new ContactModel();
                        contactModel.setName(name);
                        contactModel.setPhone(number);
                        contactModelList.add(contactModel);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ContactHelper", "queryContactList error");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return contactModelList;
    }

}
