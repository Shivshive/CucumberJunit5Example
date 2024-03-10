package workspace.application.domain.core;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import workspace.application.domain.TestConfig.CommonWorld;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.TimeZone;

@Slf4j
class FreemarkerEngine {

    private static Template template;
    private static Configuration configuration;

    private static final FreemarkerEngine freemarkerEngine = new FreemarkerEngine();

    private static final String TEMPLATE_DIR = CommonWorld.commonConfig.getProperty("templates.dir");

    private FreemarkerEngine(){}

    public static FreemarkerEngine fromTemplate(String templatePath)  {
            Configuration configuration = getConfigurationInstance();
        try {
            template = configuration.getTemplate(templatePath);
        } catch (IOException e) {
            Assertions.fail(String.format("Unable to getTemplate due to ==> %s",e.getMessage()));
        }
        return freemarkerEngine;
    }

    public String build(Map<String, Object> dataModel) {
        StringWriter stringWriter = new StringWriter();
        if(template!=null){
            try {
                template.process(dataModel,stringWriter);
                return stringWriter.toString();
            } catch (TemplateException | IOException e) {
                e.printStackTrace();
                log.error(String.format("Unable to process template due to  ==>  %s",e.getMessage()));
            }
        }
        return null;
    }

    private static Configuration getConfigurationInstance(){
        if(configuration==null){
            configuration = new Configuration(Configuration.VERSION_2_3_20);
            configuration.setWrapUncheckedExceptions(false);
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            configuration.setLogTemplateExceptions(false);
            configuration.setFallbackOnNullLoopVariable(false);
            configuration.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
        }
        return configuration;
    }
}
