package xcode.marsiajar.domain.response.exam;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RankResponse {
    private String name;
    private int duration;
    private double score;
    private int correct;
    private int incorrect;
    private int blank;
    private Date startTime;
    private Date finishTime;
    private int rank;
}
