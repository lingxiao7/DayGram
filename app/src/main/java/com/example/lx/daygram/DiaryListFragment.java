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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Created by lx on 2016/9/23.
 */
public class DiaryListFragment extends ListFragment {
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
                                    diaryLab.deleteCrime(adapter.getItem(i));
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

        //itemA类的type标志
        private static final int TYPE_A = 0;
        //itemB类的type标志
        private static final int TYPE_B = 1;
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

        public int getItemViewType(String s) {
            int result = 0;
            if (s != null) {
                result = TYPE_A;
            } else{
                result = TYPE_B;
            }
            return result;
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
            //创建两种不同种类的viewHolder变量
            ViewHolder1 holder1 = null;
            ViewHolder2 holder2 = null;
            Diary d = getItem(position);
            int type = getItemViewType(d.getTitle());

            if (convertView == null) {
                //实例化
                holder1 = new ViewHolder1();
                holder2 = new ViewHolder2();
                //根据不同的type 来inflate不同的item layout
                //然后设置不同的tag
                //这里的tag设置是用的资源ID作为Key
                switch (type) {
                    case TYPE_A:
                        convertView = getActivity().getLayoutInflater()
                                .inflate(R.layout.list_item_diary, null);
                        holder1.dayTextView = (TextView) convertView.findViewById(R.id.diary_list_item_day);
                        holder1.dateTextView = (TextView) convertView.findViewById(R.id.diary_list_item_date);
                        holder1.titleTextView = (TextView) convertView.findViewById(R.id.diary_list_item_title);
                        convertView.setTag(R.id.tag_first, holder1);
                        break;
                    case TYPE_B:
                        convertView = getActivity().getLayoutInflater()
                                .inflate(R.layout.list_item_diary_dot, null);
                        holder2.dotImageView = (ImageView) convertView.findViewById(R.id.diary_list_item_dot);
                        convertView.setTag(R.id.tag_second, holder2);
                        break;
                }
            } else {
                //根据不同的type来获得tag
                switch (type) {
                    case TYPE_B:
                        /**
                        if (null == holder2)
                            holder2 = new ViewHolder2();*/
                        holder2 = (ViewHolder2) convertView.getTag(R.id.tag_second);
                        break;
                    case TYPE_A:
                        if (null == holder1) {
                            holder1 = new ViewHolder1();
                            holder1.dayTextView = (TextView) convertView.findViewById(R.id.diary_list_item_day);
                            holder1.dateTextView = (TextView) convertView.findViewById(R.id.diary_list_item_date);
                            holder1.titleTextView = (TextView) convertView.findViewById(R.id.diary_list_item_title);
                        }
                        holder1 = (ViewHolder1) convertView.getTag(R.id.tag_first);
                        break;
                }
            }

            d = getItem(position);
            //根据不同的type设置数据
            switch (type) {
                case TYPE_B:
                    //holder2.dotImageView.setImageResource(R.drawable.add_dot_btn);
                    break;
                case TYPE_A:
                    if (null == holder1) {
                        holder1 = new ViewHolder1();
                        holder1.dayTextView = (TextView) convertView.findViewById(R.id.diary_list_item_day);
                        holder1.dateTextView = (TextView) convertView.findViewById(R.id.diary_list_item_date);
                        holder1.titleTextView = (TextView) convertView.findViewById(R.id.diary_list_item_title);
                    }
                    if (null == holder1.dayTextView)
                        holder1.dayTextView = (TextView) convertView.findViewById(R.id.diary_list_item_day);
                    if (null == holder1.dateTextView)
                        holder1.dateTextView = (TextView) convertView.findViewById(R.id.diary_list_item_date);
                    if (null == holder1.titleTextView )
                        holder1.titleTextView = (TextView) convertView.findViewById(R.id.diary_list_item_title);
                    holder1.dayTextView.setText((String)DateFormat.format("E", d.getDate()));
                    holder1.dateTextView.setText((String)DateFormat.format("d", d.getDate()));
                    holder1.titleTextView.setText(d.getTitle());
                    break;
            }

            return convertView;
        }

        private class ViewHolder1 {
            TextView dayTextView;
            TextView dateTextView;
            TextView titleTextView;
        }

        private class ViewHolder2 {
            ImageView dotImageView;
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
                diaryLab.deleteCrime(diary);
                diaryLab.saveDiaries();
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
