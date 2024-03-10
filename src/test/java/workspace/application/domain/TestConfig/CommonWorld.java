package workspace.application.domain.TestConfig;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Properties;

@Slf4j
@Component
public class CommonWorld {

    public static Properties commonConfig;

    private void readAllProperty(){
        commonConfig = new Properties();
        Collection<File> files = FileUtils.listFiles(Paths.get("src/test/java/workspace/application/domain").toFile(),new String[]{"properties"},true);
        if(!files.isEmpty()){
            files.forEach(conf -> {
                try {
                    commonConfig.load(new FileReader(conf));
                } catch (IOException e) {
                }
            });
        }
    }

    @PostConstruct
    public void init(){
        readAllProperty();
    }

}
