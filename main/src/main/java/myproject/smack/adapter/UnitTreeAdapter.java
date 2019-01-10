package myproject.smack.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;

import java.util.ArrayList;
import java.util.List;

import myproject.R;
import myproject.smack.bean.PersonBean;
import myproject.smack.myview.tree.Node;
import myproject.smack.myview.tree.TreeRecyclerAdapter;
import project.utils.PDpAndPx;

/**
 * Created by bangyong.zhang on 2018/6/27.
 */
public class UnitTreeAdapter extends TreeRecyclerAdapter {

    private List<String> listPos = new ArrayList<>();

    /**
     * @param mTree              RecyclerView
     * @param context            上下文
     * @param datas              数据集
     * @param defaultExpandLevel 默认展开层级数 0为不展开
     * @param iconExpand         展开的图标
     * @param iconNoExpand       闭合的图标
     */
    public UnitTreeAdapter(RecyclerView mTree, Context context, List<Node> datas, int defaultExpandLevel, int iconExpand, int iconNoExpand) {
        super(mTree, context, datas, defaultExpandLevel, iconExpand, iconNoExpand);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(View.inflate(mContext, R.layout.adapter_tree_unit_item, null));
    }

    @Override
    public void onBindViewHolder(final Node node, RecyclerView.ViewHolder holder, final int position) {

        final MyHolder viewHolder = (MyHolder) holder;

        if (!node.isHaveChild()) {
            viewHolder.image.setImageDrawable(Utils.getApp().getResources().getDrawable(R.drawable.sjdc_police_head));
            PersonBean bean = (PersonBean) node.bean;
            String tvValue = "   警号：" + bean.getUserName() +
                    "     性别：" + bean.getUserSex() +
                    "     手机号：" + bean.getPhone();
            viewHolder.person.setText(tvValue);
        } else {
            viewHolder.image.setImageDrawable(Utils.getApp().getResources().getDrawable(R.drawable.sjdc_file));
        }

        if (node.getIcon() == -1) {
            viewHolder.icon.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(node.getIcon());
        }
        //该块有时会存在错位点击父节点效果，所以已抽离到fragment中，刷新重新点击后结果。
//        if (!node.isHaveChild()){
//            viewHolder.item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (node.isChecked()) {
//                        node.setChecked(false);
//                        viewHolder.person.setVisibility(View.GONE);
//                    } else {
//                        node.setChecked(true);
//                        viewHolder.person.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
//        }

        viewHolder.label.setText(node.getName());
        viewHolder.person.setVisibility(node.isChecked() ? View.VISIBLE : View.GONE);
//        initHeadView(viewHolder, node);//显示头部信息，有点丑，建议隐藏
    }

    private void initHeadView(MyHolder viewHolder, Node node) {
        viewHolder.itemView.setPadding(0, 0, 0, 0);
        viewHolder.item.setPadding((int) (node.getLevel() * (PDpAndPx.dip2px(Utils.getApp(), 18))),
                3, 3, 3);
        viewHolder.item_head.setPadding((int) (node.getLevel() * (PDpAndPx.dip2px(Utils.getApp(),
                18))), 3, 3, 3);
        viewHolder.item_head.setText(node.getHeadDesc());
        viewHolder.ll_head.setVisibility(node.isShowHead() ? View.VISIBLE : View.GONE);
    }


    private void reLoadClickView(Node node, MyHolder viewHolder) {
        if (listPos.size() == 0) return;
        for (String nodeId : listPos) {
            if (nodeId.equals(node.getId())) {
                Log.e("---", nodeId + "---------" + node.getId());
                viewHolder.person.setVisibility(View.VISIBLE);
            } else {
                viewHolder.person.setVisibility(View.GONE);
            }
        }
    }

    private void moveAndAddClickView(Node node, MyHolder viewHolder) {
        int visibility = viewHolder.person.getVisibility();
        boolean b = visibility == View.VISIBLE;
        boolean g = visibility == View.GONE;
        int index = 0;
        for (int i = 0; i < listPos.size(); i++) {
            if (node.getId().equals(listPos.get(i))) {
                listPos.remove(i);
                viewHolder.person.setVisibility(View.GONE);
                index = 1;
            }
        }

        if (index == 0) {
            listPos.add((String) node.getId());
            viewHolder.person.setVisibility(View.VISIBLE);
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {

        public ImageView image;

        public TextView label;

        public ImageView icon;

        public TextView person;

        public TextView head;

        public LinearLayout ll_head;

        public View line;

        public RelativeLayout item;

        public TextView item_head;

        public MyHolder(View itemView) {
            super(itemView);
            item_head = (TextView) itemView.findViewById(R.id.item_head);
            item = (RelativeLayout) itemView.findViewById(R.id.item);
            line = (View) itemView.findViewById(R.id.item_line);
            ll_head = (LinearLayout) itemView
                    .findViewById(R.id.ll_item_head);
            head = (TextView) itemView
                    .findViewById(R.id.item_head);
            person = (TextView) itemView
                    .findViewById(R.id.tv_person_msg);
            image = (ImageView) itemView
                    .findViewById(R.id.cb_select_tree);
            label = (TextView) itemView
                    .findViewById(R.id.id_treenode_label);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }

    }
}
