package xcode.ilmugiziku.domain.enums;

public enum ColorEnum implements I18NEnum {
    PRIMARY("primary"),
    SECONDARY("secondary"),
    SUCCESS("success"),
    DANGER("danger"),
    WARNING("warning"),
    INFO("info"),
    LIGHT("light"),
    DARK("dark"),
    LINK("link");

    private final String desc;

    ColorEnum(String desc) {
        this.desc = desc;
    }

    @Override
    public String desc() {
        return desc;
    }
}
