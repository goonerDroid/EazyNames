package com.project.sublime.eazynames;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.project.sublime.eazynames.model.Events;
import com.project.sublime.eazynames.model.UsersBean;
import com.project.sublime.eazynames.service.RequestService;
import com.project.sublime.eazynames.utils.ErrorHandlers;
import com.project.sublime.eazynames.utils.Timber;
import com.project.sublime.eazynames.widgets.LetterImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RequestService requestService;
    private NamesAdapter namesAdapter;
    private ArrayList<UsersBean.User> usersList, pagedUsersList;

    private final int ITEMS_PER_PAGE = 5;
    private int mPage = 0;

    @Bind(R.id.pb_loading)
    ProgressBar progressBar;
    @Bind(R.id.rv_names)
    RecyclerView namesList;
    @Bind(R.id.connection_error_container)
    RelativeLayout noConnectionErrorLayout;
    @Bind(R.id.server_error_container)
    RelativeLayout serverErrorLayout;
    @Bind(R.id.rl_buttons_container)
    RelativeLayout buttonsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        requestService = RequestService.getInstance(this);

        usersList = new ArrayList<>();
        pagedUsersList = new ArrayList<>();
        namesAdapter = new NamesAdapter(pagedUsersList, this);

        namesList.setLayoutManager(new LinearLayoutManager(this));
        namesList.setAdapter(namesAdapter);
        fetchData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_first_name_a_z:
                sortName(true, true);
                return true;
            case R.id.sort_last_name_a_z:
                sortName(false, true);
                return true;
            case R.id.sort_first_name_z_a:
                sortName(true, false);
                return true;
            case R.id.sort_last_name_z_a:
                sortName(false, false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    private void sortName(boolean firstName, final boolean ascending) {
        if (usersList != null) {
            if (firstName) {//sorts firstname
                Collections.sort(usersList, new Comparator<UsersBean.User>() {
                    @Override
                    public int compare(UsersBean.User lhs, UsersBean.User rhs) {
                        if (ascending) {//sorts A-Z
                            return lhs.getFirstName().compareToIgnoreCase(rhs.getFirstName());
                        } else {//sorts Z-A
                            return rhs.getFirstName().compareToIgnoreCase(lhs.getFirstName());
                        }
                    }
                });

                namesAdapter.sortByLastName(false);
            } else {//sorts lastname
                Collections.sort(usersList, new Comparator<UsersBean.User>() {
                    @Override
                    public int compare(UsersBean.User lhs, UsersBean.User rhs) {
                        if (ascending) {//sorts A-Z
                            return lhs.getLastName().compareToIgnoreCase(rhs.getLastName());
                        } else {//sorts Z-A
                            return rhs.getLastName().compareToIgnoreCase(lhs.getLastName());
                        }
                    }
                });

                namesAdapter.sortByLastName(true);
            }
        }

        mPage = 0;
        pagedUsersList.clear();
        pagedUsersList.addAll(getFilteredListPerPage(mPage));
        namesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        showUserDetail(v);
    }

    @OnClick(R.id.btn_refresh_connection)
    public void onNetworkErrorClick() {
        progressBar.setVisibility(View.VISIBLE);
        fetchData();
        noConnectionErrorLayout.setVisibility(View.GONE);
        serverErrorLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_try_again_connection)
    public void onServerErrorClick() {
        progressBar.setVisibility(View.VISIBLE);
        fetchData();
        noConnectionErrorLayout.setVisibility(View.GONE);
        serverErrorLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_next)
    public void onNextClick() {
        if(mPage < getMaxPages()) {
            mPage++;
            pagedUsersList.clear();
            pagedUsersList.addAll(getFilteredListPerPage(mPage));
            namesAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.btn_previous)
    public void onPreviousClick() {
        if(mPage > 0) {
            mPage--;
            pagedUsersList.clear();
            pagedUsersList.addAll(getFilteredListPerPage(mPage));
            namesAdapter.notifyDataSetChanged();
        }
    }

    private List getFilteredListPerPage(int page) {
        int from = Math.max(0,page*5);
        int to = Math.min(usersList.size(),(page+1)*5);
        return usersList.subList(from, to);
    }

    private int getMaxPages() {
        return (int) Math.ceil(usersList.size()/ITEMS_PER_PAGE);
    }

    //Event capture for response from server.
    public void onEventMainThread(Events event) {
        if (event.getType() == Events.GET_USER_EVENT) {
            progressBar.setVisibility(View.GONE);
            if (event.getStatus()) {
                Timber.wtf("Parsed" + event.getValue());
                namesList.setVisibility(View.VISIBLE);
                buttonsContainer.setVisibility(View.VISIBLE);
                usersList.clear();
                usersList.addAll((ArrayList<UsersBean.User>) event.getValue());

                pagedUsersList.clear();
                pagedUsersList.addAll(getFilteredListPerPage(mPage));
                namesAdapter.sortByLastName(false);
                namesAdapter.notifyDataSetChanged();
            } else {
                ErrorHandlers.errorView(event, noConnectionErrorLayout, serverErrorLayout);
            }
        }
    }

    //fetches user data.
    private void fetchData() {
        requestService.getUsers();
    }


    private void showUserDetail(View v){
        int textColor = ((LetterImageView) v.findViewById(R.id.iv_name)).getBackgroundColor();
        UsersBean.User user = (UsersBean.User) v.getTag();
        BottomSheetDialogFragment bottomSheetDialogFragment = new UserDetailDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        args.putInt("textColor", textColor);
        bottomSheetDialogFragment.setArguments(args);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }


    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
