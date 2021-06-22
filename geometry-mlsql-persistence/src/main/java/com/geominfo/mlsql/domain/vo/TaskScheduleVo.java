package com.geominfo.mlsql.domain.vo;

/**
 * @program: geometry-bi
 * @description:
 * @author: xiaoqiaohui
 * @create: 2020-03-24 09:22
 **/
public class TaskScheduleVo {

    /**
     * 所选作业类型:
     * 0  -> 每秒
     * 1  -> 每分
     * 2  -> 每时
     * 3  -> 每天
     * 4  -> 每周
     * 5  -> 每月
     */
    Integer jobType;

    /**月*/
    Integer[] dayOfMonths;

    /**天*/
    Integer[] dayOfWeeks;

    /**秒  */
    Integer second;

    /**分  */
    Integer minute;

    /**时  */
    Integer hour;

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public Integer[] getDayOfMonths() {
        return dayOfMonths;
    }

    public void setDayOfMonths(Integer[] dayOfMonths) {
        this.dayOfMonths = dayOfMonths;
    }

    public Integer[] getDayOfWeeks() {
        return dayOfWeeks;
    }

    public void setDayOfWeeks(Integer[] dayOfWeeks) {
        this.dayOfWeeks = dayOfWeeks;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }
}
