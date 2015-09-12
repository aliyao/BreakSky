package com.yao.breaksky.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.yao.breaksky.R;
import com.yao.breaksky.activity.MainActivity;
import com.yao.breaksky.fragment.dummy.DummyContent;
import com.yao.breaksky.net.HttpUrl;
import com.yao.breaksky.tools.YOBitmap;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.KJLoger;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class FindItemFragment extends Fragment implements AbsListView.OnItemClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    /*
    <a style="position:relative;display:block;" title="张震讲故事之鬼迷心窍" target="_blank" href="http://www.id97.com/videos/resource/id/9189.html">
            <img alt="张震讲故事之鬼迷心窍" title="张震讲故事之鬼迷心窍" src="http://ww3.sinaimg.cn/large/0069qWFHgw1etwjkiath0j30bw0gogo8.jpg" height="230" width="100%">
        <button class="hdtag">高清</button>
        </a>

        "title=\"(.*?)\" target="_blank" href=\"(.*?)\">
            <img alt=".*?" title=".*?" src="(.*?)""
            "title=\"(.*?)\" target=\"_blank\" href=\"(.*?)\">.?<img alt=\".*?\" title=\".*?\" src=\"(.*?)\"";
            "title=\"(.*?)\" target=\"_blank\" href=\"(.*?)\">[\\s\\S]*?<img alt=\".*?\" title=\".*?\" src=\"(.*?)\"";
/
                              "title=\"(.*?)\" target=\"_blank\" href=\"(.*?)\">[\\s\\S]*?<img alt=\".*?\" title=\".*?\" src=\"(.*?)\".*?>[\\s\\S]*?</a>[\\s\\S]*?<span class=\"otherinfo\"> - (.*?)分</span></div>[\\s\\S]*?<div class=\"otherinfo\">类型：(.*?)</div>";
     */
    final String zhengZeItem = "title=\"(.*?)\" target=\"_blank\" href=\"(.*?)\">[\\s\\S]*?<img alt=\".*?\" title=\".*?\" src=\"(.*?)\".*?>([\\s\\S]*?)</a>[\\s\\S]*?<span class=\"otherinfo\"> - (.*?)分</span></div>[\\s\\S]*?<div class=\"otherinfo\">类型：(.*?)</div>";
    final String zhengZeId = "id/(.*?).html";
    final String zhengZeType = "<a.*?class=\"movietype\">(.*?)</a>";
   //final String zhengZeType = "class=\"movietype\">(.*?)</a>";
    final String zhengZeTag = "[\\s\\S]*?>(.*?)</";

    // final String zhengZe= "title=\"(.*?)\" target=\"_blank\" href=\"(.*?)\"";
    //final String zhengZe="title=\"(.*?)\" href=\"(.*?)\".*?363";
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters  String param1, String param2
    public static FindItemFragment newInstance(int sectionNumber) {
        FindItemFragment fragment = new FindItemFragment();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/

        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FindItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content
        mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                R.layout.fragment_finditem_list_item, DummyContent.ITEMS) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                if (convertView == null) {
                    view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.fragment_finditem_list_item, parent, false);
                } else {
                    view = convertView;
                }

                DummyContent.DummyItem mDummyItem = getItem(position);


                ImageView img = (ImageView) view.findViewById(R.id.image);
                TextView title = (TextView) view.findViewById(R.id.title);
                TextView type = (TextView) view.findViewById(R.id.type);
                TextView tag = (TextView) view.findViewById(R.id.tag);
                TextView score = (TextView) view.findViewById(R.id.score);
                //img.setImageResource(mDummyItem.getImgUrl());
                title.setText((String) mDummyItem.getContent());
                tag.setText(removeNull(mDummyItem.getTag()));
                score.setText(removeNull(mDummyItem.getScore())+"分");
                List<String>  typelist=mDummyItem.getType();
                String typeStr="类型：";
                if(typelist!=null&&typelist.size()>0){
                    for(int item=0;item<typelist.size();item++){
                        if (item==0){
                            typeStr=typeStr+typelist.get(item);
                        }else{
                            typeStr=typeStr+"|"+typelist.get(item);
                        }
                    }
                }else{
                    typeStr="";
                }
                type.setText(removeNull(typeStr));
                YOBitmap.getmKJBitmap().display(img,removeNull(mDummyItem.getImgUrl()));
                return view;
            }
        };
        httpGetFindList();
    }
    public String removeNull(Object mObject){
        if(mObject==null){
            return "";
        }
        return (String)mObject;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finditem, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    public void httpGetFindList() {
        KJHttp kjh = new KJHttp();
        /*HttpParams params = new HttpParams();
        params.put("id", "1"); //传递参数
        params.put("name", "kymjs");*/
        //HttpCallback中有很多方法，可以根据需求选择实现
        kjh.get(HttpUrl.FindList, new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
                KJLoger.debug("在请求开始之前调用");
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                ViewInject.longToast("请求成功");
                KJLoger.debug("log:" + t.toString());
                Pattern p = Pattern.compile(zhengZeItem);
                Matcher m = p.matcher(t.toString()); //csdn首页的源代码字符串
                List<Map<String, Object>> result = new ArrayList<>();
                while (m.find()) { //循环查找匹配字串
                    MatchResult mr = m.toMatchResult();
                    Map<String, Object> map = new HashMap<>();
                 /*   map.put("title", mr.group(1));//找到后group(1)是表达式第一个括号的内容
                    map.put("url", mr.group(2));//group(2)是表达式第二个括号的内容
                    map.put("imgurl", mr.group(3));//group(2)是表达式第三个括号的内容*//*

                    KJLoger.debug("groupCount--" + mr.groupCount());
                    for (int i = 1; i <= mr.groupCount(); i++) {
                        if (mr != null && mr.group(i) != null) {
                            KJLoger.debug("group(i)--" + i + "--" + mr.group(i));
                        }
                    }*/
                    for (int groupItem = 1; groupItem <= mr.groupCount(); groupItem++) {
                        if (mr != null && mr.group(groupItem) != null) {
                            //KJLoger.debug("group(i)--" + groupItem + "--" + mr.group(groupItem));
                            switch (groupItem) {
                                case 1:
                                    map.put("title", mr.group(groupItem));//找到后group(1)是表达式第一个括号的内容
                                    break;
                                case 2:
                                    map.put("url", mr.group(groupItem));//group(2)是表达式第二个括号的内容
                                    Pattern pID = Pattern.compile(zhengZeId);
                                    Matcher mID = pID.matcher(mr.group(groupItem)); //csdn首页的源代码字符串
                                    mID.find();
                                    map.put("id", mID.toMatchResult().group(1));//找到后group(1)是表达式第一个括号的内容
                                   // KJLoger.debug("--" + mID.toMatchResult().group(1));
                                    break;
                                case 3:
                                    map.put("imgurl", mr.group(groupItem));//group(2)是表达式第三个括号的内容
                                    break;
                                case 4:
                                    String htmlTag= mr.group(groupItem);
                                    if(!TextUtils.isEmpty(htmlTag.trim())){
                                        Pattern pTag = Pattern.compile(zhengZeTag);
                                        Matcher mTag  = pTag .matcher(mr.group(groupItem)); //csdn首页的源代码字符串
                                        mTag.find();
                                        map.put("tag", mTag .toMatchResult().group(1));//找到后group(1)是表达式第一个括号的内容
                                    }
                                    // map.put("tag", mr.group(groupItem));
                                    break;
                                case 5:
                                    map.put("score", mr.group(groupItem));
                                    break;
                                case 6:
                                    if(!TextUtils.isEmpty(mr.group(groupItem).trim())){
                                        Pattern pType = Pattern.compile(zhengZeType);
                                        Matcher mType = pType.matcher(mr.group(groupItem));
                                        List<String> typeList=new ArrayList<String>();
                                        while (mType.find()) { //循环查找匹配字串
                                            MatchResult mrType = mType.toMatchResult();
                                            for (int groupTypeItem = 1; groupTypeItem <= mrType.groupCount(); groupTypeItem++) {
                                                if (mrType != null && mrType.group(groupTypeItem) != null) {
                                                    typeList.add(mrType.group(groupTypeItem));
                                                }
                                            }
                                        }
                                        map.put("type",typeList);//找到后group(1)是表达式第一个括号的内容
                                    }

                                    break;

                            }
                        }
                    }
                    result.add(map);
                }
                //adapter = new ArrayAdapter(this,R.layout.view,R.id.textview1,list1);
                DummyContent.setData(result);
                ((ArrayAdapter) mAdapter).notifyDataSetChanged();

            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                KJLoger.debug("exception:" + strMsg);
            }


            @Override
            public void onFinish() {
                super.onFinish();
                KJLoger.debug("请求完成，不管成功或者失败都会调用");
            }


        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
