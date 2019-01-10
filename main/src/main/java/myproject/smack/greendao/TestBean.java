package myproject.smack.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/12/28.
 */

@Entity
public class TestBean {
    @Id
    private String name;

    @Generated(hash = 1303329926)
    public TestBean(String name) {
        this.name = name;
    }

    @Generated(hash = 2087637710)
    public TestBean() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
