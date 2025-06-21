package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 *
 * @TableName record
 */
@TableName(value ="record")
@Data
public class Record {
    /**
     *
     */
    @TableField(exist = false)
    private String action;

    @TableField(exist = false)
    private String storagename;


    @TableField(exist = false)
    private String goodstypename;

    @TableField(exist = false)
    private String bookname;

    @TableField(exist = false)
    private String username;


    @TableField(exist = false)
    private String adminname;


    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 书本id
     */
    private Integer book;

    /**
     * 取货人id
     */
    private Integer userid;

    /**
     * 操作人id
     */
    private Integer adminid;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 备注
     */
    private String remark;

    /**
     *
     */
    private String imagepath;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Record other = (Record) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getBook() == null ? other.getBook() == null : this.getBook().equals(other.getBook()))
            && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()))
            && (this.getAdminid() == null ? other.getAdminid() == null : this.getAdminid().equals(other.getAdminid()))
            && (this.getCount() == null ? other.getCount() == null : this.getCount().equals(other.getCount()))
            && (this.getCreatetime() == null ? other.getCreatetime() == null : this.getCreatetime().equals(other.getCreatetime()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getImagepath() == null ? other.getImagepath() == null : this.getImagepath().equals(other.getImagepath()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBook() == null) ? 0 : getBook().hashCode());
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        result = prime * result + ((getAdminid() == null) ? 0 : getAdminid().hashCode());
        result = prime * result + ((getCount() == null) ? 0 : getCount().hashCode());
        result = prime * result + ((getCreatetime() == null) ? 0 : getCreatetime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getImagepath() == null) ? 0 : getImagepath().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", book=").append(book);
        sb.append(", userid=").append(userid);
        sb.append(", adminid=").append(adminid);
        sb.append(", count=").append(count);
        sb.append(", createtime=").append(createtime);
        sb.append(", remark=").append(remark);
        sb.append(", imagepath=").append(imagepath);
        sb.append("]");
        return sb.toString();
    }
}
