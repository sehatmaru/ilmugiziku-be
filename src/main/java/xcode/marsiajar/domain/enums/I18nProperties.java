package xcode.marsiajar.domain.enums;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "i18n")
@Data
public class I18nProperties {

  private Boolean enable = true;

  private Boolean forceUseID = false;
}
