package myproject.smack.myview.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bangyong.zhang on 2018/6/26
 */
public class Node<T, B> {

    /**
     * 传入的实体对象
     */
    public B bean;

    private boolean isHaveChild;//是否有下级子节点

    private boolean isAdd;//是否添加子节点

    private int treeType = -1;//树种类，根据该字段处理不同业务树之间差异问题，目前:0表示三级督察组织架构树，其它则默认

    private int taskId;//督察类别任务id

    private boolean isShowHead;//是否隐藏头部

    private String headDesc;//头部描述

    public String getHeadDesc() {
        return headDesc;
    }

    public void setHeadDesc(String headDesc) {
        this.headDesc = headDesc;
    }

    public boolean isShowHead() {
        return isShowHead;
    }

    public void setShowHead(boolean showHead) {
        isShowHead = showHead;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTreeType() {
        return treeType;
    }

    public void setTreeType(int treeType) {
        this.treeType = treeType;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public boolean isHaveChild() {
        return isHaveChild;
    }

    public void setHaveChild(boolean haveChild) {
        isHaveChild = haveChild;
    }

    /**
     * 设置开启 关闭的图片
     */
    public int iconExpand = -1, iconNoExpand = -1;

    private T id;
    /**
     * 根节点pId为0
     */
    private T pId;

    private String name;

    /**
     * 当前的级别
     */
    private int level;

    /**
     * 是否展开
     */
    private boolean isExpand = false;

    private int icon = -1;

    /**
     * 下一级的子Node
     */
    private List<Node> children = new ArrayList<>();

    /**
     * 父Node
     */
    private Node parent;
    /**
     * 是否被checked选中
     */
    private boolean isChecked;
    /**
     * 是否为新添加的
     */
    public boolean isNewAdd = true;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public Node() {
    }

    public Node(T id, T pId, String name) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
    }

    public Node(T id, T pId, String name, B bean) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.bean = bean;
    }


    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public T getpId() {
        return pId;
    }

    public void setpId(T pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * 是否为跟节点
     *
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * 判断父节点是否展开
     *
     * @return
     */
    public boolean isParentExpand() {
        if (parent == null)
            return false;
        return parent.isExpand();
    }

    /**
     * 是否是叶子界点
     *
     * @return
     */
    public boolean isLeaf() {
        return children.size() == 0;
    }

    /**
     * 获取level
     */
    public int getLevel() {

        return parent == null ? 0 : parent.getLevel() + 1;
    }

    /**
     * 设置展开
     *
     * @param isExpand
     */
    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
        if (!isExpand) {

            for (Node node : children) {
                node.setExpand(isExpand);
            }
        }
    }

}
