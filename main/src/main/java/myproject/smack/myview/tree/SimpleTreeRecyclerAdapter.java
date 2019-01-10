package myproject.smack.myview.tree;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import myproject.R;
import myproject.smack.myview.tree.Node;
import myproject.smack.myview.tree.TreeRecyclerAdapter;


/**
 * Created by bangyong.zhang on 2018/6/27.
 */
public class SimpleTreeRecyclerAdapter extends TreeRecyclerAdapter {


    /**
     * @param mTree              RecyclerView
     * @param context            上下文
     * @param datas              数据集
     * @param defaultExpandLevel 默认展开层级数 0为不展开
     * @param iconExpand         展开的图标
     * @param iconNoExpand       闭合的图标
     */
    public SimpleTreeRecyclerAdapter(RecyclerView mTree, Context context, List<Node> datas, int defaultExpandLevel, int iconExpand, int iconNoExpand) {
        super(mTree, context, datas, defaultExpandLevel, iconExpand, iconNoExpand);
    }

    public SimpleTreeRecyclerAdapter(RecyclerView mTree, Context context, List<Node> datas, int defaultExpandLevel) {
        super(mTree, context, datas, defaultExpandLevel);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(View.inflate(mContext, R.layout.adapter_tree_item, null));
    }

    @Override
    public void onBindViewHolder(final Node node, RecyclerView.ViewHolder holder, final int position) {

        final MyHolder viewHolder = (MyHolder) holder;
        //todo do something
        viewHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(node, viewHolder.cb.isChecked());
            }
        });

        viewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                 listener.onClick(mNodes.get(position), position);
                }
            }
        });

        if (node.isChecked()) {
            viewHolder.cb.setChecked(true);
        } else {
            viewHolder.cb.setChecked(false);
        }

        if (node.getIcon() == -1) {
            viewHolder.icon.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(node.getIcon());
        }

        int index = node.getName().indexOf("]") + 1;
        String nodeHead = node.getName().substring(0, index);
        String nodeDesc = node.getName().substring(index);
        String resultValue = "<font color='#7D7DFF'>" + nodeHead + "</font>" +
                "<font color='#FF000000'>  " + nodeDesc + "</font>";
        viewHolder.label.setText(Html.fromHtml(resultValue));
    }

    class MyHolder extends RecyclerView.ViewHolder {

        public CheckBox cb;

        public TextView label;

        public ImageView icon;

        public LinearLayout mItemView;

        public MyHolder(View itemView) {
            super(itemView);

            cb = (CheckBox) itemView
                    .findViewById(R.id.cb_select_tree);
            label = (TextView) itemView
                    .findViewById(R.id.id_treenode_label);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            mItemView = (LinearLayout) itemView.findViewById(R.id.tree_item);

        }

    }

    OnTreeItemClickListener listener;

    public void setOnTreeItemClickListener(OnTreeItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnTreeItemClickListener {
        void onClick(Node node, int position);
    }
}
