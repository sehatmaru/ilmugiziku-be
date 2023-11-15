package xcode.ilmugiziku.domain.enums;

public enum ExamStatusEnum implements I18NEnum {
    REGISTERED("Registered"),
    UPCOMING("Upcoming"),
    NOT_REGISTERED("Not Registered"),
    COMPLETED("Completed"),
    CLOSED("Closed"),
    ON_GOING("On Going");

    private final String desc;

    ExamStatusEnum(String desc) {
        this.desc = desc;
    }

    @Override
    public String desc() {
        return desc;
    }
}
