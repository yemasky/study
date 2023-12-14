package cn.xueden.common.core.utils;

import java.util.List;

/**功能描述:Layer工具类
 * @Auther:梁志杰
 * @Date:2020/9/25
 * @Description:cn.xueden.common.core.utils
 * @version:1.0
 */
public class LayerData<T> {
    private Integer code = 0;

    private Integer count;

    private List<T> data;

    private String msg = "";

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
