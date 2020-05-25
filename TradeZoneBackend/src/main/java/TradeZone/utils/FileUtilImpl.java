package TradeZone.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

@Component
public class FileUtilImpl implements FileUtil {

    @Override
    public String fileContent(String path) {

        StringBuilder sb = new StringBuilder();

        File file = new File(path);

        String line;

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }

            return sb.toString().trim();

        } catch (Exception e) {
            return null;
        }
    }
}
