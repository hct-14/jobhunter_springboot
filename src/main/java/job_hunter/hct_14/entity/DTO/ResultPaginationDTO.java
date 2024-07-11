package job_hunter.hct_14.entity.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDTO {
    private Meta meta;
    private  Object result;


    public static class Meta{
        private int page;
        private int pageSize;
        private int pages;
        private long total;
    }
}
