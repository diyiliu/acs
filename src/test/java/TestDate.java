import com.diyiliu.util.DateUtil;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Description: TestDate
 * Author: DIYILIU
 * Update: 2017-09-22 15:46
 */
public class TestDate {

    @Test
    public void test(){


        for (int i = 0; i <= 3; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, i);
            Date date = calendar.getTime();
            String monthTable = DateUtil.dateToString(date, "%1$tY%1$tm");

            System.out.println(monthTable);
        }
    }
}
