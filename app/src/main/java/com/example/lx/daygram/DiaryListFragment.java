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
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by lx on 2016/9/23.
 * DiaryListFragment. As the name shown, the fragment manage the diary list.
 * Click to edit and hold down to delete.
 */
public class DiaryListFragment extends ListFragment{
    private static final String TAG = "DiaryListFragment";
    public static final String EXTRA_DIARY_YEAR =
            "com.example.lx.daygram.diary_year";
    public static final String EXTRA_DIARY_MONTH =
            "com.example.lx.daygram.diary_month";


    private ArrayList<Diary> mDiaries;
    private ArrayList<DiaryDot> mDots;
    private boolean isNeedSaved;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.diaries_title);
        mDiaries = DiaryLab.get(getActivity()).getDiaries();
        mDots = DiaryDotLab.get(getActivity()).getDots();

//        String year = (String)getArguments().getSerializable(EXTRA_DIARY_YEAR);
//        String month = (String)getArguments().getSerializable(EXTRA_DIARY_MONTH);
//        mDiaries = DiaryLab.get(getActivity()).getDiaryMonth(year, month).getDiaries();
//        mDots = DiaryDotLab.get(getActivity()).getDiaryDotMonth(year, month).getDots();

        isNeedSaved = false;

        DiaryAdapter adapter = new DiaryAdapter(getActivity(), mDiaries, mDots);
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
                                    if (adapter.getItem(i) instanceof Diary) {
                                        diaryLab.deleteDiary((Diary) adapter.getItem(i));
                                        isNeedSaved = true;
                                    }
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);
        Object o = ((DiaryAdapter)getListAdapter()).getItem(position);
        if (o instanceof Diary) {
            Diary d = (Diary)o;
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
        else {
            Diary d = new Diary();
            d.setDate(((DiaryDot) o).getDate());
            mDiaries.add(d);
            Intent i = new Intent(getActivity(), DiaryPagerActivity.class);
            i.putExtra(DiaryEditFragment.EXTRA_DIARY_DATE, ((DiaryDot) o).getDate());
            startActivityForResult(i, 0);
        }
    }

    private class DiaryAdapter extends BaseAdapter {

        //itemA类的type标志
        private static final int TYPE_A = 0;
        //itemB类的type标志
        private static final int TYPE_B = 1;
        private Context mContext = null;
        //整合数据
        private List<Object> data = new ArrayList<>();

        public DiaryAdapter(Context context, ArrayList<Diary> diaries, ArrayList<DiaryDot> dots) {
            this.mContext = context;
            data.addAll(diaries);
            data.addAll(dots);

            Collections.sort(data, new MyComparator() );
        }

        @Override
        public int getCount() {
            int count = 0;
            if (null != data) {
                count = data.size();
            }
            return count;
        }

        @Override
        public int getItemViewType(int position) {
            int result = 0;
            if (data.get(position) instanceof Diary) {
                result = TYPE_A;
            } else {
                result = TYPE_B;
            }
            return result;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
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
            int type = getItemViewType(position);

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
                        holder2 = (ViewHolder2) convertView.getTag(R.id.tag_second);
                        break;
                    case TYPE_A:
                        holder1 = (ViewHolder1) convertView.getTag(R.id.tag_first);
                        break;
                }
            }

            Object o = data.get(position);
            //根据不同的type设置数据
            switch (type) {
                case TYPE_B:
                    DiaryDot dot = (DiaryDot) o;
                    if ("Sunday".equals(DateFormat.format("EEEE", dot.getDate()).toString()) )
                        holder2.dotImageView.setImageResource(R.drawable.add_red_dot_btn);
                    else holder2.dotImageView.setImageResource(R.drawable.add_dot_btn);
                    break;
                case TYPE_A:
                    Diary diary = (Diary)o;
                    if ("Sunday".equals(DateFormat.format("EEEE", diary.getDate()).toString()) )
                        holder1.dateTextView.setTextColor(getResources().getColor(R.color.colorRed));
                    holder1.dayTextView.setText(DateFormat.format("E", diary.getDate()));
                    holder1.dateTextView.setText(DateFormat.format("d", diary.getDate()));
                    holder1.titleTextView.setText(diary.getTitle());
                    break;
            }

            return convertView;
        }

        public class MyComparator implements Comparator {

            public int compare(Object arg0, Object arg1) {
                //根据不同的情况来进行排序

                if (arg0 instanceof Diary && arg1 instanceof DiaryDot) {

                    Diary a = (Diary) arg0;
                    DiaryDot b = (DiaryDot) arg1;
                    return a.getDate().compareTo(b.getDate());

                } else if (arg0 instanceof DiaryDot && arg1 instanceof Diary) {

                    DiaryDot b = (DiaryDot) arg0;
                    Diary a = (Diary) arg1;
                    return b.getDate().compareTo(a.getDate());
                } else if (arg0 instanceof Diary && arg1 instanceof Diary) {

                    Diary a0 = (Diary) arg0;
                    Diary a1 = (Diary) arg1;
                    return a0.getDate().compareTo(a1.getDate());

                } else {
                    DiaryDot b0 = (DiaryDot) arg0;
                    DiaryDot b1 = (DiaryDot) arg1;
                    return b0.getDate().compareTo(b1.getDate());
                }
            }
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
    public void onPause() {
        super.onPause();
        if (isNeedSaved) {
            isNeedSaved = false;
            DiaryLab.get(getActivity()).saveDiaries();
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
        Diary diary = (Diary) adapter.getItem(position);

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


//    public static DiaryListFragment newInstance(String year,String month) {
//        Bundle args = new Bundle();
//        args.putSerializable(EXTRA_DIARY_YEAR, year);
//        args.putSerializable(EXTRA_DIARY_YEAR, month);
//
//
//        DiaryListFragment fragment = new DiaryListFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
}