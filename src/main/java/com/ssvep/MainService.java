/**
 * 这个文件是一个 Java 应用程序的入口点，用于启动一个嵌入式的 Tomcat 服务器，并注册多个控制器（Controller）以处理不同的 HTTP 请求。
 * 
 * @author 石振山
 * @version 3.4.3
 */

package com.ssvep;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ssvep.controller.TestRecordController;
import com.ssvep.controller.UserController;
import com.ssvep.controller.AnalysisReportController;
import com.ssvep.controller.LogsController;
import com.ssvep.controller.RecommendationController;
import com.ssvep.controller.StimulusVideoController;

import java.io.File;

public class MainService {
    private static final Logger logger = LogManager.getLogger(MainService.class);

    public static void main(String[] args) throws Exception {
        // 创建 Tomcat 实例
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        logger.info("Tomcat 实例已创建，端口号：8080");

        // 创建应用上下文
        Context ctx = tomcat.addContext("", new File(".").getAbsolutePath());
        logger.info("应用上下文已创建，路径：{}", new File(".").getAbsolutePath());

        // 添加各个 Controller 的实例
        UserController userController = new UserController();
        TestRecordController testRecordController = new TestRecordController();
        StimulusVideoController stimulusVideoController = new StimulusVideoController();
        RecommendationController recommendationController = new RecommendationController();
        LogsController logsController = new LogsController();
        AnalysisReportController analysisReportController = new AnalysisReportController();

        // 注册每个 Controller
        Tomcat.addServlet(ctx, "userController", userController);
        ctx.addServletMappingDecoded("/users", "userController");

        Tomcat.addServlet(ctx, "testRecordController", testRecordController);
        ctx.addServletMappingDecoded("/testRecords", "testRecordController");

        Tomcat.addServlet(ctx, "stimulusVideoController", stimulusVideoController);
        ctx.addServletMappingDecoded("/stimulusVideos", "stimulusVideoController");

        Tomcat.addServlet(ctx, "recommendationController", recommendationController);
        ctx.addServletMappingDecoded("/recommendations", "recommendationController");

        Tomcat.addServlet(ctx, "logsController", logsController);
        ctx.addServletMappingDecoded("/logs", "logsController");

        Tomcat.addServlet(ctx, "analysisReportController", analysisReportController);
        ctx.addServletMappingDecoded("/analysisReports", "analysisReportController");

        // 启动服务器
        try {
            logger.info("启动 Tomcat...");
            tomcat.start();
            logger.info("Tomcat 启动成功！");
        } catch (Exception e) {
            logger.error("Tomcat 启动失败，错误信息：", e);
        }
        tomcat.getServer().await();
    }
}
