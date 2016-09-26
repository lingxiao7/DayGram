package com.example.lx.daygram;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lx on 2016/9/23.
 * Class: DiaryDetailListFragment
 * This is a Diray's details ListFragment. Show all Diary's details by order. Click
 * any of these, you can edit on a DiaryEditFragment.
 */
public class DiaryDetailsListFragment extends ListFragment {
    private static final String TAG = "DiaryListFragment";


    private ArrayList<Diary> mDiaries;
    private boolean isNeedSaved;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.diaries_title);
        mDiaries = DiaryLab.get(getActivity()).getDiaries();
        isNeedSaved = false;
        /**ArrayAdapter<Diary> adapter =
         new ArrayAdapter<Diary>(getActivity(),
         android.R.layout.simple_list_item_1,
         mDiaries);**/
        DiaryAdapter adapter = new DiaryAdapter(getActivity(), mDiaries);
        setListAdapter(adapter);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ListView listView = (ListView)v.findViewById(android.R.id.list);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            registerForContextMenu(listView);
        } else {
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.diary_list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_item_delete_diary:
                            DiaryAdapter adapter = (DiaryAdapter)getListAdapter();
                            DiaryLab diaryLab = DiaryLab.get(getActivity());
                            for (int i = adapter.getCount() - 1; i >= 0; i--)
                                if (getListView().isItemChecked(i)) {
                                    diaryLab.deleteDiary(adapter.getItem(i));
                                    isNeedSaved = true;
                                }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isNeedSaved) {
            isNeedSaved = false;
            DiaryLab.get(getActivity()).saveDiaries();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);
        Diary d = ((DiaryAdapter)getListAdapter()).getItem(position);
        Log.d(TAG, d.getTitle() + "was clicked");

        // Start DiaryEditActivity
        // Intent i = new Intent(getActivity(), DiaryActivity.class);
        // Start DiaryPagerActivity
        Intent i = new Intent(getActivity(), DiaryPagerActivity.class);
        //i.putExtra(DiaryEditFragment.EXTRA_DIARY_DATE, d.getDate());
        i.putExtra(DiaryFragment.EXTRA_DIARY_DATE, d.getDate());
        i.putExtra(DiaryEditFragment.EXTRA_DIARY_DATE, d.getDate());
        startActivity(i);
    }

    private class DiaryAdapter extends BaseAdapter {

        private Context mContext = null;
        private List<Diary> mDiaries = null;

        public DiaryAdapter(Context context, ArrayList<Diary> diaries) {
            mContext = context;
            mDiaries = diaries;
        }

        @Override
        public int getCount() {
            int count = 0;
            if (null != mDiaries) {
                count = mDiaries.size();
            }
            return count;
        }


        @Override
        public Diary getItem(int position) {
            Diary item = null;

            if (null != mDiaries) {
                item = mDiaries.get(position);
            }

            return item;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Diary d = getItem(position);
            convertView = getActivity().getLayoutInflater()
                    .inflate(R.layout.list_item_diary_details, null);
            if (d != null) {
                TextView textTextView = (TextView) convertView.findViewById(R.id.diary_details);
                String text = DateFormat.format("dd EEEE / ", d.getDate()).toString();
                if (d.getText() != null) text += d.getText().toString();
                textTextView.setText(text);
            }
            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DiaryAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.diary_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        DiaryAdapter adapter = (DiaryAdapter)getListAdapter();
        Diary diary = adapter.getItem(position);

        switch (item.getItemId()) {
            case R.id.menu_item_delete_diary:
                DiaryLab diaryLab =  DiaryLab.get(getActivity());
                diaryLab.deleteDiary(diary);
                diaryLab.saveDiaries();
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
