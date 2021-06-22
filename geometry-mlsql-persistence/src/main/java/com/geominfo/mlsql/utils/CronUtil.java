package com.geominfo.mlsql.utils;

import com.geominfo.mlsql.domain.vo.TaskScheduleVo;

/**
 * @Classname CronUtil
 * @Description TODO
 * @Date 2020/3/26
 * @Created by xqh
 */
public class CronUtil {

    /**
     *
     *方法摘要：构建Cron表达式
     *@param  taskScheduleVo
     *@return String
     */
    public static String createCronExpression(TaskScheduleVo taskScheduleVo) throws Exception{
        StringBuffer cronExp = new StringBuffer("");

        if(null == taskScheduleVo.getJobType()) {
            System.out.println("执行周期未配置" );//执行周期未配置
        }

        if (null != taskScheduleVo.getSecond()
                && null == taskScheduleVo.getMinute()
                && null == taskScheduleVo.getHour()){
            //每隔几秒
            if (taskScheduleVo.getJobType().intValue() == 0) {
                cronExp.append("0/").append(taskScheduleVo.getSecond());
                cronExp.append(" ");
                cronExp.append("* ");
                cronExp.append("* ");
                cronExp.append("* ");
                cronExp.append("* ");
                cronExp.append("?");
            }

        }

        if (null != taskScheduleVo.getSecond()
                && null != taskScheduleVo.getMinute()
                && null == taskScheduleVo.getHour()){
            //每隔几分钟
            if (taskScheduleVo.getJobType().intValue() == 1) {
                cronExp.append("* ");
                cronExp.append("0/").append(taskScheduleVo.getMinute());
                cronExp.append(" ");
                cronExp.append("* ");
                cronExp.append("* ");
                cronExp.append("* ");
                cronExp.append("?");
            }

        }

        if (null != taskScheduleVo.getSecond()
                && null != taskScheduleVo.getMinute()
                && null == taskScheduleVo.getHour()){
            //每小时
            if (taskScheduleVo.getJobType().intValue() == 2) {
                cronExp.append(taskScheduleVo.getSecond());
                cronExp.append(" ");
                cronExp.append(taskScheduleVo.getMinute());
                cronExp.append(" ");
                cronExp.append("* ");
                cronExp.append("* ");
                cronExp.append("* ");
                cronExp.append("?");
            }
        }

        if (null != taskScheduleVo.getSecond()
                && null != taskScheduleVo.getMinute()
                && null != taskScheduleVo.getHour()) {
            //秒
            cronExp.append(taskScheduleVo.getSecond()).append(" ");
            //分
            cronExp.append(taskScheduleVo.getMinute()).append(" ");
            //小时
            cronExp.append(taskScheduleVo.getHour()).append(" ");

            //每天
            if(taskScheduleVo.getJobType().intValue() == 3){
                cronExp.append("* ");//日
                cronExp.append("* ");//月
                cronExp.append("?");//周
            }
            //按每周
            else if(taskScheduleVo.getJobType().intValue() == 4){
                //一个月中第几天
                cronExp.append("? ");
                //月份
                cronExp.append("* ");
                //周
                Integer[] weeks = taskScheduleVo.getDayOfWeeks();
                for(int i = 0; i < weeks.length; i++){
                    if(i == 0){
                        cronExp.append(weeks[i]);
                    } else{
                        cronExp.append(",").append(weeks[i]);
                    }
                }
            }
            //按每月
            else if(taskScheduleVo.getJobType().intValue() == 5){
                //一个月中的哪几天
                Integer[] days = taskScheduleVo.getDayOfMonths();
                for(int i = 0; i < days.length; i++){
                    if(i == 0){
                        cronExp.append(days[i]);
                    } else{
                        cronExp.append(",").append(days[i]);
                    }
                }
                //月份
                cronExp.append(" * ");
                //周
                cronExp.append("?");
            }
        }
        return cronExp.toString();
    }

    /**
     *
     *方法摘要：生成计划的详细描述
     *@param  taskScheduleVo
     *@return String
     */
    public static String createDescription(TaskScheduleVo taskScheduleVo){
        StringBuffer description = new StringBuffer("");

        if (null != taskScheduleVo.getSecond()
                && null != taskScheduleVo.getMinute()
                && null != taskScheduleVo.getHour()) {
            //按每天
            if(taskScheduleVo.getJobType().intValue() == 1){
                description.append("每天");
                description.append(taskScheduleVo.getHour()).append("时");
                description.append(taskScheduleVo.getMinute()).append("分");
                description.append(taskScheduleVo.getSecond()).append("秒");
                description.append("执行");
            }

            //按每周
            else if(taskScheduleVo.getJobType().intValue() == 3){
                if(taskScheduleVo.getDayOfWeeks() != null && taskScheduleVo.getDayOfWeeks().length > 0) {
                    String days = "";
                    for(int i : taskScheduleVo.getDayOfWeeks()) {
                        days += "周" + i;
                    }
                    description.append("每周的").append(days).append(" ");
                }
                if (null != taskScheduleVo.getSecond()
                        && null != taskScheduleVo.getMinute()
                        && null != taskScheduleVo.getHour()) {
                    description.append(",");
                    description.append(taskScheduleVo.getHour()).append("时");
                    description.append(taskScheduleVo.getMinute()).append("分");
                    description.append(taskScheduleVo.getSecond()).append("秒");
                }
                description.append("执行");
            }

            //按每月
            else if(taskScheduleVo.getJobType().intValue() == 2){
                //选择月份
                if(taskScheduleVo.getDayOfMonths() != null && taskScheduleVo.getDayOfMonths().length > 0) {
                    String days = "";
                    for(int i : taskScheduleVo.getDayOfMonths()) {
                        days += i + "号";
                    }
                    description.append("每月的").append(days).append(" ");
                }
                description.append(taskScheduleVo.getHour()).append("时");
                description.append(taskScheduleVo.getMinute()).append("分");
                description.append(taskScheduleVo.getSecond()).append("秒");
                description.append("执行");
            }

        }
        return description.toString();
    }

    //参考例子
    public static void main(String[] args) {
        //执行时间：每天的12时12分12秒 start
        TaskScheduleVo taskScheduleVo = new TaskScheduleVo();

        String cropExp = null;
        try {
            taskScheduleVo.setJobType(0);//按每秒
            taskScheduleVo.setSecond(30);
            String cronExp = createCronExpression(taskScheduleVo);
            System.out.println(cronExp);

            taskScheduleVo.setJobType(4);//按每分钟
            taskScheduleVo.setMinute(8);
            String cronExpp = createCronExpression(taskScheduleVo);
            System.out.println(cronExpp);

            taskScheduleVo.setJobType(1);//按每天
            Integer hour = 12; //时
            Integer minute = 12; //分
            Integer second = 12; //秒
            taskScheduleVo.setHour(hour);
            taskScheduleVo.setMinute(minute);
            taskScheduleVo.setSecond(second);
            cropExp = createCronExpression(taskScheduleVo);
            System.out.println(cropExp + ":" + createDescription(taskScheduleVo));
            //执行时间：每天的12时12分12秒 end

            taskScheduleVo.setJobType(3);//每周的哪几天执行
            Integer[] dayOfWeeks = new Integer[3];
            dayOfWeeks[0] = 1;
            dayOfWeeks[1] = 2;
            dayOfWeeks[2] = 3;
            taskScheduleVo.setDayOfWeeks(dayOfWeeks);
            cropExp = createCronExpression(taskScheduleVo);
            System.out.println(cropExp + ":" + createDescription(taskScheduleVo));

            taskScheduleVo.setJobType(2);//每月的哪几天执行
            Integer[] dayOfMonths = new Integer[3];
            dayOfMonths[0] = 1;
            dayOfMonths[1] = 21;
            dayOfMonths[2] = 13;
            taskScheduleVo.setDayOfMonths(dayOfMonths);
            cropExp = createCronExpression(taskScheduleVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(cropExp + ":" + createDescription(taskScheduleVo));

    }

    /**
     * 组装Cron表达式
     * @param month
     * @param hour
     * @param day
     * @throws Exception
     */
    public static String AssembleCron(String month,String day,Integer hour)throws Exception{
        TaskScheduleVo taskScheduleVo = new TaskScheduleVo();
        switch (month){
            case "month":
                taskScheduleVo.setJobType(5);
                Integer[] dayOfMonths = new Integer[1];
                dayOfMonths[0] = new Integer(day);
                taskScheduleVo.setHour(0);//时
                taskScheduleVo.setMinute(0);//分
                taskScheduleVo.setSecond(0);//秒
                taskScheduleVo.setDayOfMonths(dayOfMonths);
                break;
            case "day":
                taskScheduleVo.setJobType(3);
                taskScheduleVo.setHour(hour);
                taskScheduleVo.setMinute(0);//分
                taskScheduleVo.setSecond(0);//秒
                break;
            case "hour":
                taskScheduleVo.setJobType(2);
                taskScheduleVo.setMinute(hour);//分
                taskScheduleVo.setSecond(0);//秒
                break;
        }
        return createCronExpression(taskScheduleVo);
    }

}