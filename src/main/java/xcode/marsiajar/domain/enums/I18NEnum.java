package xcode.marsiajar.domain.enums;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = EnumSerializer.class)
public interface I18NEnum {

  String desc();

  String name();
}
