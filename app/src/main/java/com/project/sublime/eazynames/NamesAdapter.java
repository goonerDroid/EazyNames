package com.project.sublime.eazynames;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.sublime.eazynames.model.UsersBean;
import com.project.sublime.eazynames.widgets.LetterImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by goonerDroid on 18-04-2016.
 */
public class NamesAdapter extends RecyclerView.Adapter<NamesAdapter.ViewHolder> {

    private static final int MAX_ROW_DISPLAY = 5;
    private final View.OnClickListener onClickListener;
    private ArrayList<UsersBean.User> userArrayList;
    private StyleSpan boldStyleSpan;
    private boolean sortByLastName = false;


    public NamesAdapter(ArrayList<UsersBean.User> userArrayList ,
                        View.OnClickListener onClickListener) {
        this.userArrayList = userArrayList;
        this.onClickListener = onClickListener;
        this.boldStyleSpan = new StyleSpan(android.graphics.Typeface.BOLD);
    }

    @Override
    public NamesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NamesAdapter.ViewHolder holder, int position) {
        UsersBean.User user = userArrayList.get(position);
        if(sortByLastName) {
            holder.userName.setText(boldLastName(user));
        } else
            holder.userName.setText(user.getFirstName().concat("  " + user.getLastName()));

        holder.letterImageView.setLetter(user.getFirstName().charAt(0));
        holder.letterImageView.setOval(true);

        holder.rowContainer.setOnClickListener(onClickListener);
        holder.rowContainer.setTag(user);

    }

    @Override
    public int getItemCount() {
        if (userArrayList == null) {
            return 0;
        }
        return Math.min(MAX_ROW_DISPLAY, userArrayList.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView userName;
        @Bind(R.id.ll_container)
        LinearLayout rowContainer;
        @Bind(R.id.iv_name)
        LetterImageView letterImageView ;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void sortByLastName(boolean sort) {
        sortByLastName = sort;
    }

    private SpannableStringBuilder boldLastName(UsersBean.User user) {
        SpannableStringBuilder sb = new SpannableStringBuilder(
                user.getFirstName().concat("  " + user.getLastName()));
        sb.setSpan(boldStyleSpan, user.getFirstName().length()+2,
                user.getFirstName().length()+3, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }

}
