package com.project.sublime.eazynames;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.TextView;

import com.project.sublime.eazynames.model.UsersBean;
import com.project.sublime.eazynames.widgets.LetterImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserDetailDialogFragment extends BottomSheetDialogFragment {

    private UsersBean.User user;
    @Bind(R.id.iv_name)
    LetterImageView letterImageView;
    @Bind(R.id.tv_name)
    TextView name;
    @Bind(R.id.tv_user_name)
    TextView userName;
    private int textColor;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null)        {
            user = (UsersBean.User) bundle.getSerializable("user");
            textColor = bundle.getInt("textColor");
        }
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
        ButterKnife.bind(this, contentView);
        letterImageView.setLetter(user.getFirstName().charAt(0));
        letterImageView.setOval(true);
        letterImageView.setBackgroundColor(textColor);

        name.setText(user.getFirstName().concat("  " + user.getLastName()));
        userName.setText("Username : @".concat(user.getUserName()));
        dialog.setContentView(contentView);


        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
 
        if( behavior != null && behavior instanceof BottomSheetBehavior ) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}