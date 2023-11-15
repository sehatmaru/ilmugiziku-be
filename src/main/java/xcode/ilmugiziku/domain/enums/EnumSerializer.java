package xcode.ilmugiziku.domain.enums;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

@Component
public class EnumSerializer extends JsonSerializer<Enum> {

  private static I18nProperties i18nProperties;

  private static MessageSource messageSource;

  public EnumSerializer() {}

  @Autowired
  public void setMessageSource(MessageSource messageSource) {
    EnumSerializer.messageSource = messageSource;
  }

  @Autowired
  public void setI18nProperties(I18nProperties i18nProperties) {
    EnumSerializer.i18nProperties = i18nProperties;
  }

  public String getI18nString(I18NEnum i18nEnum) {
    String localMsg;
    if (i18nProperties.getForceUseID()) {
      Locale locale = new Locale("id", "ID");
      localMsg = messageSource.getMessage(i18nEnum.
              desc(), new String[0], "", locale);
    } else {
      localMsg =
          messageSource.getMessage(
              i18nEnum.desc(), new String[0], "", LocaleContextHolder.getLocale());
    }

    if (StringUtils.isEmpty(localMsg)) {
      localMsg = messageSource.getMessage(i18nEnum.desc(), new String[0], "", Locale.US);
    }

    return localMsg;
  }

  @Override
  public void serialize(Enum value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    if (value instanceof I18NEnum && i18nProperties.getEnable()) {
      I18NEnum _val = (I18NEnum) value;
      gen.writeStartObject();
      gen.writeFieldName("key");
      gen.writeString(_val.name());
      gen.writeFieldName("value");
      gen.writeString(getI18nString(_val));
      gen.writeEndObject();
    } else {
      gen.writeString(value.name());
    }
  }
}
