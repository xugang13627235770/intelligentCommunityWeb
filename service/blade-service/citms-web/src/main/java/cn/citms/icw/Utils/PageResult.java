package cn.citms.icw.Utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * @author liuyuyang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageResult<T> extends Page<T> {
    private static final long serialVersionUID = 1671413920270661424L;

    public  PageResult(long current, long size, long total, List<T> records){
        super(current, size, total);
        super.setRecords(records);
    }
}
