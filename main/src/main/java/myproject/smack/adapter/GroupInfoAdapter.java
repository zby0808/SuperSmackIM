package myproject.smack.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.jivesoftware.smackx.muc.Occupant;

import myproject.R;

/**
 * Created by Administrator on 2018/12/13.
 */

public class GroupInfoAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    public GroupInfoAdapter() {
        super(R.layout.adapter_group_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        Occupant occupant = (Occupant) item;
        helper.setText(R.id.tv_group_info_adapter_name,occupant.getNick());
    }
}
