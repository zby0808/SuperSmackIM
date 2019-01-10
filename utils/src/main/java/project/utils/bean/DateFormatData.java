package project.utils.bean;

/**
 * 实现功能
 *Created by zby on 2016/12/9.
 */
public class DateFormatData {
        private long differ;   //结果差
        private Boolean day; //日
        private Boolean hour;        // 小时
        private Boolean min;        // 分钟

        public Boolean getSecond() {
            return second;
        }

        public void setSecond(Boolean second) {
            this.second = second;
        }

        public Boolean getMin() {
            return min;
        }

        public void setMin(Boolean min) {
            this.min = min;
        }

        public Boolean getHour() {
            return hour;
        }

        public void setHour(Boolean hour) {
            this.hour = hour;
        }

        public long getDiffer() {
            return differ;
        }

        public void setDiffer(long differ) {
            this.differ = differ;
        }

        public Boolean getDay() {
            return day;
        }

        public void setDay(Boolean day) {
            this.day = day;
        }

        private Boolean second;        // 秒
        private String differDesc; // 字符显示

        public String getDifferDesc() {
            return differDesc;
        }

        public void setDifferDesc(String differDesc) {
            this.differDesc = differDesc;
        }

}
